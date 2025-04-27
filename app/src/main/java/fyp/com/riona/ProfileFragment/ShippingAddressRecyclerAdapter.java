package fyp.com.riona.ProfileFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.CartPayment.CartShippingList;
import fyp.com.riona.Login.LoginActivity;

import fyp.com.riona.R;

import fyp.com.riona.UserAddress;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ShippingAddressRecyclerAdapter extends RecyclerView.Adapter<ShippingAddressRecyclerAdapter.MyViewHolder> {
    ////////////////Declare Variable//////////////////
    private Context context;
    ArrayList<UserAddress> dataModelArrayList;
    SharedPreferences user_prefernce;
    SharedPreferences.Editor user_prefernce_editor;
    ProgressDialog progress;
    SharedPreferences user_preference;
    SharedPreferences.Editor user_preferenceEditor;
    String user_type;
    String user_token;
    String newtokenconsumer, newtokenprofessional;
    Activity act;
    String address_id;
    String address, country, city;
    Dialog dialog;

    public ShippingAddressRecyclerAdapter(Context context, ArrayList<UserAddress> dataModelArrayList, Activity act) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
        this.act = act;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipping_address_recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tv_address_name.setText(dataModelArrayList.get(i).getAddress() + "," + " " + dataModelArrayList.get(i).getCity() + "," + " " + dataModelArrayList.get(i).getCountry());
        myViewHolder.img_edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(act);
                dialog.setContentView(R.layout.custom_add_edit_address_layout);

                ImageView imageViewDialogClose = dialog.findViewById(R.id.close_dialogCustomAddress);
                final EditText et_addressDialog = dialog.findViewById(R.id.editText_Address_custom_dialog);
                final EditText et_cityDialog = dialog.findViewById(R.id.editText_city_custom_dialog);
                final EditText et_countryDialog = dialog.findViewById(R.id.editText_country_custom_dialog);
                Button btn_proceedDialog = dialog.findViewById(R.id.btn_proceed_customDialogAddress);
                et_addressDialog.setText(dataModelArrayList.get(i).getAddress());
                et_cityDialog.setText(dataModelArrayList.get(i).getCity());
                et_countryDialog.setText(dataModelArrayList.get(i).getCountry());
                address_id = dataModelArrayList.get(i).getId();

                btn_proceedDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        address = et_addressDialog.getText().toString().trim();
                        country = et_countryDialog.getText().toString().trim();
                        city = et_cityDialog.getText().toString().trim();
                        if (address.isEmpty()) {
                            Toast.makeText(act, "Please Enter Address", Toast.LENGTH_SHORT).show();
                        } else if (city.isEmpty()) {
                            Toast.makeText(act, "Please Enter City", Toast.LENGTH_SHORT).show();
                        } else if (country.isEmpty()) {
                            Toast.makeText(act, "Please Enter Country", Toast.LENGTH_SHORT).show();
                        } else {
                            String address_type = "1";
                            user_preference = act.getSharedPreferences("signupdata", MODE_PRIVATE);
                            user_type = user_preference.getString("user_type", "");
                            user_token = user_preference.getString("token", null);
                            if (user_type != null && !user_type.isEmpty()) {
                                progress = new ProgressDialog(act);
                                progress.setMessage("Please Wait...");
                                progress.show();

                                    newtokenconsumer = "bearer   " + user_token;

                                    UpdateShippingAddressforconsumer(newtokenconsumer, address_id, address, country, city, address_type);

                            } else {
                                Intent intent = new Intent(act, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                act.startActivity(intent);

                            }
                        }


                    }
                });

                imageViewDialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    private void UpdateShippingAddressforconsumer(String token, String id, String address, String country, String city, String address_type) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CartShippingList> call = apiService.updateConsumerShippingAddress(token, id, address, city, country, address_type);

        call.enqueue(new Callback<CartShippingList>() {
            @Override
            public void onResponse(Call<CartShippingList> call, Response<CartShippingList> response) {
                /////Progress Dialog Dismiss  /////
                //   progress.dismiss();
                final CartShippingList listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        if (act != null) {
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Toast.makeText(act, "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = act.getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    Intent intent = new Intent(act, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    act.startActivity(intent);
                                }
                            });
                        }

                    } else if (status.contains("0")) {
                        if (act != null)
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Toast.makeText(act, "Maximum Address Already Added", Toast.LENGTH_SHORT).show();
                                }
                            });

                    } else {
                        if (act != null)
                            act.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    progress.dismiss();
                                    Toast.makeText(act, "Shipping Address Updated Successfully", Toast.LENGTH_SHORT).show();
                                    dataModelArrayList = new ArrayList<>(listFoodModel.getUserAddress());
                                    user_prefernce = act.getSharedPreferences("signupdata", MODE_PRIVATE);
                                    user_prefernce_editor = user_prefernce.edit();
                                    Gson gson = new Gson();
                                    if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                        String listofbillingaddress = gson.toJson(dataModelArrayList);
                                        user_prefernce_editor.putString("addressarraylist", listofbillingaddress);
                                    }
                                    user_prefernce_editor.apply();
                                    dialog.dismiss();
                                    notifyDataSetChanged();


                                }

                            });


                    }

                } else {
                    if (act != null)
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(act, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                }

            }

            @Override
            public void onFailure(Call<CartShippingList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                //     progress.dismiss();
                if (act != null)
                    act.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progress.dismiss();
                            Snackbar.make(act.findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_address_name;
        ImageView img_edit_address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_address_name = itemView.findViewById(R.id.address_title);
            img_edit_address = itemView.findViewById(R.id.address_edit_icon);
        }
    }
}
