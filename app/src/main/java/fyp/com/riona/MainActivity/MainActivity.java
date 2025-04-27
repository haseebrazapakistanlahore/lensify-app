package fyp.com.riona.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.CartFragment.CartFragment;
import fyp.com.riona.CategoriesFragment.CategoriesFragment;
import fyp.com.riona.Favourites.FavouriteDataModel;
import fyp.com.riona.Favourites.FavouritesActivity;
import fyp.com.riona.FriendlyDB;
import fyp.com.riona.HomeFragment.HomeFragment;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MyOrders.MyOrdersActivity;
import fyp.com.riona.Notification.NotificationFragment;
import fyp.com.riona.ProfileFragment.ProfileFragment;
import fyp.com.riona.R;
import fyp.com.riona.SaleFragment.SaleFragment;
import fyp.com.riona.Search.SearchFragment;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.e("Expressin", ""+R.id.bottom_nav_home);
            Fragment fragment = null;
            int itemId = item.getItemId(); // Store the itemId in a variable to use in switch case
            if (itemId == R.id.bottom_nav_home) {
                // Handle bottom_nav_home case
                fragment = new HomeFragment();
            } else if (itemId == R.id.bottom_nav_sale) {
                // Handle bottom_nav_sale case
                fragment = new SaleFragment();
            } else if (itemId == R.id.bottom_nav_categories) {
                // Handle bottom_nav_categories case
                fragment = new CategoriesFragment();
            } else if (itemId == R.id.navigation_notifications) {
                // Handle navigation_notifications case
                fragment = new NotificationFragment();
            } else if (itemId == R.id.bottom_nav_cart) {
                // Handle bottom_nav_cart case
                CartFragment cartFragment = new CartFragment();
                FragmentTransaction fragmentTransactionPro = getSupportFragmentManager().beginTransaction();
                fragmentTransactionPro.replace(R.id.fragment_container, cartFragment, "Fragment_Cart");
                fragmentTransactionPro.commit();
            }

            return loadFragment(fragment);
        }
    };


    SharedPreferences user_preference  ;
    SharedPreferences.Editor user_preferenceEditor, home_preferenceEditor;
    String user_name, user_img, user_email, user_type, islogin;
    TextView tv_user_name, tv_user_email;
    NavigationView navigationView;
    Button logout_btn_visiable;
    RelativeLayout layout_for_login_button;
    Button login_button, login_button_professional;
    private static int sub_menu = 0;
    ArrayList<PagesDataModel> getPagelModelArrayList;
    PagesDataModel d;
    ImageView user_image_view;
    QBadgeView qBadgeView, qBadgeView2;
    private static MainActivity instance;
    private boolean isChecked = false;
    Menu mymenu;
    LinearLayout linearLayoutfortoolbar;
    private FrameLayout frameLayout;
    ImageView professional_img;
    public Activity activity = MainActivity.this;
    private static final String NOTIFICATION = "notification";
    SharedPreferences notification_pref;
    SharedPreferences.Editor sharedPreferencesEditor;

    SharedPreferences cart_product_pref, search_product_pref;
    SharedPreferences.Editor sharedPreferencesEditor_cart_product, sharedPreferencesEditor_search_product;

    FriendlyDB db;

