package fyp.com.riona.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import fyp.com.riona.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.ForgotPassword.ForgotPasswordActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.SignUp.SignUpActivity;
import fyp.com.riona.UserAddress;

public class LoginActivity extends AppCompatActivity {
    String devtoken = "";

    private TextView tv_signUp, tv_skip, tv_forgotPassword;
    EditText et_email, et_pass;
    String st_email, st_pass;
    SharedPreferences user_prefernce;
    SharedPreferences.Editor user_prefernce_editor;
    AlertDialog alertDialog;
    int user_type = 0;
    ProgressDialog progress;
    Button btn_sign_in;
//    RadioGroup userType_radioGroup;
    User_Data_Model user_data_model;
    ImageView img_visibility;
    int visible = 0;
    ArrayList<UserAddress> dataModelArrayList;
    SharedPreferences notification_pref;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
        ///// Initialization of Progress Dialog  ////////////
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        alertDialog = new AlertDialog.Builder(LoginActivity.this).create();

        tv_forgotPassword = findViewById(R.id.forgotPassword_TextView_login);

        img_visibility = findViewById(R.id.img_password_visibility);
        img_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visible == 0) {
                    et_pass.setTransformationMethod(null);
                    img_visibility.setImageResource(R.drawable.ic_visibility_gray);
                    visible = 1;

                } else {
                    et_pass.setTransformationMethod(new PasswordTransformationMethod());
                    img_visibility.setImageResource(R.drawable.ic_visibility_off_gray);
                    visible = 0;
                }

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryDark));
        }
        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

         btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_email.onEditorAction(EditorInfo.IME_ACTION_DONE);
                et_pass.onEditorAction(EditorInfo.IME_ACTION_DONE);
                st_email = et_email.getText().toString().trim();
                st_pass = et_pass.getText().toString().trim();


                if (user_type == 0) {

                        if (st_email.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please Insert Email", Toast.LENGTH_SHORT).show();
//                            et_email.setError("Please Insert Email");

                        } else if (st_pass.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Please Insert Password", Toast.LENGTH_SHORT).show();

//                            et_pass.setError("Please Insert Password");
                        } else {
                            progress.show();
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    devtoken = task.getResult();
                                    Log.e("TGED", "DEVICE: " + devtoken);
                                    Consumer_Login(st_email, st_pass, task.getResult());

                                }
                            });


                        }



                }
             }


        });
    }

    private void initialization() {
        tv_signUp = findViewById(R.id.signup_tv_loginActivity);
        tv_skip = findViewById(R.id.skip_tv_LoginActivity);
        et_email = findViewById(R.id.et_email_login);
        et_pass = findViewById(R.id.et_password_login);
         btn_sign_in = findViewById(R.id.login_user_btn);

    }


    ///////////////////////////Function that is used to Send TourOperator Data to Server/////////////////////////////////
    public void Consumer_Login(String email, String password, String device_id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<User_List_Login> call = apiService.Consumer_Login(email, password, device_id);

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
                                Snackbar.make(findViewById(android.R.id.content), "Invalid Email or Password", Snackbar.LENGTH_LONG).show();
                            }
                        });

//                        Snackbar.make(getWindow().getDecorView().getRootView(), "", Snackbar.LENGTH_LONG).show();
                    } else if (status.equals("4")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else if (status.equals("5")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Snackbar.make(findViewById(android.R.id.content), listFoodModel.getMessage(), Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String notificationcount = listFoodModel.getNotificationsCount();
                                user_data_model = listFoodModel.getUser_data_model();
                                dataModelArrayList = (ArrayList<UserAddress>) user_data_model.getAddresses();
                                String name = user_data_model.getFullName();
                                String email = user_data_model.getEmail();
                                String phone = user_data_model.getPhone();
                                String imageUrl = user_data_model.getProfileImage();
                                String islogin = "1";
                                user_prefernce = getApplicationContext().getSharedPreferences("signupdata", MODE_PRIVATE);
                                user_prefernce_editor = user_prefernce.edit();
                                user_prefernce_editor.putString("user_type", String.valueOf(user_type));
                                user_prefernce_editor.putString("name", name);
                                user_prefernce_editor.putString("email", email);
                                user_prefernce_editor.putString("phone", phone);
                                user_prefernce_editor.putString("token", token);
                                user_prefernce_editor.putString("url", imageUrl);
                                user_prefernce_editor.putString("islogin", islogin);
                                Gson gson = new Gson();
                                if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                    String listofbillingaddress = gson.toJson(dataModelArrayList);
                                    user_prefernce_editor.putString("addressarraylist", listofbillingaddress);
                                }
                                user_prefernce_editor.apply();


                                notification_pref = getSharedPreferences("notificationitem", MODE_PRIVATE);
                                sharedPreferencesEditor = notification_pref.edit();
                                sharedPreferencesEditor.putString(
                                        "notificationValue", notificationcount);
                                sharedPreferencesEditor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                finish();
                            }
                        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
