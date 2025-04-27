package fyp.com.riona.SeeAllProducts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Favourites.FavouriteDataModel;
import fyp.com.riona.Favourites.Favourite_List;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductsRecyclerAdapter;
import fyp.com.riona.R;

import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.SeeAllProducts.ForConsumer.ConsumerProductsList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SeeAllProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String sub_inner_category_id, sub_inner_category_titile;
    String consumer_products_id;
    TextView tv_no_product;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    TextView tv_title;
    ArrayList<FeaturedProductDataModel> dataModelArrayList;
    LinearLayout linearLayout_sort_by_name,linearLayout_sort_by_price;
    int count_for_sort_by_name=0;
    SharedPreferences favouritesprefernce,cat_preference;
    String token,newtoken;
    String islogin,user_type;
    int i = 0;
    int j =0;
    ImageButton list_view_change,grid_view_change;

    SharedPreferences user_preference;

    ////////////// List For Favourite Product Data Model ///////////////////////////////////

    ArrayList<FavouriteDataModel> dataModelArrayList_favrt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_products);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        recyclerView = findViewById(R.id.allProducts_recyclerView);
        tv_no_product = findViewById(R.id.nodata_allProducts);
        tv_title = findViewById(R.id.tv_toolbar_allproduct);
        linearLayout_sort_by_name = findViewById(R.id.pro_recycler_sort_button_name);
        linearLayout_sort_by_price = findViewById(R.id.pro_recycler_sort_button_price);
        list_view_change = findViewById(R.id.pro_recycler_list_view_button);
        grid_view_change = findViewById(R.id.pro_recycler_grid_view_button);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading..");


        user_preference = getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = user_preference.getString("user_type", "");
        token = user_preference.getString("token","");


        cat_preference = getSharedPreferences("cate_third_id", MODE_PRIVATE);
        sub_inner_category_id = cat_preference.getString("sub_inner_category_id",null);
        sub_inner_category_titile = cat_preference.getString("sub_inner_category_name",null);
        consumer_products_id = cat_preference.getString("consumer_id",null);

        Toolbar toolbar = findViewById(R.id.toolbar_allProducts);
        toolbar.setTitle("");
        tv_title.setText(sub_inner_category_titile);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progress.show();


        if (sub_inner_category_id != null && !sub_inner_category_id.isEmpty()) {

            if (user_type.equals("0")) {
                if (token != null && !token.isEmpty()) {
                    newtoken = "bearer " + token;
                    getConsumerFavourite(newtoken);
                    getByInnerChaildCatId(sub_inner_category_id);

                } else {
                    getByInnerChaildCatId(sub_inner_category_id);
                }
            }

            else
            {
                getByInnerChaildCatId(sub_inner_category_id);
            }

        }


        if(consumer_products_id!=null && !consumer_products_id.isEmpty()) {
            if (consumer_products_id.equals("1")) {

                if(token!=null && !token.isEmpty())
                {
                    newtoken = "bearer " + token;
                    getConsumerFavourite(newtoken);
                    getConsumerAllProducts();
                    tv_title.setText("Consumer Products");

                }
                else
                {
                    getConsumerAllProducts();
                    tv_title.setText("Consumer Products");
                }

            }

            else if (consumer_products_id.equals("2")) {


            }
        }
        linearLayout_sort_by_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count_for_sort_by_name==0) {
                    sortData();
                    count_for_sort_by_name=1;
                }
                else {
                    sortData2();
                    count_for_sort_by_name=0;
                }
            }
        });
        linearLayout_sort_by_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
                favouritesprefernce = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE);
                user_type = favouritesprefernce.getString("user_type", "");
                token = favouritesprefernce.getString("token","");
                islogin = favouritesprefernce.getString("islogin", "");
                if (islogin != null && !islogin.isEmpty()) {

                    if (user_type.contains("0")) {
                        if(i==0) {
                            orderbyconsumer(dataModelArrayList);
                            i=1;
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));


                        }
                        else {
                            reverseorderbyconsumer(dataModelArrayList);
                            i=0;
                            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                            recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));


                        }
                    }

                }


            }
        });

        list_view_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));

                recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            }
        });
        grid_view_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));

            }
        });
    }
    private static void order(List<FeaturedProductDataModel> persons) {

        Collections.sort(persons, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {

                String x1 = ((FeaturedProductDataModel) o1).getPrice();
                String x2 = ((FeaturedProductDataModel) o2).getPrice();
                int sComp = Integer.valueOf(x1).compareTo(Integer.valueOf(x2));
                return sComp;

            }});
    }
    private static void orderbyconsumer(List<FeaturedProductDataModel> persons) {

        Collections.sort(persons, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {

                String x1 = ((FeaturedProductDataModel) o1).getPrice();
                String x2 = ((FeaturedProductDataModel) o2).getPrice();
                int sComp = Integer.valueOf(x1).compareTo(Integer.valueOf(x2));
                return sComp;

            }});
    }
    private static void reverseorder(List<FeaturedProductDataModel> persons) {

        Collections.sort(persons, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {

                String x1 = ((FeaturedProductDataModel) o1).getPrice();
                String x2 = ((FeaturedProductDataModel) o2).getPrice();
                int sComp = -Integer.valueOf(x1).compareTo(Integer.valueOf(x2));
                return sComp;

            }
        });
    }
    private static void reverseorderbyconsumer(List<FeaturedProductDataModel> persons) {

        Collections.sort(persons, new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {

                String x1 = ((FeaturedProductDataModel) o1).getPrice();
                String x2 = ((FeaturedProductDataModel) o2).getPrice();
                int sComp = -Integer.valueOf(x1).compareTo(Integer.valueOf(x2));
                return sComp;

            }
        });
    }


    private void sortData()
    {
        Collections.sort(dataModelArrayList, new Comparator<FeaturedProductDataModel>() {
            @Override
            public int compare(FeaturedProductDataModel lhs, FeaturedProductDataModel rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));


    }
    private void sortData2()
    {
        Collections.sort(dataModelArrayList, new Comparator<FeaturedProductDataModel>() {

            @Override
            public int compare(FeaturedProductDataModel o1, FeaturedProductDataModel o2) {

                return -o1.getTitle().compareTo(o2.getTitle());

            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));


    }

    public void getConsumerAllProducts() {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumerProductsList> call = apiService.getConsumerProducts();

        call.enqueue(new Callback<ConsumerProductsList>() {
            @Override
            public void onResponse(Call<ConsumerProductsList> call, retrofit2.Response<ConsumerProductsList> response) {
                final ConsumerProductsList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progress.dismiss();

                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList = new ArrayList<>(listofhome.getProducts());


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {




                                if(dataModelArrayList!=null && !dataModelArrayList.isEmpty()) {
                                    ////////////////////////////////////////////Set Data to  Recycler View///////////////////////////////////////////////////////////////

                                    if(dataModelArrayList_favrt!=null && !dataModelArrayList_favrt.isEmpty())
                                    {


                                        for (int index = 0; index < dataModelArrayList.size(); index++) {

                                            for (int index2 = 0; index2 < dataModelArrayList_favrt.size(); index2++) {

                                                String id = dataModelArrayList_favrt.get(index2).getId();
                                                if (dataModelArrayList.get(index).getId().equals(id)) {
                                                    String inner_id = dataModelArrayList.get(index).getId();
                                                    String title = dataModelArrayList.get(index).getTitle();
                                                    String price = dataModelArrayList.get(index).getPrice();
                                                    String offerPrice = dataModelArrayList.get(index).getOfferPrice();
                                                    String offerAvailable = dataModelArrayList.get(index).getOfferAvailable();
                                                    String thumbnail = dataModelArrayList.get(index).getThumbnail();
                                                    String productType = dataModelArrayList.get(index).getProductType();
                                                    FeaturedProductDataModel model = new FeaturedProductDataModel();
                                                    model.setId(inner_id);
                                                    model.setTitle(title);
                                                    model.setFavrt_bit("1");
                                                    model.setOfferAvailable(offerAvailable);
                                                    model.setOfferPrice(offerPrice);
                                                    model.setPrice(price);
                                                    model.setThumbnail(thumbnail);
                                                    model.setProductType(productType);
                                                    dataModelArrayList.set(index, model);


                                                }

                                            }
                                        }


                                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                        recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));
                                        progress.dismiss();

                                    }
                                    else
                                    {
                                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                        recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));
                                        progress.dismiss();

                                    }


                                }



                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            progress.dismiss();

                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ConsumerProductsList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        progress.dismiss();


                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });


            }
        });
    }


    public void getByInnerChaildCatId(String id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ProductbysubinnercatList> call = apiService.getBySubChildCategoryId(id);

        call.enqueue(new Callback<ProductbysubinnercatList>() {
            @Override
            public void onResponse(Call<ProductbysubinnercatList> call, retrofit2.Response<ProductbysubinnercatList> response) {
                final ProductbysubinnercatList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progress.dismiss();

                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList = new ArrayList<>(listofhome.getProducts());


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {



                                if(dataModelArrayList!=null && !dataModelArrayList.isEmpty()) {
                                    ////////////////////////////////////////////Set Data to  Recycler View///////////////////////////////////////////////////////////////

                                    if(dataModelArrayList_favrt!=null && !dataModelArrayList_favrt.isEmpty())
                                    {


                                        for (int index = 0; index < dataModelArrayList.size(); index++) {

                                            for (int index2 = 0; index2 < dataModelArrayList_favrt.size(); index2++) {

                                                String id = dataModelArrayList_favrt.get(index2).getId();
                                                if (dataModelArrayList.get(index).getId().equals(id)) {
                                                    String inner_id = dataModelArrayList.get(index).getId();
                                                    String title = dataModelArrayList.get(index).getTitle();
                                                    String price = dataModelArrayList.get(index).getPrice();
                                                    String offerPrice = dataModelArrayList.get(index).getOfferPrice();
                                                    String offerAvailable = dataModelArrayList.get(index).getOfferAvailable();
                                                    String thumbnail = dataModelArrayList.get(index).getThumbnail();
                                                    String productType = dataModelArrayList.get(index).getProductType();
                                                    FeaturedProductDataModel model = new FeaturedProductDataModel();
                                                    model.setId(inner_id);
                                                    model.setTitle(title);
                                                    model.setFavrt_bit("1");
                                                    model.setOfferAvailable(offerAvailable);
                                                    model.setOfferPrice(offerPrice);
                                                    model.setPrice(price);
                                                    model.setThumbnail(thumbnail);
                                                    model.setProductType(productType);
                                                    dataModelArrayList.set(index, model);


                                                }

                                            }
                                        }


                                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                        recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));
                                        progress.dismiss();

                                    }
                                    else
                                    {
                                        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                                        recyclerView.setAdapter(new FeaturedProductsRecyclerAdapter(SeeAllProductsActivity.this, dataModelArrayList, SeeAllProductsActivity.this));
                                        progress.dismiss();

                                    }


                                }


                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            progress.dismiss();

                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ProductbysubinnercatList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        progress.dismiss();


                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });


            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    ////////////////////////////////////////////////////Function that is used to get Consumer Favourite////////////////////
    public void getConsumerFavourite(String token) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Favourite_List> call = apiService.get_favrt_consumer(token);

        call.enqueue(new Callback<Favourite_List>() {
            @Override
            public void onResponse(Call<Favourite_List> call, retrofit2.Response<Favourite_List> response) {
                final Favourite_List listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if(status.contains("401"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Consumer Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
                                SharedPreferences.Editor editor2 = getApplicationContext().getSharedPreferences("favrtListhead", MODE_PRIVATE).edit();
                                editor2.clear(); //clear all stored data
                                editor2.apply();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                    }
                    else if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList_favrt = new ArrayList<>(listofhome.getFavourites());
                       runOnUiThread(new Runnable() {

                            @Override
                            public void run() {







                            }
                        });
                    }
                } else {

                  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Favourite_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////


               runOnUiThread(new Runnable() {

                    @Override
                    public void run() {




                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });



            }
        });
    }


}
