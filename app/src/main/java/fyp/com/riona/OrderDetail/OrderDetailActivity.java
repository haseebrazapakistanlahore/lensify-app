package fyp.com.riona.OrderDetail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MyOrders.MyOrdersActivity;

import fyp.com.riona.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderDetailActivity extends AppCompatActivity {

    TextView tv_tottal, tv_status, tv_date, tv_payment;
    Button btn_change_status;
    private RecyclerView recyclerView;
    String islogin, user_type;
    String token, newtoken;
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    SharedPreferences ordersprefernce;
    String order_id;
    ArrayList<OrderDetailsProduct> orderProductsList;
    OrderDetailsModel orderDetailsModel;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }
        recyclerView = findViewById(R.id.order_details_recyclerView);
        tv_tottal = findViewById(R.id.order_detail_total);
        tv_status = findViewById(R.id.order_details_status);
        tv_date = findViewById(R.id.order_detail_date);
        tv_payment = findViewById(R.id.order_detail_payment_method);
        linearLayout = findViewById(R.id.my_id_details_linear);
        btn_change_status = findViewById(R.id.changeStatus_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progress = new ProgressDialog(OrderDetailActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar_myOrderDetails);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        order_id = intent.getStringExtra("order_id");
        progress.setMessage("Loading..");
        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        ordersprefernce = getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = ordersprefernce.getString("user_type", "");
        islogin = ordersprefernce.getString("islogin", "");
        token = ordersprefernce.getString("token", "");
        if (islogin != null && !islogin.isEmpty()) {
            if (user_type.contains("0")) {
                newtoken = "bearer   " + token;
                progress.show();
                getOrderDetailsIdbyconsumer(newtoken, order_id);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
            Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
            finish();
        }
        btn_change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (islogin != null && !islogin.isEmpty()) {
                    if (user_type.contains("0")) {
                        newtoken = "bearer   " + token;
                        progress.show();
                        ChangeOrderDetailsstatusbyconsumer(newtoken, order_id, "Delivered");
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }

    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void getOrderDetailsIdbyconsumer(String token, String id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<OrderDetailsList> call = apiService.getOrderDetailsbyIdbyconsumer(token, id);

        call.enqueue(new Callback<OrderDetailsList>() {
            @Override
            public void onResponse(Call<OrderDetailsList> call, retrofit2.Response<OrderDetailsList> response) {
                final OrderDetailsList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("401")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
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

                                progress.dismiss();

                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                orderDetailsModel = new OrderDetailsModel();
                                orderDetailsModel = listofhome.getOrder();
                                orderProductsList = new ArrayList<>(listofhome.getOrderProducts());
                                recyclerView.setAdapter(new OrderDetailProductsAdapter(getApplicationContext(), orderProductsList));
                                String total = orderDetailsModel.getNetTotal();
                                String payment = orderDetailsModel.getPaymentMethod();
                                String status = orderDetailsModel.getOrderStatus();
                                String date = orderDetailsModel.getCreatedAt();
                                if (status.contains("Dispatched")) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                } else if (status.contains("Delivered")) {
                                    linearLayout.setVisibility(View.GONE);


                                } else {
                                    linearLayout.setVisibility(View.GONE);
                                }
                                if (payment != null && !payment.isEmpty()) {
                                    tv_payment.setText("Payment Method: " + payment);
                                }
                                if (date != null && !date.isEmpty()) {
                                    tv_date.setText("Date: " + date);
                                }
                                if (total != null && !total.isEmpty()) {
                                    tv_tottal.setText("Total: " + total);
                                }
                                if (status != null && !status.isEmpty()) {
                                    tv_status.setText(status);
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
            public void onFailure(Call<OrderDetailsList> call, Throwable t) {
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




    ////////////////////////////////////////////////////Function that is used to get Home Data////////////////////
    public void ChangeOrderDetailsstatusbyconsumer(String token, String id, String status) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<OrderDetailsList> call = apiService.changeorderstatusbyconsumer(token, id, status);

        call.enqueue(new Callback<OrderDetailsList>() {
            @Override
            public void onResponse(Call<OrderDetailsList> call, retrofit2.Response<OrderDetailsList> response) {
                final OrderDetailsList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("401")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
                                //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                SharedPreferences.Editor editor = getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                editor.clear(); //clear all stored data
                                editor.apply();
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

                                progress.dismiss();

                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////


                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();

                                Toast.makeText(getApplicationContext(), "Successfully Update", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OrderDetailActivity.this, MyOrdersActivity.class);
                                startActivity(intent);


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
            public void onFailure(Call<OrderDetailsList> call, Throwable t) {
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

}
