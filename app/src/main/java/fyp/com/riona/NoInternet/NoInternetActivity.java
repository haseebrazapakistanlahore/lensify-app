package fyp.com.riona.NoInternet;

import android.content.Intent;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fyp.com.riona.R;

import fyp.com.riona.Splash;

public class NoInternetActivity extends AppCompatActivity {

    private Button bt_retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(NoInternetActivity.this, R.color.colorPrimaryDark));
        }
        bt_retry = findViewById(R.id.noInternet_retry);
        bt_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Splash.class));
                finish();
            }
        });
    }
}
