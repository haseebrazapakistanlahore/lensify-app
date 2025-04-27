package fyp.com.riona.SignUp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import fyp.com.riona.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextView tv_login, tv_skip;
    private EditText et_name, et_email, et_phone, et_pass, et_confrm_pass, et_company, et_company_address;
    String st_name, st_email, st_phone, st_pass, st_confrm_pass, st_company, st_comapny_address;
    Button btn_sign_up;
     int user_type = 0;
    ProgressDialog progress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    UserDataModel userDataModel;
    SharedPreferences user_prefernce;
    SharedPreferences.Editor user_prefernce_editor;
    AlertDialog alertDialog;
    ImageView img_visibilityPassword, img_visibility_confirmPassword;
    int visiblePassword, visibleConfirmPass = 0;
    SharedPreferences notification_pref;
    SharedPreferences.Editor sharedPreferencesEditor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initialization();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(SignUpActivity.this, R.color.colorPrimaryDark));
        }
        ///// Initialization of Progress Dialog  ////////////
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        img_visibilityPassword = findViewById(R.id.img_password_visibility_signUp1);
        img_visibilityPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visiblePassword == 0) {
                    et_pass.setTransformationMethod(null);
                    img_visibilityPassword.setImageResource(R.drawable.ic_visibility_gray);
                    visiblePassword = 1;

                } else {
                    et_pass.setTransformationMethod(new PasswordTransformationMethod());
                    img_visibilityPassword.setImageResource(R.drawable.ic_visibility_off_gray);
                    visiblePassword = 0;
                }

            }
        });

        img_visibility_confirmPassword = findViewById(R.id.img_password_visibility_signUp2);
        img_visibility_confirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visibleConfirmPass == 0) {
                    et_confrm_pass.setTransformationMethod(null);
                    img_visibility_confirmPassword.setImageResource(R.drawable.ic_visibility_gray);
                    visibleConfirmPass = 1;

                } else {
                    et_confrm_pass.setTransformationMethod(new PasswordTransformationMethod());
                    img_visibility_confirmPassword.setImageResource(R.drawable.ic_visibility_off_gray);
                    visibleConfirmPass = 0;
                }

            }
        });


        alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_type == 0) {

                        st_name = et_name.getText().toString().trim();
                        st_email = et_email.getText().toString().trim();
                        st_pass = et_pass.getText().toString().trim();
                        st_phone = et_phone.getText().toString().trim();
                        st_confrm_pass = et_confrm_pass.getText().toString().trim();

                        if (st_name.isEmpty()) {
                            Toast.makeText(SignUpActivity.this, "Please Insert Full Name", Toast.LENGTH_SHORT).show();
                        } else if (st_email.isEmpty()) {
                            Toast.makeText(SignUpActivity.this, "Please Insert Email", Toast.LENGTH_SHORT).show();
                        } else if (st_phone.isEmpty()) {
                            Toast.makeText(SignUpActivity.this, "Please Insert Phone", Toast.LENGTH_SHORT).show();
                        } else if (st_pass.isEmpty()) {
                            Toast.makeText(SignUpActivity.this, "Please Insert Password", Toast.LENGTH_SHORT).show();
                        } else if (st_confrm_pass.isEmpty()) {
                            Toast.makeText(SignUpActivity.this, "Please Insert Password", Toast.LENGTH_SHORT).show();
                        } else if (!st_email.matches(emailPattern)) {
                            Toast.makeText(SignUpActivity.this, "Enter a Valid Email", Toast.LENGTH_SHORT).show();
//                                et_email.setError("Enter Valid Email");
                        } else if (!st_pass.equals(st_confrm_pass)) {

                            Toast.makeText(SignUpActivity.this, "Passwords Does Not Match", Toast.LENGTH_SHORT).show();
//                                et_pass.setError("Password Do Not Match");
//                                et_confrm_pass.setError("Password Do Not Match");


                        }
                        else if(st_pass.length()<6 )
                        {
                            Toast.makeText(SignUpActivity.this, "Password should be greater than 6", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            ///// Progress Dialog  Show////////////
                            progress.show();
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {

                                    Log.e("TGED", "DEVICE: " + task.getResult());
                                    signup_method_consumer(st_email, st_name, st_pass, st_phone,task.getResult());

                                }
                            });


                        }


                }

            }

        });
    }

    private void initialization() {
        tv_login = findViewById(R.id.login_tv_SignUpActivity);
        tv_skip = findViewById(R.id.skip_tv_SignUpActivity);
        et_name = findViewById(R.id.user_signup_name);
        et_email = findViewById(R.id.user_signup_email);
        et_phone = findViewById(R.id.user_signup_phone);
        et_pass = findViewById(R.id.user_signup_password);
        et_confrm_pass = findViewById(R.id.user_signup_confirm_pass);
        et_company = findViewById(R.id.user_signup_company_name);
        et_company_address = findViewById(R.id.user_signup_company_address);
        btn_sign_up = findViewById(R.id.user_signup_btn);

    }

    ///////////////////////////Function that is used to Send Consumer  Data to Server/////////////////////////////////
    public void signup_method_consumer(String email, String name, String password, String phone,String device_id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ConsumerUserList> call = apiService.Consumer_SignUp(email, name, password, phone,device_id);

        call.enqueue(new Callback<ConsumerUserList>() {
            @Override
            public void onResponse(Call<ConsumerUserList> call, Response<ConsumerUserList> response) {
                ///// Progress Dialog  Dismiss////////////
                progress.dismiss();
                final ConsumerUserList listFoodModel = response.body();
                final String token;
                if (listFoodModel != null) {
                    String status = listFoodModel.getStatus();
                    token = listFoodModel.getAccessToken();
                    ////////////////////////////////If Getting Error From Server or Email Already Registered  ///////////////////////////////////////////////////////////////
                    if (status.contains("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progress.dismiss();
                                Snackbar.make(findViewById(android.R.id.content), "Email Already In Use", Snackbar.LENGTH_LONG).show();

                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
////////////////////////////////Array List that Get data from Server and save in Sharedprefernce   ///////////////////////////////////////////////////////////////
                                String notificationcount=listFoodModel.getNotificationsCount();
                                userDataModel = listFoodModel.getConsumer();
                                String name = userDataModel.getFullName();
                                String email = userDataModel.getEmail();
                                String phone = userDataModel.getPhone();
                                String islogin = "1";
                                user_prefernce = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE);
                                user_prefernce_editor = user_prefernce.edit();
                                user_prefernce_editor.putString("islogin", islogin);
                                user_prefernce_editor.putString("name", name);
                                user_prefernce_editor.putString("email", email);
                                user_prefernce_editor.putString("phone", phone);
                                user_prefernce_editor.putString("token", token);
                                user_prefernce_editor.putString("user_type", String.valueOf(user_type));
                                user_prefernce_editor.apply();
                                progress.dismiss();
                                ////////////////////////////////Go to Main Activity or Home Page///////////////////////////////////////////////////////////////

                                notification_pref = getSharedPreferences("notificationitem", MODE_PRIVATE);
                                sharedPreferencesEditor = notification_pref.edit();
                                sharedPreferencesEditor.putString(
                                        "notificationValue", notificationcount);
                                sharedPreferencesEditor.apply();
                                Toast.makeText(getApplicationContext(), "Successfully Signed Up", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();

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
            public void onFailure(Call<ConsumerUserList> call, Throwable t) {
                //   Log.e(TAG, "Unable to submit post to API.");

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

    public void clearFields() {

        et_name.getText().clear();
        et_email.getText().clear();
        et_pass.getText().clear();
        et_confrm_pass.getText().clear();
        et_phone.getText().clear();
        et_company.getText().clear();
        et_company_address.getText().clear();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext() , MainActivity.class));
        finish();
    }
}
