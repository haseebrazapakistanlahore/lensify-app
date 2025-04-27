package fyp.com.riona.ProductDetails;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.ArUtilPack.ARActivity;
import fyp.com.riona.CartProductRealmModel;
import fyp.com.riona.GlobalChecks;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.R;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailsActivity extends AppCompatActivity {
    EditText product_add_comment;
    RatingBar product_rating_bar, product_rating;
    ViewPager viewPager;
    ProductImagesSliderAdapter adapter;
    ImageView productImage;
    TextView product_detail_category_name, product_detail_offPrice, color_number, product_size, product_number_rating;
    TextView product_detail_title, product_detail_price, product_detail_description, custom_dialog_heading, custom_dialog_message;
    Button add_to_cart, product_submit_review, btn_continue_shopping;
    Spinner product_detail_color_spiner;
    LinearLayout colors_layout, color_number_layout, product_size_layout, product_rating_view, rate_server;
    WormDotsIndicator wormDotsIndicator;
    ProductColorsSpinnerAdapter productColorsSpinnerAdapter;
    ArrayList<ColorsModel> colorsModelArrayList, colorsModelArrayListfinal;
    ArrayList<ProductImagesModel> productImagesModelArrayList;
    ArrayList<ProductImagesModel> productImagesModelArrayListfinal;
    String Title;
    String Price;
    String Description;
    String ImageUrl;
    String Id;
    String availableQ;
    String categoryName;
    String categoryId;
    String priceForProfessional;
    String offPrice;
    String productSize;
    String productColorNo;
    String productType;
    String no_of_review;
    int rating;
    String sub_category_name;
    String sub_child_category_name;
    TextView tv_qyantity;
    Button increse_button, decrease_button;
    //String product_id;
    String color_id;
    String orderQuantity;
    long count;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    /////////////////////Local DataBase////////////////
    private Realm myRealm;
    SharedPreferences favouritesprefernce, cartprefernce;
    String islogin, user_type;
    String token, newtoken;
    int item_count = 1;
    GlobalChecks globalChecks;
    private static ProductDetailsActivity instance;
    ProductDetailModel d;
    String checkComment;
    SharedPreferences.Editor sharedPreferencesEditor;

    LinearLayout lltry;
    SharedPreferences cart_main_pref;
    SharedPreferences.Editor sharedPreferencesEditor_main_act;

    EditText et_left, et_right, et_size, et_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        et_left = findViewById(R.id.et_left);
        et_right = findViewById(R.id.et_right);
        et_size = findViewById(R.id.et_size);
        et_color = findViewById(R.id.et_color);
        viewPager = findViewById(R.id.product_images_viewpager);
        lltry = findViewById(R.id.tryon);
        productImage = findViewById(R.id.product_detail_image);
        product_detail_title = findViewById(R.id.product_detail_title);
        product_detail_price = findViewById(R.id.product_detail_price);
        product_detail_description = findViewById(R.id.product_detail_description);
        add_to_cart = findViewById(R.id.add_to_cart_button);
        product_add_comment = findViewById(R.id.product_add_comment);
        product_rating_bar = findViewById(R.id.product_add_ratting);
        product_submit_review = findViewById(R.id.product_submit_rating);
        product_rating = findViewById(R.id.product_rating);
        product_detail_color_spiner = findViewById(R.id.product_detail_color_spiner);
        product_detail_color_spiner.setVisibility(View.GONE);
        product_detail_category_name = findViewById(R.id.product_detail_category_name);
        wormDotsIndicator = findViewById(R.id.worm_dots_indicator);
        colors_layout = findViewById(R.id.colors_layout);
        color_number_layout = findViewById(R.id.color_number_layout);
        product_size_layout = findViewById(R.id.product_size_layout);
        color_number = findViewById(R.id.color_number);
        product_size = findViewById(R.id.product_size);
        product_detail_offPrice = findViewById(R.id.product_detail_offPrice);
        product_rating_view = findViewById(R.id.product_rating_view);
        rate_server = findViewById(R.id.rate_linear);
        btn_continue_shopping = findViewById(R.id.product_continue_shopping);

        /////////////////////////Qunatiyty Edit Text////////////////////
        tv_qyantity = findViewById(R.id.quantity_total);
        increse_button = findViewById(R.id.increase_button);
        decrease_button = findViewById(R.id.decrese_button);
        product_number_rating = findViewById(R.id.review_rating_number);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(ProductDetailsActivity.this, R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_productDetails);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        Id = intent.getStringExtra("product_id");
        progress = new ProgressDialog(ProductDetailsActivity.this);
        progress.setMessage("Loading..");
        progress.show();
        getProductDetailsId(Id);


        /////////////////////////////////////for local database/////////////////////

        myRealm = Realm.getDefaultInstance();
        instance = this;

        globalChecks = new GlobalChecks(ProductDetailsActivity.this);

        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        favouritesprefernce = getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = favouritesprefernce.getString("user_type", "");
        token = favouritesprefernce.getString("token", "");
        islogin = favouritesprefernce.getString("islogin", "");
        //////////////Review Sumbit///////////////
        product_submit_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkRatting = String.valueOf(product_rating_bar.getRating());
                checkComment = product_add_comment.getText().toString();
                if (islogin != null && !islogin.isEmpty()) {
                    if (user_type.contains("0")) {
                        newtoken = "bearer   " + token;
                        if (TextUtils.isEmpty(checkComment)) {
                            product_add_comment.setError("Enter Your message");

                        } else {
                            progress.show();
                            add_review_product_consumer(newtoken, Id, checkRatting, checkComment);
                        }


                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


            }
        });

        btn_continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ProductDetailsActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        product_detail_color_spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ColorsModel colorsModel = productColorsSpinnerAdapter.getItem(pos);
                String myid = colorsModel.getId();
                if (myid != null && !myid.isEmpty()) {
                    color_id = myid;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ////////////////////////////////Click Listener for Increse and Decrease Quantity////////////
        increse_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int b = 1;
                b += item_count;
                item_count = b;
                tv_qyantity.setText(String.valueOf(b));
            }
        });
        decrease_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (item_count == 1 || item_count == 0) {
                    int c = 1;
                    tv_qyantity.setText(String.valueOf(c));
                } else {
                    int b = 1;
                    int c = item_count - b;
                    item_count = c;
                    tv_qyantity.setText(String.valueOf(c));
                }

            }
        });

