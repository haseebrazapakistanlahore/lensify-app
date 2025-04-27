package fyp.com.riona.MyOrders;

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
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String islogin,user_type;
    String token,newtoken;
    ArrayList<OrderDataModel> dataModelArrayList = new ArrayList<>();
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    TextView tv_nodata;
    SharedPreferences ordersprefernce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        recyclerView = findViewById(R.id.myOrder_recyclerView);
        tv_nodata = findViewById(R.id.noData_myOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Toolbar toolbar = findViewById(R.id.toolbar_myOrder);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        progress= new ProgressDialog(this);
        progress.setMessage("Loading...");
        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        ordersprefernce = getSharedPreferences("signupdata", MODE_PRIVATE);
        user_type = ordersprefernce.getString("user_type", "");
        islogin = ordersprefernce.getString("islogin", "");
        token = ordersprefernce.getString("token","");
        if (islogin != null && !islogin.isEmpty()) {
            if (user_type.contains("0")) {
                newtoken = "bearer   " + token;
                progress.show();
                getConsumerOrder(newtoken);
            }

        } else {
            Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    ////////////////////////////////////////////////////Function that is used to get ConsumerFavourite////////////////////
    public void getConsumerOrder(String token) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<OrderList> call = apiService.get_order_consumer(token);

        call.enqueue(new Callback<OrderList>() {
            @Override
            public void onResponse(Call<OrderList> call, retrofit2.Response<OrderList> response) {
                final OrderList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if(status.contains("401"))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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

                    }
                    else if (status.contains("0")) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progress.dismiss();

                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        dataModelArrayList = (ArrayList<OrderDataModel>) listofhome.getOrders();
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                progress.dismiss();
                                if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                    sortData();
                                    Collections.reverse(dataModelArrayList);
                                    recyclerView.setAdapter(new MyOrdersRecyclerAdapter(getApplicationContext(),dataModelArrayList));

                                } else {

                                    tv_nodata.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<OrderList> call, Throwable t) {
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


    private void sortData()
    {
        Collections.sort(dataModelArrayList, new Comparator<OrderDataModel>() {
            @Override
            public int compare(OrderDataModel lhs, OrderDataModel rhs) {
                return lhs.getInvoiceId().compareTo(rhs.getInvoiceId());
            }
        });


    }

}
