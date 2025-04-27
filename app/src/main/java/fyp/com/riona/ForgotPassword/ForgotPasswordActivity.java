package fyp.com.riona.ForgotPassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import fyp.com.riona.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.Login.User_List_Login;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText et_Email;
    private Button bt_reset;
    private RadioGroup radioGroup;
    private RadioButton radioButtonConsumer , radioButtonProfessional;
    private int user_type = 0;
    private String st_email;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialization();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_forGotPassword);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        bt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_Email.onEditorAction(EditorInfo.IME_ACTION_DONE);
                st_email = et_Email.getText().toString().trim();

                if (user_type == 0) {
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(getApplicationContext(), "Please Select User Type First ", Toast.LENGTH_LONG).show();
                    } else {
                        if (st_email.isEmpty()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();

                        }else {
                            progress.show();
                            Consumer_ForgotPassword(st_email);
                        }

                    }


                }
            }


        });

    }

    private void initialization() {
        radioGroup = findViewById(R.id.user_type_ForGotPassword_RadioGroup);
        radioButtonProfessional = findViewById(R.id.radio_professional_forgot);
        radioButtonConsumer = findViewById(R.id.radio_consumer_forgot);
        bt_reset = findViewById(R.id.forget_password_reset_button);
        et_Email = findViewById(R.id.forget_password_email);
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait...");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    ///////////////////////////Function that is used to to get Email of Forgot Password/////////////////////////////////
    public void Consumer_ForgotPassword(String email) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<User_List_Login> call = apiService.Consumer_ForgotPassword(email);

        call.enqueue(new Callback<User_List_Login>() {
            @Override
            public void onResponse(Call<User_List_Login> call, Response<User_List_Login> response) {
                /////Progress Dialog Dismiss  /////
                progress.dismiss();
                final User_List_Login listFoodModel = response.body();
                String token;
                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    token = listFoodModel.getAccessToken();
                    if (status.equals("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), "Invalid Email", Snackbar.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();
                        Intent i = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();


                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(findViewById(android.R.id.content), "Please Check Your Internet Connection.", Snackbar.LENGTH_LONG).show();

                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<User_List_Login> call, Throwable t) {
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



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
