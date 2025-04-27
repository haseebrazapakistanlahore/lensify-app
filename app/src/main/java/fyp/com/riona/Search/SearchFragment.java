package fyp.com.riona.Search;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Favourites.FavouriteDataModel;
import fyp.com.riona.Favourites.Favourite_List;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;

import fyp.com.riona.R;

import fyp.com.riona.SeeAllProducts.ForConsumer.ConsumerProductsList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<FeaturedProductDataModel> dataModelArrayList;
    LinearLayout linearLayout_sort_by_name, linearLayout_sort_by_price;
    SharedPreferences favouritesprefernce;
    String islogin, user_type;
    String token,newtoken;
    SearchProductsRecyclerAdapter adapter;
    View view;
    ProgressDialog progress;

    Activity act ;

    ////////////// List For Favourite Product Data Model ///////////////////////////////////

    ArrayList<FavouriteDataModel> dataModelArrayList_favrt = new ArrayList<>();
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_search, container, false);
        recyclerView = view.findViewById(R.id.allProducts_recyclerView_search);
        setHasOptionsMenu(true);
        linearLayout_sort_by_name = view.findViewById(R.id.pro_recycler_sort_button_name);
        linearLayout_sort_by_price = view.findViewById(R.id.pro_recycler_sort_button_price);
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading...");

        act = MainActivity.getInstance();
        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        favouritesprefernce =act.getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = favouritesprefernce.getString("user_type", "");
        islogin = favouritesprefernce.getString("islogin", "");
        token = favouritesprefernce.getString("token", "");
        progress.show();
        if (islogin != null && !islogin.isEmpty()) {
            if (user_type.contains("0")) {

                if(token!=null && !token.isEmpty())
                {
                    newtoken = "bearer   " + token;
                    getConsumerFavourite(newtoken);
                    SearchConsumerAllProducts();

                }
                else
                {
                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                    SharedPreferences.Editor editor = act.getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                    editor.clear(); //clear all stored data
                    editor.apply();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        } else {

            SearchConsumerAllProducts();
        }
        return view;
    }

    public void SearchConsumerAllProducts() {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumerProductsList> call = apiService.getConsumerProducts();

        call.enqueue(new Callback<ConsumerProductsList>() {
            @Override
            public void onResponse(Call<ConsumerProductsList> call, retrofit2.Response<ConsumerProductsList> response) {
                final ConsumerProductsList listofhome = response.body();
                progress.dismiss();
                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {

                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Snackbar.make(act.findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList = new ArrayList<>(listofhome.getProducts());


                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

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


                                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                    adapter = new SearchProductsRecyclerAdapter(getActivity(), dataModelArrayList, MainActivity.getInstance());
                                    recyclerView.setAdapter(adapter);

                                }
                                else {
                                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                    adapter = new SearchProductsRecyclerAdapter(getActivity(), dataModelArrayList, MainActivity.getInstance());
                                    recyclerView.setAdapter(adapter);
                                }

                            }
                        });
                    }
                } else {

                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ConsumerProductsList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                act.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        Toast.makeText(act, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });


            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //  menuInflater.inflate(R.menu.main2, menu);
        super.onCreateOptionsMenu(menu, menuInflater);


        MenuItem searchItem = menu.findItem(R.id.search_button);
        MenuItem proButton = menu.findItem(R.id.pro_toolbar_button);
        searchItem.expandActionView();

        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);

                return false;
            }
        });



        proButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (getActivity() != null) {
                    Intent refresh = new Intent(getContext(), MainActivity.class);
                    startActivity(refresh);
                    getActivity().finish();

                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                return true;
            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.pro_toolbar_button).setVisible(false);
        menu.findItem(R.id.search_button).setVisible(false);
        super.onPrepareOptionsMenu(menu);
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
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = act.getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });

                    }
                    else if (status.contains("0")) {

                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                Snackbar.make(act.findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList_favrt = new ArrayList<>(listofhome.getFavourites());
                        act.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

//                                if (dataModelArrayList_favrt != null && !dataModelArrayList_favrt.isEmpty()) {
//
//
//
//                                    SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("favrtListhead", MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    Gson gson = new Gson();
//
//                                    String dealsList = gson.toJson(dataModelArrayList_favrt);
//                                    editor.putString("favrtList", dealsList);
//                                    editor.apply();
//                                }





                            }
                        });
                    }
                } else {

                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            Toast.makeText( getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Favourite_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////


                act.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {




                        Toast.makeText(act, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });



            }
        });
    }


}