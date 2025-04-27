package fyp.com.riona;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.NoInternet.NoInternetActivity;

import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;
import fyp.com.riona.R;


public class Splash extends AppCompatActivity {
    SharedPreferences user_preference;
    String islogin;
    // Connection detector class
    ConnectionDetector cd;
    // flag for Internet connection status
    Boolean isInternetPresent = true;
    String currentVersion="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Splash.this, R.color.colorPrimaryDark));
        }

        cd = new ConnectionDetector(Splash.this);

        user_preference = getSharedPreferences("signupdata", MODE_PRIVATE);
        islogin = user_preference.getString("islogin", "");



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                checkUpdate();



            }
        }, 1000);
    }


    public void checkUpdate()
    {
        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(Splash.this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                        Log.d("TGED", update.getLatestVersion());
                        Log.d("TGED", update.getLatestVersionCode() + "");
                        Log.d("TGED", update.getReleaseNotes());
                        Log.d("TGED", update.getUrlToDownload() + "");
                        Log.d("TGED", Boolean.toString(isUpdateAvailable));
                        if (isUpdateAvailable) {
                            androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(Splash.this).create();
                            alertDialog.setTitle("Update");
                            alertDialog.setIcon(R.mipmap.ic_launcher);
                            alertDialog.setMessage("New Update is available");
                            alertDialog.setCanceledOnTouchOutside(false);

                            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getApplicationContext().getPackageName())));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                                    }
                                }
                            });

                            alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Splash.this, Splash.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                            alertDialog.show();
                        } else {
                            isInternetPresent = cd.isConnectingToInternet();
                            if (!isInternetPresent) {
                                startActivity(new Intent(getApplicationContext(), NoInternetActivity.class));
                                finish();

                            } else {
                                if (islogin != null && !islogin.isEmpty()) {
                                    if (islogin.contains("1")) {
                                        Intent intent = new Intent(Splash.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(Splash.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    Intent intent = new Intent(Splash.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();
    }




}
