package fyp.com.riona.HomeFragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Favourites.FavouriteDataModel;
import fyp.com.riona.Favourites.Favourite_List;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductsFragment;

import fyp.com.riona.R;

import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.SeeAllProducts.SeeAllProductsActivity;
import fyp.com.riona.TopSellingFragment.TopSellingFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;
public class HomeFragment extends Fragment {

    ViewPager slider_viewpager, tab_viewPager;
    SliderAdapter sliderAdapter;
    TabLayout tabLayout;
    private RecyclerView recyclerView;
    WormDotsIndicator wormDotsIndicator;
    private int currentPage = 0; // this will tell us the current page available on the view pager
    ArrayList<BannerDataModel> homeSliderModels = new ArrayList<BannerDataModel>();
    private static final long SLIDER_TIMER = 2000; // change slider interval
    private boolean isCountDownTimerActive = false; // let the timer start if and only if it has completed previous task
    private Handler handler;
    ArrayList<CategoryDataModel> dataModelArrayList;
    ArrayList<FeaturedProductDataModel> featuredProductsDataModelArrayList;
    ArrayList<FeaturedProductDataModel> topSellingProductDataModelArrayList;
    HomeFragmentCategoriesRecyclerAdapter adapter;
    SharedPreferences user_preference, featuretopprefernce;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    String user_type;
    String token,newtoken;
    Activity mycureent_activity;
    ////////////// List For Favourite Product Data Model ///////////////////////////////////

