package fyp.com.riona.Search;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;

import com.bumptech.glide.Glide;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Favrt_List;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.ProductDetails.ProductDetailsActivity;
import fyp.com.riona.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
@SuppressWarnings("unchecked")
public class SearchProductsRecyclerAdapter extends RecyclerView.Adapter<SearchProductsRecyclerAdapter.MyViewHolder> implements Filterable {
    ////////////////Declare Variable//////////////////
    private Context context;
    ArrayList<FeaturedProductDataModel> dataModelArrayList;
    String offer_price;
    SharedPreferences featuredprefernce;
    String islogin,user_type;
    Activity act;
    String token,newtoken;

    SharedPreferences search_main_pref;
    SharedPreferences.Editor sharedPreferencesEditor_main_search ;

    ArrayList<FeaturedProductDataModel> dataModelArrayListFull ;

    ///  Progress Bar Declare  //////
    ProgressDialog progress;

    public SearchProductsRecyclerAdapter(Context context, ArrayList<FeaturedProductDataModel> dataModelArrayList, Activity act) {

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
        this.act = act;
        this.dataModelArrayListFull = new ArrayList<>(dataModelArrayList);
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.featured_products_recycler_item , viewGroup , false));
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, final int i) {

        progress= new ProgressDialog(act);
        progress.setMessage("Loading..");
        progress.setCancelable(true);

        String inner_id = dataModelArrayList.get(i).getFavrt_bit();


        if(inner_id!=null && !inner_id.isEmpty())
        {
            if (inner_id.equals("1")) {
                myViewHolder.fav_button.setBackgroundResource(R.drawable.ic_favorite_red);
            }
            else
            {


                myViewHolder.fav_button.setBackgroundResource(R.drawable.ic_like);

            }
        }
        else
        {

            myViewHolder.fav_button.setBackgroundResource(R.drawable.ic_like);
        }



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
        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        featuredprefernce = context.getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = featuredprefernce.getString("user_type", "");
        token = featuredprefernce.getString("token","");
        islogin = featuredprefernce.getString("islogin", "");
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
        } else {
            ////////////////For Professional Product///////////////////////
            if (user_type != null && !user_type.isEmpty()) {
                if (user_type.contains("1")) {
                    ////////////////Offer Price is not available //////////////////
                    if (offer_price.equals("0")) {
                        myViewHolder.price_off.setVisibility(View.INVISIBLE);
                        myViewHolder.product_price.setText("Rs:" + dataModelArrayList.get(i).getPrice());

                    } else {

                        ////////////////Offer Price is  available //////////////////
                        myViewHolder.price_off.setVisibility(View.VISIBLE);
                        myViewHolder.price_off.setText(dataModelArrayList.get(i).getPrice());
                        myViewHolder.product_price.setText("Rs:" + dataModelArrayList.get(i).getOfferPrice());
                    }
                } else {
                    ////////////////When user is Consumer //////////////////
                    myViewHolder.price_off.setVisibility(View.GONE);
                    myViewHolder.product_price.setVisibility(View.GONE);
                }
            } else {
                ////////////////When user is Professional or not Login //////////////////
                myViewHolder.price_off.setVisibility(View.GONE);
                myViewHolder.product_price.setVisibility(View.GONE);

            }

        }
        myViewHolder.fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////////////Cehck User is Login or not///////////////////////


                if (islogin != null && !islogin.isEmpty()) {
                    if (user_type.contains("0")) {


                        if (dataModelArrayList.get(i).getFavrt_bit() != null && !dataModelArrayList.get(i).getFavrt_bit().isEmpty()) {

                            if (dataModelArrayList.get(i).getFavrt_bit().contains("1")) {
                                progress.show();
                                newtoken = "bearer   " + token;
                                delete_favrt_consumer(newtoken, dataModelArrayList.get(i).getId());
                            } else {
                                newtoken = "bearer   " + token;
                                add_favrt_consumer(newtoken, dataModelArrayList.get(i).getId());
                            }
                        }
                        else
                        {
                            newtoken = "bearer   " + token;
                            add_favrt_consumer(newtoken, dataModelArrayList.get(i).getId());
                        }

                    }

                } else {
                    Toast.makeText(context, "Login First", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataModelArrayList.get(i).getProductType().equals("0")) {


                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    String id =dataModelArrayList.get(i).getId();
                    intent.putExtra("product_id",id);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                } else {

                    if (islogin != null && !islogin.isEmpty()) {

                            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                            alertDialog.setTitle("Alert");
                            alertDialog.setMessage("You have to login Professional ");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(context, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            context.startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();


                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("You have to login Professional ");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Proceed",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    }
                }
            }
        });
    }
    private void add_favrt_consumer(String token,String id) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Favrt_List> call = apiService.add_favrt_consumer(token,id);

        call.enqueue(new Callback<Favrt_List>() {
            @Override
            public void onResponse(Call<Favrt_List> call, Response<Favrt_List> response) {

                Favrt_List listFoodModel = response.body();
                if (listFoodModel != null) {
                    String  status = listFoodModel.getStatus();
                    ////////////////////////////////If Getting Error From Server   ///////////////////////////////////////////////////////////////
                    if(status.contains("401"))
                    {
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
                            }
                        });

                    }
                    else if(status.contains("0"))
                    {
                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(context, "Invalid request", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {




                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ////////////////////////////////////////////Set Data to  Recycler View Adapter ///////////////////////////////////////////////////////////////
                                Toast.makeText(context, "Successful  Added to Favourite", Toast.LENGTH_LONG).show();
                                String data = "1";
                                search_main_pref   = act.getSharedPreferences("search_data", MODE_PRIVATE);
                                sharedPreferencesEditor_main_search  = search_main_pref.edit();
                                sharedPreferencesEditor_main_search.putString("Searchfragment", data);
                                sharedPreferencesEditor_main_search.apply();
                                // ((BottomNavigationView).findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.bottom_nav_cart);
                                Intent intent = new Intent(act, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                act.startActivity(intent);
                            }
                        });
                    }

                } else {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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

                        Toast.makeText(context, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }
                });
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
                                String data = "1";
                                search_main_pref   = act.getSharedPreferences("search_data", MODE_PRIVATE);
                                sharedPreferencesEditor_main_search  = search_main_pref.edit();
                                sharedPreferencesEditor_main_search.putString("Searchfragment", data);
                                sharedPreferencesEditor_main_search.apply();
                                // ((BottomNavigationView).findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.bottom_nav_cart);
                                Intent intent = new Intent(act, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                act.startActivity(intent);
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
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<FeaturedProductDataModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(dataModelArrayListFull);

            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(FeaturedProductDataModel items : dataModelArrayListFull){
                    if (items.getTitle().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(items);
                    }
                }
            }

            FilterResults results = new FilterResults();

            results.values = filteredList;

            return  results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            dataModelArrayList.clear();
            dataModelArrayList.addAll((List)filterResults.values);
            notifyDataSetChanged();

        }
    };
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton fav_button;
        TextView product_price, product_name, price_off ;
        ImageView product_image;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fav_button = (ImageButton) itemView.findViewById(R.id.fav_Button);
            product_price = (TextView) itemView.findViewById(R.id.product_value);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            price_off = (TextView) itemView.findViewById(R.id.middleHalf);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            cardView = (CardView) itemView.findViewById(R.id.featured_product_recyclerView);
        }
    }
}
