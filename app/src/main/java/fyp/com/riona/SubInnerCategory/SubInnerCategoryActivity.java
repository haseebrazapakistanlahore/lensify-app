package fyp.com.riona.SubInnerCategory;

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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Favourites.FavouriteDataModel;
import fyp.com.riona.Favourites.Favourite_List;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;

import fyp.com.riona.R;

import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.SubCategory.SabCatDataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SubInnerCategoryActivity extends AppCompatActivity {
    String sub_cat_id, sub_cat_name;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    ArrayList<SabCatDataModel> dataModelArrayList_circle;
    ArrayList<FeaturedProductDataModel> dataModelArrayList;
    private RecyclerView recyclerView, recyclerView_circle;
    TextView tv_title , tv_noData;
    LinearLayout linearLayout_sort_by_name, linearLayout_sort_by_price;
    int count_for_sort_by_name = 0;
    SharedPreferences favouritesprefernce,cat_preference;
    String token;
    String islogin,user_type ,newtoken;
    int i = 0;
    int j = 0;
    ImageButton list_view_change, grid_view_change;

    ////////////// List For Favourite Product Data Model ///////////////////////////////////

    ArrayList<FavouriteDataModel> dataModelArrayList_favrt = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_inner_category);
        //////////////Set Id's///////////////////////////////
        recyclerView_circle = findViewById(R.id.sub_categories_inner_circle_recyclerView);
        recyclerView = findViewById(R.id.sub_categories_inner_Products_recyclerView);
        linearLayout_sort_by_name = findViewById(R.id.pro_recycler_sort_button_name);
        linearLayout_sort_by_price = findViewById(R.id.pro_recycler_sort_button_price);
        list_view_change = findViewById(R.id.pro_recycler_list_view_button);
        tv_noData = findViewById(R.id.nodata_sub_inner);
        grid_view_change = findViewById(R.id.pro_recycler_grid_view_button);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading..");
        tv_title = findViewById(R.id.tv_toolbar_sub_inner_category);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        cat_preference = getSharedPreferences("cate_second_id", MODE_PRIVATE);
        sub_cat_id = cat_preference.getString("sub_category_id",null);
        sub_cat_name = cat_preference.getString("sub_category_name",null);


        favouritesprefernce = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = favouritesprefernce.getString("user_type", "");
        token = favouritesprefernce.getString("token", "");
        islogin = favouritesprefernce.getString("islogin", "");
        Toolbar toolbar = findViewById(R.id.toolbar_sub_inner_category);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tv_title.setText(sub_cat_name);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (sub_cat_id != null && !sub_cat_id.isEmpty()) {

            if (user_type.contains("0")) {
                if (token != null && !token.isEmpty()) {
                    newtoken = "bearer   " + token;
                    getConsumerFavourite(newtoken);
                    progress.show();
                    getBySubChaildCatId(sub_cat_id);

                } else {
                    progress.show();
                    getBySubChaildCatId(sub_cat_id);
                }
            }


            else
            {
                progress.show();
                getBySubChaildCatId(sub_cat_id);
            }


        }
        linearLayout_sort_by_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_for_sort_by_name == 0) {
                    sortData();
                    count_for_sort_by_name = 1;
                } else {
                    sortData2();
                    count_for_sort_by_name = 0;
                }
            }
        });
        linearLayout_sort_by_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////

                if (islogin != null && !islogin.isEmpty()) {

                    if (user_type.contains("0")) {
                        if (i == 0) {
                            orderbyconsumer(dataModelArrayList);
                            i = 1;
                            setuprecyclerViewAdapter();
                        } else {
                            reverseorderbyconsumer(dataModelArrayList);
                            i = 0;
                            setuprecyclerViewAdapter();
                        }

                    }
                }


            }
        });
        list_view_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setuprecyclerViewAdapter();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            }
        });
        grid_view_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupRecycler();
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

            }
        });
    }

    private static void orderbyconsumer(List<FeaturedProductDataModel> persons) {

        Collections.sort(persons, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {

                String x1 = ((FeaturedProductDataModel) o1).getPrice();
                String x2 = ((FeaturedProductDataModel) o2).getPrice();
                int sComp = Integer.valueOf(x1).compareTo(Integer.valueOf(x2));
                return sComp;

            }
        });
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


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortData() {
        Collections.sort(dataModelArrayList, new Comparator<FeaturedProductDataModel>() {
            @Override
            public int compare(FeaturedProductDataModel lhs, FeaturedProductDataModel rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

        setuprecyclerViewAdapter();

    }

    private void sortData2() {
        Collections.sort(dataModelArrayList, new Comparator<FeaturedProductDataModel>() {

            @Override
            public int compare(FeaturedProductDataModel o1, FeaturedProductDataModel o2) {

                return -o1.getTitle().compareTo(o2.getTitle());

            }
        });

        setuprecyclerViewAdapter();

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getBySubChaildCatId(String id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<SubInnerList> call = apiService.getBySubCatId(id);

        call.enqueue(new Callback<SubInnerList>() {
            @Override
            public void onResponse(Call<SubInnerList> call, retrofit2.Response<SubInnerList> response) {
                final SubInnerList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });
                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList = new ArrayList<>(listofhome.getProducts());
                        dataModelArrayList_circle = new ArrayList<SabCatDataModel>(listofhome.getSubCategory().getSub_child_categories());


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {



                                    if (dataModelArrayList_favrt != null && !dataModelArrayList_favrt.isEmpty()) {


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

                                        progress.dismiss();
                                        setupRecycler();
                                        setupRecycler_Circle();
                                    }


                                    else
                                    {
                                        progress.dismiss();
                                        setupRecycler();
                                        setupRecycler_Circle();
                                    }




                                } else {

                                    // linearLayout.setVisibility(View.GONE);
                                    mynewsetupRecycler();

                                    tv_noData.setVisibility(View.VISIBLE);
                                }

                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                            }
                        });
                    }
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<SubInnerList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
//                progress.dismiss();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }

                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });
            }


        });

    }


    private void setuprecyclerViewAdapter() {

        SubInnerCategoryProductsRecyclerAdapter adapter = new SubInnerCategoryProductsRecyclerAdapter(SubInnerCategoryActivity.this, dataModelArrayList, SubInnerCategoryActivity.this);
        recyclerView.setAdapter(adapter);


    }

    private void mysetuprecyclerViewAdapter() {

        SubInnerCircleCategoryAdapter adapter = new SubInnerCircleCategoryAdapter(SubInnerCategoryActivity.this, dataModelArrayList_circle);
        recyclerView.setAdapter(adapter);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


    }

    private void setupRecycler() {

        setuprecyclerViewAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));


    }

    private void mynewsetupRecycler() {

        mysetuprecyclerViewAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));


    }

    private void setupRecycler_Circle() {
        int demosize = dataModelArrayList_circle.size();
        if (demosize <= 4) {
//            linearLayout.setOrientation(LinearLayout.VERTICAL);
//            back_arrow.setVisibility(View.GONE);
//            farwd_arrow.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.CENTER_HORIZONTAL;
            recyclerView_circle.setLayoutParams(params);
            if (dataModelArrayList_circle != null) {
                SubInnerCircleCategoryAdapter adapter = new SubInnerCircleCategoryAdapter(getApplicationContext(), dataModelArrayList_circle);
                recyclerView_circle.setAdapter(adapter);
            }

            recyclerView_circle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


        } else {
            SubInnerCircleCategoryAdapter adapter = new SubInnerCircleCategoryAdapter(getApplicationContext(), dataModelArrayList_circle);
            recyclerView_circle.setAdapter(adapter);
            recyclerView_circle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        }

//        home_categories_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                int currentLastVisible = ((LinearLayoutManager) horizontalLayout).findLastVisibleItemPosition();
//
//                recyclerView.smoothScrollToPosition(currentLastVisible + 2);
//
//            }
//        });
//
//        back_Arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentFirstVisible = ((LinearLayoutManager) horizontalLayout).findFirstVisibleItemPosition();
//                recyclerView.smoothScrollToPosition(currentFirstVisible - 2);
//
//            }
//        });
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