//////////////////////////////////////////////////Add to Cart ////////////////////////////////////////////////////////////
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String item = tv_qyantity.getText().toString();
                int myitem = Integer.valueOf(item);
                int total = 0;
                if (availableQ != null && !availableQ.isEmpty()) {
                    total = Integer.valueOf(availableQ);
                }


                if (myitem > total) {
                    Toast.makeText(getApplicationContext(), "Please Select Maximum Quantity " + total, Toast.LENGTH_LONG).show();

                } else if (colors_layout.getVisibility() == View.VISIBLE && color_id.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please Select Color First ", Toast.LENGTH_LONG).show();


                } else {


                    CartProductRealmModel object = myRealm.where(CartProductRealmModel.class)
                            .equalTo("id", Id)
                            .findFirst();
                    if (object == null) {

                        if (et_size.getText().toString().toString() != null && !et_size.getText().toString().isEmpty() && et_color.getText().toString().toString() != null && !et_color.getText().toString().isEmpty()) {
                            setupAddToCart();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Color/Size are compulsory", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                ProductDetailsActivity.this);
                        builder.setTitle("Add To  Cart");
                        builder.setMessage("Item Already Exists In Cart");
                        final AlertDialog closedialog = builder.create();

                        closedialog.show();

                        final Timer timer2 = new Timer();
                        timer2.schedule(new TimerTask() {
                            public void run() {
                                closedialog.dismiss();
                                timer2.cancel(); //this will cancel the timer of the system
                            }
                        }, 1000); // the timer will count 5 seconds....

                    }
                }

            }
        });


    }

    /////////////////Setup Data to Cart ////////////////////////
    public void setupAddToCart() {


        final String priceForCart;

        if (productType.equals("0")) {

            priceForCart = Price;
        } else {
            priceForCart = Price;

        }


        orderQuantity = tv_qyantity.getText().toString();

        myRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm obj) {

                CartProductRealmModel cartRealmModel = obj.createObject(CartProductRealmModel.class);
                cartRealmModel.setId(Id);
                cartRealmModel.setTitle(Title);
                cartRealmModel.setPrice(priceForCart);
                cartRealmModel.setDescription(Description);
                cartRealmModel.setThumbnail(String.valueOf(ImageUrl));
                cartRealmModel.setAvailable_quantity(availableQ);
                cartRealmModel.setColor_name(et_color.getText().toString());
                cartRealmModel.setProduct_quantity(orderQuantity);
                cartRealmModel.setOffer_price(offPrice);
                cartRealmModel.setProduct_type(productType);
                cartRealmModel.setLeft_engrave(et_left.getText().toString());
                cartRealmModel.setRight_engrave(et_right.getText().toString());
                cartRealmModel.setSize(et_size.getText().toString());
                if (offPrice.equals("0")) {
                    cartRealmModel.setProduct_total(String.valueOf(Float.valueOf(priceForCart) * Integer.parseInt(orderQuantity)));
                } else {
                    cartRealmModel.setProduct_total(String.valueOf(Float.valueOf(offPrice) * Integer.parseInt(orderQuantity)));

                }

            }
        }, new Realm.Transaction.OnSuccess() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess() {
                // Transaction was a success.


                cartCounting();
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ProductDetailsActivity.this);
                builder.setTitle("Add To  Cart");
                builder.setMessage("Item Added To Cart");
                final AlertDialog closedialog = builder.create();

                closedialog.show();

                final Timer timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    public void run() {
                        closedialog.dismiss();
                        timer2.cancel(); //this will cancel the timer of the system
                    }
                }, 1000); // the timer will count 5 seconds....

                //////////////////cart Value//////////////////////////

                globalChecks.realmCheck();

                long count = myRealm.where(CartProductRealmModel.class).count();

                //  ((MainActivity) Objects.requireNonNull(getApplicationContext())).addBadgeView(Integer.parseInt(String.valueOf(count)));
                cartprefernce = getSharedPreferences("cartitem", MODE_PRIVATE);
                sharedPreferencesEditor = cartprefernce.edit();
                sharedPreferencesEditor.putString(
                        "cartValue", String.valueOf(count));
                sharedPreferencesEditor.apply();


                ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView(Integer.parseInt(String.valueOf(count)));


            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.d("Error", "Error with adding to database" + error.getMessage());
            }
        });

    }


    public void cartCounting() {
        count = myRealm.where(CartProductRealmModel.class).count();

        (MainActivity.getInstance()).addBadgeView(Integer.parseInt(String.valueOf(count)));
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;

        } else if (item.getItemId() == R.id.main_menu_cart) {
            String data = "1";
            cart_main_pref = getApplicationContext().getSharedPreferences("details_data", MODE_PRIVATE);
            sharedPreferencesEditor_main_act = cart_main_pref.edit();
            sharedPreferencesEditor_main_act.putString("Cartfragment", data);
            sharedPreferencesEditor_main_act.apply();

            // Uncomment if needed
            // ((BottomNavigationView).findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.bottom_nav_cart);

            Intent intent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.main_menu_home) {
            Intent intent1 = new Intent(ProductDetailsActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
            return true;

        } else {
            return false; // Default case if no items match
        }
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getProductDetailsId(String id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ProductDetailsList> call = apiService.getProductDetailsbyId(id);

        call.enqueue(new Callback<ProductDetailsList>() {
            @Override
            public void onResponse(Call<ProductDetailsList> call, retrofit2.Response<ProductDetailsList> response) {
                final ProductDetailsList listofhome = response.body();

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

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////
                                colorsModelArrayList = new ArrayList<>();
                                colorsModelArrayListfinal = new ArrayList<>();
                                productImagesModelArrayList = new ArrayList<>();
                                productImagesModelArrayListfinal = new ArrayList<>();

                                d = listofhome.getProduct();
                                Title = d.getTitle();
                                Price = d.getPrice();
                                Description = d.getDescription();
                                ImageUrl = d.getThumbnail();
                                availableQ = d.getAvailableQuantity();
                                rating = Integer.parseInt(listofhome.getRating());
                                categoryName = d.getCategoryName();
                                sub_category_name = d.getSubCategoryName();
                                sub_child_category_name = d.getSubChildCategoryId();
                                categoryId = d.getCategoryId();
                                priceForProfessional = d.getPriceForProfessional();
                                productType = d.getProductType();
                                String checkOff = d.getOfferPrice();
                                availableQ = d.getAvailableQuantity();
                                no_of_review = listofhome.getNoOfReviews();
                                if (checkOff != null && !checkOff.isEmpty()) {
                                    offPrice = d.getOfferPrice();

                                } else {
                                    offPrice = "0";
                                }

                                productSize = d.getSize();
                                productColorNo = d.getColorNo();

                                colorsModelArrayList = new ArrayList<>(listofhome.getProduct().getColors());
                                productImagesModelArrayList = new ArrayList<>(listofhome.getProduct().getProductImages());


                                ColorsModel colorsDataModel1 = new ColorsModel();

                                colorsDataModel1.setId("0");
                                colorsDataModel1.setName("Select a color");
                                colorsModelArrayListfinal.add(colorsDataModel1);
                                ProductImagesModel dd = new ProductImagesModel();
                                dd.setId(Id);
                                dd.setImageUrl(ImageUrl);
                                productImagesModelArrayListfinal.add(dd);
                                for (int i = 0; i < colorsModelArrayList.size(); i++) {

                                    ColorsModel colorsDataModel = new ColorsModel();


                                    colorsDataModel.setId(colorsModelArrayList.get(i).getId());
                                    colorsDataModel.setName(colorsModelArrayList.get(i).getName());
                                    colorsDataModel.setImageUrl(colorsModelArrayList.get(i).getImageUrl());

                                    colorsModelArrayListfinal.add(colorsDataModel);

                                }

                                for (int i = 0; i < productImagesModelArrayList.size(); i++) {

                                    ProductImagesModel productsImageDataModel = new ProductImagesModel();
                                    productsImageDataModel.setId(productImagesModelArrayList.get(i).getId());
                                    productsImageDataModel.setImageUrl(productImagesModelArrayList.get(i).getImageUrl());
                                    productImagesModelArrayListfinal.add(productsImageDataModel);

                                }
                                setupData();
                                lltry.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(ProductDetailsActivity.this, ARActivity.class);
                                        intent.putExtra("color", "" + response.body().getProduct().getTitle());
                                        startActivity(intent);
                                    }
                                });
                                progress.dismiss();

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
            public void onFailure(Call<ProductDetailsList> call, Throwable t) {
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


    private void setupData() {


        product_detail_title.setText(Title);
        product_detail_price.setText("Rs" + " " + Price);
        product_detail_description.setText(Description);
        product_detail_category_name.setText(categoryName);


        if (productType.equals("0")) {

            if (offPrice.equals("0")) {

                product_detail_price.setText("Rs" + " " + Price);
                product_detail_offPrice.setVisibility(View.GONE);


            } else {

                product_detail_price.setText("Rs" + " " + offPrice);
                product_detail_offPrice.setVisibility(View.VISIBLE);
                product_detail_offPrice.setPaintFlags(product_detail_offPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                product_detail_offPrice.setText("Rs" + " " + Price);


            }

        } else {

            if (offPrice.equals("0")) {

                product_detail_price.setText("Rs" + " " + Price);
                product_detail_offPrice.setVisibility(View.GONE);


            } else {

                product_detail_price.setText("Rs" + " " + offPrice);
                product_detail_offPrice.setVisibility(View.VISIBLE);
                product_detail_offPrice.setPaintFlags(product_detail_offPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                product_detail_offPrice.setText("Rs" + " " + Price);


            }

        }


        if (productImagesModelArrayListfinal.isEmpty()) {
            Glide.with(ProductDetailsActivity.this)
                    .load(getApplicationContext().getResources().getString(R.string.url) + ImageUrl).placeholder(R.drawable.placeholder)

                    .dontAnimate()
                    .into(productImage);

            productImage.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            wormDotsIndicator.setVisibility(View.GONE);

        } else {
            productImage.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            adapter = new ProductImagesSliderAdapter(getApplicationContext(), productImagesModelArrayListfinal);
            viewPager.setAdapter(adapter);
            wormDotsIndicator.setViewPager(viewPager);


        }

        if (colorsModelArrayListfinal.size() == 1) {

            colors_layout.setVisibility(View.GONE);

        } else {
            colors_layout.setVisibility(View.GONE);
            productColorsSpinnerAdapter = new ProductColorsSpinnerAdapter(getApplicationContext(), colorsModelArrayListfinal);
            product_detail_color_spiner.setAdapter(productColorsSpinnerAdapter);


        }

        if (productColorNo != null && !productColorNo.isEmpty()) {
            color_number.setText(productColorNo);
            color_number_layout.setVisibility(View.VISIBLE);


        } else {

            color_number_layout.setVisibility(View.GONE);

        }


        if (productSize != null && !productSize.isEmpty()) {
            product_size.setText(productSize + " /ML");
            product_size_layout.setVisibility(View.VISIBLE);


        } else {

            product_size_layout.setVisibility(View.GONE);

        }


        ArrayList<String> stringArrayList = new ArrayList<String>();

        if (rating == 0) {
            rate_server.setVisibility(View.GONE);
        } else {
            rate_server.setVisibility(View.VISIBLE);
            product_rating.setRating(rating);
            product_number_rating.setText("Based on " + no_of_review + " review(s)");

        }
        for (int i = 1; i <= Integer.parseInt(availableQ); i++) {


            stringArrayList.add(String.valueOf(i));

        }
    }


    private void add_review_product_consumer(String token, String id, String rating, String comment) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ReviewSubmitList> call = apiService.add_review_product_consumer(token, id, rating, comment);

        call.enqueue(new Callback<ReviewSubmitList>() {
            @Override
            public void onResponse(Call<ReviewSubmitList> call, Response<ReviewSubmitList> response) {

                ReviewSubmitList listFoodModel = response.body();
                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    ////////////////////////////////If Getting Error From Server   ///////////////////////////////////////////////////////////////
                    if (status.contains("401")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences("signupdata", MODE_PRIVATE).edit();
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

                    } else if (status.contains("0")) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Review not Added!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                ////////////////////////////////////////////Set Data to  Recycler View Adapter ///////////////////////////////////////////////////////////////
                                Toast.makeText(getApplicationContext(), "Review Added!", Toast.LENGTH_LONG).show();


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
            public void onFailure(Call<ReviewSubmitList> call, Throwable t) {

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

}
