package fyp.com.riona.CartPayment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.CartProductRealmModel;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.R;
import fyp.com.riona.UserAddress;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartPaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner payment_method, shipping_address_sp;
    ImageButton custom_dialog_close, address_dialog_close, add_billing_address_button;
    Button add_more_addresses_button;
    ImageView custom_dialog_icon;
    PaymentSpinnerAdapter paymentSpinnerAdapter;
    Button proceed, place_order_button;
    ImageButton checkout_back_button;
    TextView grand_total;
    LinearLayout off_Layout, net_layout;
    TextView offprice, net_pricemy;
    TextView profile_name, profile_email, profile_number, profile_username;
    ArrayList<UserAddress> addressesModelArraylist;
    String selecedPaymentMehod;
    RealmResults<CartProductRealmModel> cartProductsRealm;
    Realm realm = Realm.getDefaultInstance();
    int selected_billing_address = 0;
    int selected_shipping_addressId = 0;
    LinearLayout delivery_layout;
    String a;
    float as;
    RelativeLayout relativeLayout;
    String[] MethodNames = {"Select Payment Method", "Cash On Delivery"};
    int flags[] = {R.drawable.ic_select_payment, R.drawable.ic_cash};
    String getName, getNumber, getEmail, getUsername;
    TextView tv_delivery_price, vatprice;
    double grossTotal = 0;
    double calculatedDiscount = 0;
    double netTotal = 0;
    double deliveryCharges = 0;
    String couponCode;
    String calculatedCouponDiscount;
    String discountId;
    String calculatedSlabDiscount;
    String userFixedDiscount;
    SharedPreferences mypref;
    String gross, discount2;
    SharedPreferences user_preference;
    ArrayList<TempDataModel> listme = new ArrayList<>();

    String user_token, new_user_token;
    AddressListSpinnerAdapter addressListSpinnerAdapter;
    ArrayList<UserAddress> dataModelArrayList;
    SharedPreferences user_prefernce;
    SharedPreferences.Editor user_prefernce_editor;
    ProgressDialog progress;
    SharedPreferences.Editor sharedPreferencesEditor;
    SharedPreferences mypref2, myprefcart;
    SharedPreferences.Editor editor;
    OrderPost listme2;
    String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_payment);
        payment_method = findViewById(R.id.payment_spinner);
        grand_total = findViewById(R.id.checkout_total);
        off_Layout = findViewById(R.id.checkout_off_layout);
        profile_name = findViewById(R.id.profile_name_cart_profile);
        profile_email = findViewById(R.id.profile_email_cart_profile);
        profile_number = findViewById(R.id.profile_number_cart_profile);
        profile_username = findViewById(R.id.profile_username);
        place_order_button = findViewById(R.id.place_order_button);
        offprice = findViewById(R.id.offprice);
        add_more_addresses_button = findViewById(R.id.add_address);
        net_layout = findViewById(R.id.checkout_net_layout);
        net_pricemy = findViewById(R.id.netprice);
        delivery_layout = findViewById(R.id.checkout_deli_layout);
        tv_delivery_price = findViewById(R.id.delprice);
        vatprice = findViewById(R.id.vatprice);
        shipping_address_sp = findViewById(R.id.shipping_spinner);

        cartProductsRealm = realm.where(CartProductRealmModel.class).findAll();

        off_Layout.setVisibility(View.GONE);
        net_layout.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_cart_payment);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progress = new ProgressDialog(CartPaymentActivity.this);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        user_preference = getSharedPreferences("signupdata", MODE_PRIVATE);
        getName = user_preference.getString("name", "");
        getNumber = user_preference.getString("phone", "");
        getEmail = user_preference.getString("email", "");
        getUsername = user_preference.getString("name", "");
        user_type = user_preference.getString("user_type", "");
        user_token = user_preference.getString("token", "");
        String domestic = user_preference.getString("addressarraylist", null);

        addressesModelArraylist = new ArrayList<>();

        if (domestic != null && !domestic.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserAddress>>() {
            }.getType();
            addressesModelArraylist = gson.fromJson(domestic, type);
            if (addressesModelArraylist.size() != 0) {
                addressListSpinnerAdapter = new AddressListSpinnerAdapter(getApplicationContext(), addressesModelArraylist);
                shipping_address_sp.setAdapter(addressListSpinnerAdapter);
            } else {
                relativeLayout.setVisibility(View.GONE);
            }
        }

        mypref = getSharedPreferences("testcartdata", MODE_PRIVATE);
        gross = mypref.getString("grossTotal", "");
        discount2 = mypref.getString("calculatedDiscount", "");
        couponCode = mypref.getString("couponCode", "");
        calculatedCouponDiscount = mypref.getString("couponDiscount", "");
        discountId = mypref.getString("discountSlabId", "");
        calculatedSlabDiscount = mypref.getString("discountSlabAmount", "");
        userFixedDiscount = mypref.getString("userFixedDiscount", "");

        if (gross != null && !gross.isEmpty()) {

            grossTotal = Double.parseDouble(gross);
        }

        if (discount2 != null && !discount2.isEmpty()) {
            calculatedDiscount = Double.parseDouble(discount2);
        }

        DecimalFormat precision = new DecimalFormat("0.00");
        netTotal = (grossTotal) - calculatedDiscount;
        net_layout.setVisibility(View.VISIBLE);
        double net = netTotal * 100D / 100D;


        getconsumerdeliverycharges(precision.format(net) + "");


        double gross = grossTotal * 100D / 100D;
        grand_total.setText(String.valueOf(precision.format(gross)));

        if (calculatedDiscount > 0) {
            off_Layout.setVisibility(View.VISIBLE);
            double offprice_d = calculatedDiscount * 100D / 100D;
            offprice.setText(String.valueOf(precision.format(offprice_d)));
            //offprice.setText(String.valueOf(Math.round(calculatedDiscount * 100D) / 100D));
        }


        profile_name.setText(getName);
        profile_email.setText(getEmail);
        profile_number.setText(getNumber);
        profile_username.setText(getName);


        paymentSpinnerAdapter = new PaymentSpinnerAdapter(getApplicationContext(), flags, MethodNames);
        payment_method.setAdapter(paymentSpinnerAdapter);

        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (selected_shipping_addressId == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select or Add Address", Toast.LENGTH_SHORT).show();

                } else if (selecedPaymentMehod.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Payment Method Not Selected", Toast.LENGTH_SHORT).show();

                } else {

                    for (CartProductRealmModel cartProductRealmModel : cartProductsRealm) {
                        TempDataModel tempDataModel = new TempDataModel();
                        String product_id = String.valueOf(cartProductRealmModel.getId());
                        String product_quantity = String.valueOf(cartProductRealmModel.getProduct_quantity());
                        String color_id = cartProductRealmModel.getColor_name();
                        tempDataModel.setFrame_color(color_id);
                        tempDataModel.setProduct_id(product_id);
                        tempDataModel.setProduct_quantity(product_quantity);
                        tempDataModel.setSize(cartProductRealmModel.getSize());
                        tempDataModel.setLeft_engrave(cartProductRealmModel.getLeft_engrave());
                        tempDataModel.setRight_engrave(cartProductRealmModel.getRight_engrave());
                        listme.add(tempDataModel);
                    }


                    if (user_type != null && !user_type.isEmpty()) {
                        listme2 = new OrderPost(listme);
                        new_user_token = "bearer   " + user_token;
                        progress.show();
                        addorderforConsumer(new_user_token, String.valueOf(grossTotal), net_pricemy.getText().toString(), discountId, userFixedDiscount, couponCode, calculatedCouponDiscount, calculatedSlabDiscount, String.valueOf(deliveryCharges), String.valueOf(selected_shipping_addressId), selecedPaymentMehod, listme2);

                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }

                }
            }
        });


        add_more_addresses_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(CartPaymentActivity.this);
                dialog.setContentView(R.layout.custom_add_edit_address_layout);

                ImageView imageViewDialogClose = dialog.findViewById(R.id.close_dialogCustomAddress);
                final EditText et_addressDialog = dialog.findViewById(R.id.editText_Address_custom_dialog);
                final EditText et_cityDialog = dialog.findViewById(R.id.editText_city_custom_dialog);
                final EditText et_countryDialog = dialog.findViewById(R.id.editText_country_custom_dialog);
                Button btn_proceedDialog = dialog.findViewById(R.id.btn_proceed_customDialogAddress);

                btn_proceedDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = et_addressDialog.getText().toString().trim();
                        String country = et_countryDialog.getText().toString().trim();
                        String city = et_cityDialog.getText().toString().trim();
                        String address_type = "1";
                        if (user_type != null && !user_type.isEmpty()) {


                            if (address == null || address.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                            } else if (country == null || country.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please Enter Country", Toast.LENGTH_SHORT).show();
                            } else if (city == null || city.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Please Enter City", Toast.LENGTH_SHORT).show();
                            } else {
                                new_user_token = "bearer   " + user_token;
                                progress.show();
                                AddShippingAddressforConsumer(new_user_token, address, country, city, address_type);
                            }

                        } else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
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
        shipping_address_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    UserAddress addressesModel = addressListSpinnerAdapter.getItem(position);
                    String myid = addressesModel.getId();
                    if (myid != null && !myid.isEmpty()) {
                        selected_shipping_addressId = Integer.parseInt(myid);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                try {
                    if (i == 0) {

                        selecedPaymentMehod = "";

                    } else if (i == 1) {
                        selecedPaymentMehod = "Cash On Delivery";

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /////////////////////////////////////////Add Shipping Address for Consumer////////////////////////////////////
    private void AddShippingAddressforConsumer(String token, String address, String country, String city, String address_type) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CartShippingList> call = apiService.addConsumerShippingAddress(token, address, city, country, address_type);

        call.enqueue(new Callback<CartShippingList>() {
            @Override
            public void onResponse(Call<CartShippingList> call, Response<CartShippingList> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final CartShippingList listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
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
                                finish();
                            }
                        });


                    } else if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Maximum Address Already Added", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(), "Shipping Address Added Successfully", Toast.LENGTH_SHORT).show();
                                dataModelArrayList = new ArrayList<>(listFoodModel.getUserAddress());
                                user_prefernce = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE);
                                user_prefernce_editor = user_prefernce.edit();
                                Gson gson = new Gson();
                                if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                    String listofbillingaddress = gson.toJson(dataModelArrayList);
                                    user_prefernce_editor.putString("addressarraylist", listofbillingaddress);
                                }
                                user_prefernce_editor.apply();
                                Intent intent = getIntent();
                                overridePendingTransition(0, 0);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(intent);
                            }

                        });


                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<CartShippingList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Snackbar.make(findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                    }
                });
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if (i == 0) {

            selecedPaymentMehod = "";

        } else if (i == 1) {
            selecedPaymentMehod = "Cash On Delivery";

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        selecedPaymentMehod = "";
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    ///////////////////////////////Add Order for Consumer /////////////////////////////////////////////////////////////////

    private void addorderforConsumer(String token, String gross_total, String net_total, String discount_slab_id, String discount_amount, String coupon_code, String coupon_discount_amount, String fix_discount_amount, String delivery_charges, String shipping_address_id, String payment_method, OrderPost listofproduct) {

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<OrderList> call = apiService.sendorderforconsumer(token, gross_total, net_total, discount_slab_id, discount_amount, coupon_code, coupon_discount_amount, fix_discount_amount, delivery_charges, shipping_address_id, payment_method, listofproduct);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, Response<OrderList> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final OrderList listFoodModel = response.body();

                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    if (status.contains("401")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
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
                                finish();
                            }
                        });


                    } else if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (status.contains("3")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Account Unauthorized", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (status.contains("4")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Account Suspended", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else if (status.contains("5")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Account Deleted", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else if (status.contains("6")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Invalid  Token", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else if (status.contains("7")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Invalid  User Type", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else if (status.contains("8")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Invalid Coupon Code  ", Toast.LENGTH_SHORT).show();
                                mypref2 = getApplicationContext().getSharedPreferences("flagforcoupanbuttondisable", MODE_PRIVATE);
                                editor = mypref2.edit();
                                editor.clear();
                                editor.apply();
                            }
                        });

                    } else if (status.contains("9")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Invalid Available Quantity", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else if (status.contains("10")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listme.clear();
                                listme2 = null;
                                Toast.makeText(getApplicationContext(), "Getting Error during OrdersPost due to Invalid Product Quantity", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                mypref2 = getApplicationContext().getSharedPreferences("flagforcoupanbuttondisable", MODE_PRIVATE);
                                editor = mypref2.edit();
                                editor.clear();
                                editor.apply();
                                clearCart();
                                SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("cartitem", MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                                editor1.clear();
                                editor1.apply();
                                ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView(Integer.parseInt("0"));
                                Intent intent = new Intent(CartPaymentActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }

                        });


                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listme.clear();
                            listme2 = null;
                            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<OrderList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listme.clear();
                        listme2 = null;
                        Snackbar.make(findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                    }
                });
            }
        });

    }

    public void clearCart() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                cartProductsRealm.deleteAllFromRealm();
                long count = realm.where(CartProductRealmModel.class).count();
                Log.e("Cart Data1 = ", String.valueOf(count));

            }
        });


        long count = realm.where(CartProductRealmModel.class).count();
        Log.e("Cart Data2 = ", String.valueOf(count));

        myprefcart = getSharedPreferences("cartitem", MODE_PRIVATE);
        sharedPreferencesEditor = myprefcart.edit();
        sharedPreferencesEditor.putString(
                "cartValue", "0");
        sharedPreferencesEditor.apply();