    ArrayList<FavouriteDataModel> dataModelArrayList_favrt = new ArrayList<>();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!isCountDownTimerActive) {
                automateSlider();
            }

            handler.postDelayed(runnable, 1000);
            // our runnable should keep running for every 1000 milliseconds (1 seconds)
        }
    };
    private Button bt_seeAll;


    SharedPreferences category_pref;
    SharedPreferences.Editor sharedprefernce_cat_editor ;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.categories_recyclerView_homePage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        tab_viewPager = view.findViewById(R.id.home_TabViewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.home_tabs);
        slider_viewpager = (ViewPager) view.findViewById(R.id.slider_viewpager_fragment_home);
        wormDotsIndicator = (WormDotsIndicator) view.findViewById(R.id.worm_dots_indicator);
        bt_seeAll = view.findViewById(R.id.see_all_button_for_consumer);
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading..");
        setHasOptionsMenu(true);

        mycureent_activity= MainActivity.getInstance();
        slider_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentPage = 0;
                } else if (position == 1) {
                    currentPage = 1;
                } else if (position == 2) {
                    currentPage = 2;
                } else {
                    currentPage = 3;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_seeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeeAllProductsActivity.class);
                category_pref = mycureent_activity.getSharedPreferences("cate_third_id", MODE_PRIVATE);
                sharedprefernce_cat_editor = category_pref.edit();
                sharedprefernce_cat_editor.putString("consumer_id","1");
                sharedprefernce_cat_editor.apply();
                startActivity(intent);
            }
        });

        handler = new Handler();
        handler.postDelayed(runnable, 1000);
        runnable.run();

        if (getActivity() != null) {
            user_preference = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);

            user_type = user_preference.getString("user_type", "");
            token = user_preference.getString("token","");
            if (user_type != null && !user_type.isEmpty()) {
                if (user_type.contains("0")) {
                    if(token!=null && !token.isEmpty())
                    {
                        newtoken = "bearer   " + token;
                        getConsumerFavourite(newtoken);
                        progress.show();
                        getHomeData();

                    }
                    else
                    {
                        Toast.makeText(mycureent_activity, "Login First", Toast.LENGTH_LONG).show();
                        //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = mycureent_activity.getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                        editor.clear(); //clear all stored data
                        editor.apply();
                        Intent intent = new Intent(mycureent_activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
                 else {
                    progress.show();
                    getHomeData();
                }
            } else {
                progress.show();
                getHomeData();
            }
        }

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        HomeProductsVPAdapter adapter = new HomeProductsVPAdapter(getChildFragmentManager());
        adapter.AddFragment(new FeaturedProductsFragment(), "Featured");
        adapter.AddFragment(new TopSellingFragment(), "Top Selling");
        viewPager.setAdapter(adapter);
    }


    private void automateSlider() {
        isCountDownTimerActive = true;
        new CountDownTimer(SLIDER_TIMER, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                int nextSlider = currentPage + 1;
                int size = homeSliderModels.size();
                ////////////This is used for get the length of slider array list/////////////
                if (size == 0) {


                    size = 3;
                } else {


                    if (nextSlider == size) {
                        nextSlider = 0; // if it's last Image, let it go to the first image
                    }

                    slider_viewpager.setCurrentItem(nextSlider);
                    isCountDownTimerActive = false;
                }
            }
        }.start();
    }


    public class HomeProductsVPAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentListTitles = new ArrayList<>();


        public HomeProductsVPAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentListTitles.size();
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentListTitles.get(position);
        }

        public void AddFragment(Fragment fragment, String Titles) {

            fragmentList.add(fragment);
            fragmentListTitles.add(Titles);

        }
    }


    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getHomeData() {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumerHomeList> call = apiService.getHomeDataforConsumer();

        call.enqueue(new Callback<ConsumerHomeList>() {
            @Override
            public void onResponse(Call<ConsumerHomeList> call, retrofit2.Response<ConsumerHomeList> response) {
                final ConsumerHomeList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                                }
                            });
                        }
                    } else {
                        progress.dismiss();
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////
                        homeSliderModels = new ArrayList<>(listofhome.getBanners());
                        dataModelArrayList = new ArrayList<>(listofhome.getCategories());
                        featuredProductsDataModelArrayList = new ArrayList<>(listofhome.getFeaturedProducts());
                        topSellingProductDataModelArrayList = new ArrayList<>(listofhome.getTopSellingProducts());
/////////////////////////////////////////////////////Send data to Featured and Top Sellling Product///////////////////////////////////////////


                        if (getActivity() == null) {
                            return;
                        } else {
                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    progress.dismiss();
                                    if (getActivity() != null) {
                                        featuretopprefernce = getActivity().getSharedPreferences("homelist", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor prefsEditor = featuretopprefernce.edit();
                                        //Set the values
                                        Gson gson = new Gson();
                                        String listofbusiness = gson.toJson(featuredProductsDataModelArrayList);
                                        prefsEditor.putString("featuredProductsDataModelArrayList", listofbusiness);
                                        String sports = gson.toJson(topSellingProductDataModelArrayList);
                                        prefsEditor.putString("topSellingProductDataModelArrayList", sports);
                                        prefsEditor.apply();
                                    }
                                    setupRecycler();
                                    if (tab_viewPager != null) {
                                        setupViewPager(tab_viewPager);
                                    }
                                    tabLayout.setupWithViewPager(tab_viewPager);
                                    sliderAdapter = new SliderAdapter(getContext(), homeSliderModels);
                                    slider_viewpager.setAdapter(sliderAdapter);
                                    wormDotsIndicator.setViewPager(slider_viewpager);


                                }
                            });
                        }
                    }
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ConsumerHomeList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            progress.dismiss();
                            Toast.makeText(getActivity(), "Please Check Your Internet Connection.",
                                    Toast.LENGTH_LONG).show();
                            //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                        }

                    });
                }

            }
        });
    }

    private void setupRecycler() {

        adapter = new HomeFragmentCategoriesRecyclerAdapter(getContext(), dataModelArrayList);
        recyclerView.setAdapter(adapter);

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
                        mycureent_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mycureent_activity, "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = mycureent_activity.getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
                                SharedPreferences.Editor editor2 = mycureent_activity.getSharedPreferences("favrtListhead", MODE_PRIVATE).edit();
                                editor2.clear(); //clear all stored data
                                editor2.apply();
                                Intent intent = new Intent(mycureent_activity, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mycureent_activity.startActivity(intent);
                            }
                        });

                    }
                    else if (status.contains("0")) {

                        mycureent_activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                Snackbar.make(mycureent_activity.findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList_favrt = new ArrayList<>(listofhome.getFavourites());
                        mycureent_activity.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if (dataModelArrayList_favrt != null && !dataModelArrayList_favrt.isEmpty()) {



                                    SharedPreferences sharedPreferences = mycureent_activity.getSharedPreferences("favrtListhead", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();

                                    String dealsList = gson.toJson(dataModelArrayList_favrt);
                                    editor.putString("favrtList", dealsList);
                                    editor.apply();
                                }





                            }
                        });
                    }
                } else {

                    mycureent_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            Toast.makeText(mycureent_activity, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Favourite_List> call, Throwable t) {
                /////Progress Dialog Dismiss  /////


                mycureent_activity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {




                        Toast.makeText(mycureent_activity, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });



            }
        });
    }




}