////////////// List For Favourite Product Data Model ///////////////////////////////////

    ArrayList<FavouriteDataModel> dataModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onNewIntent(getIntent());
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        navigation = findViewById(R.id.bottom_nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        Realm.init(MainActivity.this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);


        loadFragment(new HomeFragment());
        instance = this;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View hView = navigationView.getHeaderView(0);
        tv_user_name = (TextView) hView.findViewById(R.id.client_name);
        tv_user_email = (TextView) hView.findViewById(R.id.client_email);
        user_image_view = (ImageView) hView.findViewById(R.id.imageView_main);
        logout_btn_visiable = (Button) findViewById(R.id.nav_logout);
        layout_for_login_button = (RelativeLayout) findViewById(R.id.login_button_layout);
        login_button_professional = (Button) findViewById(R.id.professional_login);
        login_button = (Button) findViewById(R.id.user_main_screen_login);
        frameLayout = findViewById(R.id.fragment_container);

        qBadgeView = new QBadgeView(this);
        qBadgeView2 = new QBadgeView(this);
        db = new FriendlyDB(MainActivity.this);


        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        user_preference = getSharedPreferences("signupdata", MODE_PRIVATE);

        user_type = user_preference.getString("user_type", "");
        islogin = user_preference.getString("islogin", "");
        if (islogin != null && !islogin.isEmpty()) {

            if (user_type != null && !user_type.isEmpty()) {
                user_name = user_preference.getString("name", "");
                user_email = user_preference.getString("email", "");
                user_img = user_preference.getString("url", "");

                if (user_name != null && !user_name.isEmpty()) {
                    tv_user_name.setText(user_name);
                }
                if (user_email != null && !user_email.isEmpty()) {
                    tv_user_email.setText(user_email);
                }
                if (user_img != null && !user_img.isEmpty()) {
                    Glide.with(MainActivity.this)
                            .load(getResources().getString(R.string.url) + user_img).placeholder(R.drawable.placeholder)

                            .dontAnimate()
                            .into(user_image_view);
                }


            }
            logout_btn_visiable.setVisibility(View.VISIBLE);
            layout_for_login_button.setVisibility(View.GONE);
        }

        user_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        logout_btn_visiable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logout_btn_visiable.getVisibility() == View.VISIBLE) {
                    logout();

                }
            }
        });

        login_button_professional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        Intent intent = getIntent();

        if (intent.hasExtra("goto")) {
            String goTo = intent.getStringExtra("goto");

            switch (goTo) {

//                case "cart":
//                    CartFragment fragment = new CartFragment();
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment, "FragmentCart");
//                    fragmentTransaction.commit();
//                    break;



                default:
                    if (user_type != null && !user_type.isEmpty()) {
                        if (user_type.equals("0")) {
                            navigation.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
                            HomeFragment fragmentHome = new HomeFragment();
                            FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                            fragmentTransactionHome.replace(R.id.fragment_container, fragmentHome, "FragmentHome");
                            fragmentTransactionHome.commit();
                        }
                    }

                    break;
            }

        } else {

            if (user_type != null && !user_type.isEmpty()) {
                if (user_type.equals("0")) {
                    navigation.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
                    HomeFragment fragmentHome = new HomeFragment();
                    FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionHome.replace(R.id.fragment_container, fragmentHome, "FragmentHome");
                    fragmentTransactionHome.commit();
                }
            }


        }


        navigationView.setNavigationItemSelectedListener(this);

        cart_product_pref = getApplicationContext().getSharedPreferences("details_data", MODE_PRIVATE);
        String data = cart_product_pref.getString("Cartfragment", null);


        search_product_pref = getApplicationContext().getSharedPreferences("search_data", MODE_PRIVATE);
        String search = search_product_pref.getString("Searchfragment", null);

        if (data != null && data.contains("1")) {
            navigation.getMenu().findItem(R.id.bottom_nav_cart).setChecked(true);
            cart_product_pref = getApplicationContext().getSharedPreferences("details_data", MODE_PRIVATE);
            sharedPreferencesEditor_cart_product = cart_product_pref.edit();
            sharedPreferencesEditor_cart_product.clear();
            sharedPreferencesEditor_cart_product.apply();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new CartFragment());
            fragmentTransaction.commitNow();


            navigation.setSelectedItemId(R.id.bottom_nav_cart);

        }

        if (search != null && search.contains("1")) {

            search_product_pref = getApplicationContext().getSharedPreferences("search_data", MODE_PRIVATE);
            sharedPreferencesEditor_search_product = search_product_pref.edit();
            sharedPreferencesEditor_search_product.clear();
            sharedPreferencesEditor_search_product.apply();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new SearchFragment());
            fragmentTransaction.commitNow();


        }

