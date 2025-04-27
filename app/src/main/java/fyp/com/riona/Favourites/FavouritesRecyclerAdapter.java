package fyp.com.riona.Favourites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Favrt_List;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.ProductDetails.ProductDetailsActivity;
import fyp.com.riona.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class FavouritesRecyclerAdapter extends RecyclerView.Adapter<FavouritesRecyclerAdapter.MyViewHolder> {
    Context context;
    String offer_price;
    SharedPreferences favouritesprefernce;
    String islogin,user_type;
    ArrayList<FavouriteDataModel> dataModelArrayList;
    Activity act;
    String token,newtoken;
    int position;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    public FavouritesRecyclerAdapter(Context context, ArrayList<FavouriteDataModel> dataModelArrayList,Activity act) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
        this.act = act;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favourites_recycler_item , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        progress= new ProgressDialog(act);
        progress.setMessage("Loading..");
        progress.setCancelable(true);
        //////////////////Set Data ///////////////////////
        offer_price = dataModelArrayList.get(i).getOfferPrice();
        if (dataModelArrayList.get(i).getThumbnail() != null && !dataModelArrayList.get(i).getThumbnail().isEmpty()) {
            Glide.with(context)
                    .load(context.getResources().getString(R.string.url) + dataModelArrayList.get(i).getThumbnail()).placeholder(R.drawable.placeholder)
                     .dontAnimate()
                    .into(myViewHolder.product_image);

        }
        if (dataModelArrayList.get(i).getTitle() != null && !dataModelArrayList.get(i).getTitle().isEmpty()) {
            myViewHolder.product_name.setText(dataModelArrayList.get(i).getTitle());
        }
        if (dataModelArrayList.get(i).getDescription() != null && !dataModelArrayList.get(i).getDescription().isEmpty()) {
            myViewHolder.prodcut_discription.setText(dataModelArrayList.get(i).getDescription());
        }
        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        favouritesprefernce = context.getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = favouritesprefernce.getString("user_type", "");
        token = favouritesprefernce.getString("token","");
        islogin = favouritesprefernce.getString("islogin", "");
        position =i;
        ////////////////Set Offer Price///////////////
        if (offer_price == null) {
            offer_price = "0";
        }
        //////////////////////Check User is Professional or Consumer ////////////////////////////
        if (dataModelArrayList.get(i).getProductType().equals("0")) {
            if (offer_price.equals("0")) {
                ////////////////Offer Price is not available //////////////////
                myViewHolder.price_off.setVisibility(View.INVISIBLE);
                myViewHolder.product_price.setText("Rs:" + dataModelArrayList.get(i).getPrice());

            } else {

                ////////////////Offer Price is  available //////////////////
                myViewHolder.price_off.setVisibility(View.VISIBLE);
                myViewHolder.price_off.setText("Rs:" + dataModelArrayList.get(i).getPrice());
                myViewHolder.price_off.setPaintFlags(myViewHolder.price_off.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                myViewHolder.product_price.setText("Rs:" + dataModelArrayList.get(i).getOfferPrice());
            }
        }
        else
        {
            if (offer_price.equals("0")) {
                ////////////////Offer Price is not available //////////////////
                myViewHolder.price_off.setVisibility(View.INVISIBLE);
                myViewHolder.product_price.setText("Rs:" + dataModelArrayList.get(i).getPrice());

            } else {

                ////////////////Offer Price is  available //////////////////
                myViewHolder.price_off.setVisibility(View.VISIBLE);
                myViewHolder.price_off.setText("Rs:" + dataModelArrayList.get(i).getPrice());
                myViewHolder.price_off.setPaintFlags(myViewHolder.price_off.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                myViewHolder.product_price.setText("Rs:" + dataModelArrayList.get(i).getOfferPrice());
            }
        }

      myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(context, ProductDetailsActivity.class);
              String id =dataModelArrayList.get(i).getId();
              intent.putExtra("product_id",id);
              intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);

          }
      });
        myViewHolder.del_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////Cehck User is Login or not///////////////////////


                if (islogin != null && !islogin.isEmpty()) {
                    if (user_type.contains("0")) {
                        newtoken = "bearer   " + token;
                        progress.show();
                        String id=dataModelArrayList.get(i).getId();
                        delete_favrt_consumer(newtoken,id);



                    }

                } else {
                    Toast.makeText(context, "Login First", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });




    }


    private void delete_favrt_consumer(String token,String id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Favrt_List> call = apiService.delete_favrt_consumer(token,id);

        call.enqueue(new Callback<Favrt_List>() {
            @Override
            public void onResponse(Call<Favrt_List> call, Response<Favrt_List> response) {
                progress.dismiss();
                Favrt_List listFoodModel = response.body();
                if (listFoodModel != null) {
                    String  status = listFoodModel.getStatus();
                    ////////////////////////////////If Getting Error From Server   ///////////////////////////////////////////////////////////////
                    if(status.contains("401"))
                    {
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(context, "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = context.getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
                                SharedPreferences.Editor editor2 = context.getSharedPreferences("favrtListhead", MODE_PRIVATE).edit();
                                editor2.clear(); //clear all stored data
                                editor2.apply();
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                act.finish();
                            }
                        });

                    }
                    else if(status.contains("0"))
                    {
                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(context, "No Record Found", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {




                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                ////////////////////////////////////////////Set Data to  Recycler View Adapter ///////////////////////////////////////////////////////////////
                                Toast.makeText(context, "Successfully Removed from Favourites", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(context, FavouritesActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                act.finish();
                               // removeAt(position);
                               // removeItem(id);



                            }
                        });
                    }

                } else {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Toast.makeText(context, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();
                        }
                    });


                }

            }

            @Override
            public void onFailure(Call<Favrt_List> call, Throwable t) {

                act.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progress.dismiss();

                        Toast.makeText(context, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

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
        ImageView del_button;
        TextView product_price, product_name , price_off,prodcut_discription ;
        ImageView product_image;
       LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            del_button = (ImageView) itemView.findViewById(R.id.favrt_delete_product);
            product_price = (TextView) itemView.findViewById(R.id.favrt_origional_price_product);
            product_name = (TextView) itemView.findViewById(R.id.favrt_titile_product);
            price_off = (TextView) itemView.findViewById(R.id.off_price_favourites_recycler_item);
            product_image = (ImageView) itemView.findViewById(R.id.favrt_Image_product);
            linearLayout =  itemView.findViewById(R.id.favrt_card_product);
            prodcut_discription = (TextView)itemView.findViewById(R.id.favrt_discription_product);



        }
    }
}