//        ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView(Integer.parseInt(String.valueOf(count)));
        String val_check2;
        val_check2 = Objects.requireNonNull(myprefcart.getString("cartValue", ""));
        ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView(Integer.valueOf(val_check2));

    }


    public void getconsumerdeliverycharges(String netTotal) {
        String token = "Bearer " + user_preference.getString("token", "");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MiscellaneousModel> call = apiService.getconsumerdeliverycharges(token, netTotal);

        call.enqueue(new Callback<MiscellaneousModel>() {
            @Override
            public void onResponse(Call<MiscellaneousModel> call, retrofit2.Response<MiscellaneousModel> response) {
                final MiscellaneousModel listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //  progress.dismiss();
                                Snackbar.make(CartPaymentActivity.this.findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listofhome.getData().getDeliveryCharges() != null || !listofhome.getData().getDeliveryCharges().isEmpty()) {
                                    deliveryCharges = Double.parseDouble(listofhome.getData().getDeliveryCharges());
                                    delivery_layout.setVisibility(View.VISIBLE);
                                    tv_delivery_price.setText(String.valueOf(deliveryCharges));
                                } else {
                                    delivery_layout.setVisibility(View.GONE);

                                }
                                if (listofhome.getData().getVat() != null) {
                                    vatprice.setText(String.valueOf(listofhome.getData().getVat() + " %"));
                                } else {
                                    vatprice.setText(String.valueOf("0.0 %"));

                                }
                                if (listofhome.getData().getNetTotal() != null || !listofhome.getData().getNetTotal().isEmpty()) {

                                    net_pricemy.setText(String.valueOf(listofhome.getData().getNetTotal() + ""));
                                } else {
                                    net_pricemy.setText(String.valueOf("0.0"));

                                }
                            }
                        });
                    }

                } else {
                    if (CartPaymentActivity.this != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //     progress.dismiss();
                                Toast.makeText(CartPaymentActivity.this, "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<MiscellaneousModel> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                if (CartPaymentActivity.this != null) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //    progress.dismiss();
                            Toast.makeText(CartPaymentActivity.this, "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                        }

                    });

                }
            }
        });
    }


    public class Data {

        @SerializedName("professional_id")
        @Expose
        private String professionalId;
        @SerializedName("min_order")
        @Expose
        private String minOrder;
        @SerializedName("vat")
        @Expose
        private String vat;
        @SerializedName("delivery_charges")
        @Expose
        private String deliveryCharges;
        @SerializedName("net_total")
        @Expose
        private String netTotal;

        public String getProfessionalId() {
            return professionalId;
        }

        public void setProfessionalId(String professionalId) {
            this.professionalId = professionalId;
        }

        public String getMinOrder() {
            return minOrder;
        }

        public void setMinOrder(String minOrder) {
            this.minOrder = minOrder;
        }

        public String getVat() {
            return vat;
        }

        public void setVat(String vat) {
            this.vat = vat;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getNetTotal() {
            return netTotal;
        }

        public void setNetTotal(String netTotal) {
            this.netTotal = netTotal;
        }

    }

    public class MiscellaneousModel {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("data")
        @Expose
        private Data data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

    }
}