//        getAllPages();
        userLoginStatus();


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                activity.findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_home);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final boolean fromNotification = extras.getBoolean(NOTIFICATION);
            if (fromNotification) {
//                Fragment fragment = null;
//                fragment = new NotificationFragment();
//                loadFragment(fragment);

                BottomNavigationView bottomNavigationView = (BottomNavigationView)
                        activity.findViewById(R.id.bottom_nav_view);
                bottomNavigationView.setSelectedItemId(R.id.navigation_notifications);

            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.bottom_nav_home != seletedItemId) {
            setHomeItem(MainActivity.this);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

        }
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (isChecked == false) {
            getMenuInflater().inflate(R.menu.main2, menu);

            mymenu = menu;
            MenuItem proButton = menu.findItem(R.id.pro_toolbar_button);
            super.onCreateOptionsMenu(menu);

            proButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    isChecked = true;

                    return false;
                }
            });

        } else if (isChecked == true) {
            getMenuInflater().inflate(R.menu.main3, menu);
            mymenu = menu;
            MenuItem proButton = menu.findItem(R.id.pro_toolbar_button);
            super.onCreateOptionsMenu(menu);

            proButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    isChecked = false;


                    return false;
                }
            });
        }

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search_button) {

            SearchFragment searchFragment = new SearchFragment();
            FragmentTransaction fragmentTransactionSearch = getSupportFragmentManager().beginTransaction();
            fragmentTransactionSearch.replace(R.id.fragment_container, searchFragment, "Fragment_Search");
            fragmentTransactionSearch.addToBackStack(null);
            fragmentTransactionSearch.commit();

            return true;

        } else if (id == R.id.pro_toolbar_button) {

            showHomeFragment();
            isChecked = false;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem checkable = menu.findItem(R.id.pro_toolbar_button);
        linearLayoutfortoolbar = (LinearLayout) checkable.getActionView();
        professional_img = (ImageView) linearLayoutfortoolbar.findViewById(R.id.toolbar_title_professional);
        super.onPrepareOptionsMenu(menu);
        linearLayoutfortoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(checkable);
            }
        });
        checkable.setChecked(isChecked);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_categories) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
            bottomNavigationView.setSelectedItemId(R.id.bottom_nav_categories);

        } else if (id == R.id.nav_all_favrt) {
            startActivity(new Intent(getApplicationContext(), FavouritesActivity.class));

        } else if (id == R.id.nav_my_info) {
            Fragment fragment2 = null;
            if (user_type != null && !user_type.isEmpty()) {

                fragment2 = new ProfileFragment();
                loadFragment(fragment2);


            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

        } else if (id == R.id.nav_my_orders) {

            startActivity(new Intent(getApplicationContext(), MyOrdersActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void userLoginStatus() {

        Menu menuNav = navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.nav_my_orders);
        MenuItem nav_item3 = menuNav.findItem(R.id.nav_my_info);
        MenuItem nav_item4 = menuNav.findItem(R.id.nav_all_favrt);
        if (islogin != null && !islogin.isEmpty()) {
            if (islogin.contains("1")) {
                nav_item2.setEnabled(true);
                nav_item2.setVisible(true);
                nav_item3.setEnabled(true);
                nav_item4.setEnabled(true);
                nav_item3.setVisible(true);
                nav_item4.setVisible(true);
            } else {
                nav_item2.setEnabled(false);
                nav_item3.setEnabled(false);
                nav_item3.setVisible(false);
                nav_item4.setEnabled(false);
                nav_item4.setVisible(false);
                nav_item2.setVisible(false);
            }
        } else {
            nav_item2.setEnabled(false);
            nav_item3.setEnabled(false);
            nav_item3.setVisible(false);
            nav_item4.setEnabled(false);
            nav_item4.setVisible(false);
            nav_item2.setVisible(false);
        }


    }



    private void showHomeFragment() {
        navigation.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransactionProfessional = getSupportFragmentManager().beginTransaction();
        fragmentTransactionProfessional.replace(R.id.fragment_container, homeFragment);
        fragmentTransactionProfessional.commit();


    }

    public void logout() {
        user_preference = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE);

        user_preferenceEditor = user_preference.edit();
        user_preferenceEditor.clear();
        user_preferenceEditor.apply();
        clearnotification();
        db.deleteTableF();
        //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
        SharedPreferences.Editor editor = MainActivity.this.getSharedPreferences("favrtListhead", MODE_PRIVATE).edit();
        editor.clear(); //clear all stored data
        editor.apply();
//        home_preference =  getApplicationContext().getSharedPreferences("homelist",MODE_PRIVATE);
//        home_preferenceEditor = home_preference.edit();
//        home_preferenceEditor.clear();
//        home_preferenceEditor.apply();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    public void clearnotification() {


        notification_pref = getSharedPreferences("notificationitem", MODE_PRIVATE);
        sharedPreferencesEditor = notification_pref.edit();
        sharedPreferencesEditor.putString(
                "notificationValue", "0");
        sharedPreferencesEditor.apply();
        ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView(Integer.parseInt("0"));


    }

    /////////////////////////This Funcation is used for drwaer cross icon //////////////////////////
    public void closeDrawer(View view) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getAllPages() {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<PagesList> call = apiService.getCustomPages();

        call.enqueue(new Callback<PagesList>() {
            @Override
            public void onResponse(Call<PagesList> call, retrofit2.Response<PagesList> response) {
                final PagesList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });
                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        getPagelModelArrayList = new ArrayList<>(listofhome.getPages());
                        d = new PagesDataModel();
                        d.setId(" ");
                        d.setTitle(" ");
                        getPagelModelArrayList.add(d);

                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

//                            addPages();
                        }
                    });

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
            public void onFailure(Call<PagesList> call, Throwable t) {
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


    public void addPages() {

        Menu menuNav = navigationView.getMenu();
        final SubMenu subMenu = menuNav.addSubMenu("");

        for (int i = 0; i < getPagelModelArrayList.size(); i++) {
            sub_menu = i;

            subMenu.add(Menu.NONE, sub_menu, Menu.NONE, String.valueOf(getPagelModelArrayList.get(i).getTitle()));

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addBadgeView(int cart_count) {


        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3); // number of menu from left
        qBadgeView.bindTarget(v).setBadgeGravity(Gravity.END | Gravity.TOP).setGravityOffset(18, 8, true).setBadgeNumber(cart_count);

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void addBadgeView_notification(int cart_count) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) navigation.getChildAt(0);
                View v = bottomNavigationMenuView.getChildAt(4); // number of menu from left
                qBadgeView2.bindTarget(v).setBadgeGravity(Gravity.END | Gravity.TOP).setGravityOffset(18, 8, true).setBadgeNumber(cart_count);

            }
        });


    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onStart() {
        super.onStart();
        int noo = 0;
        int count = 0;


        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("cartitem", MODE_PRIVATE);
        String val_check = Objects.requireNonNull(sharedPreferences2.getString("cartValue", ""));
        SharedPreferences sharedPreferences_notification = getApplicationContext().getSharedPreferences("notificationitem", MODE_PRIVATE);
        String val_count = Objects.requireNonNull(sharedPreferences_notification.getString("notificationValue", ""));

        if (!val_check.isEmpty()) {
            noo = Integer.parseInt(val_check);
        }

        addBadgeView(noo);
        Log.e("CArt Data3 = ", String.valueOf(noo));
        if (!val_count.isEmpty()) {
            count = Integer.parseInt(val_count);
        }
        addBadgeView_notification(count);
        Log.e("notification Data = ", String.valueOf(count));


    }

    public void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            MainActivity.this.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Sorry, Not able to open!", Toast.LENGTH_SHORT).show();
        }
    }


}
