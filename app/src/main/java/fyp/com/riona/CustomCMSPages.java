package fyp.com.riona;

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
import android.widget.TextView;
import android.widget.Toast;

import fyp.com.riona.R;

import retrofit2.Call;
import retrofit2.Callback;
import fyp.com.riona.CustomPage.PageDetailsList;
import fyp.com.riona.CustomPage.PageDetailsModel;

public class CustomCMSPages extends AppCompatActivity {


    TextView page_title, page_description;
    String title, id;
    ProgressDialog progress;
    PageDetailsModel d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_cmspages);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar_cmsPages);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        page_title = (TextView) findViewById(R.id.page_title);
        page_description = (TextView) findViewById(R.id.page_description);

        Intent intent = getIntent();
        title = intent.getStringExtra("page_title");
        id = intent.getStringExtra("page_id");

        progress = new ProgressDialog(CustomCMSPages.this);
        progress.setMessage("Loading..");
        if (!title.isEmpty() || !id.isEmpty()) {

            page_title.setText(title);
            progress.show();
            getPageDetails(id);


        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getPageDetails(String id) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<PageDetailsList> call = apiService.getPageById(id);

        call.enqueue(new Callback<PageDetailsList>() {
            @Override
            public void onResponse(Call<PageDetailsList> call, retrofit2.Response<PageDetailsList> response) {
                final PageDetailsList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if (status.contains("0")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progress.isShowing()) {
                                    progress.dismiss();
                                }
                                Snackbar.make(findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                            }
                        });

                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////

                        d = listofhome.getPage();

                    }


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (d.getTitle() != null && !d.getTitle().isEmpty()) {
                                page_title.setText(d.getTitle());

                            }
                            if (d.getDescription() != null && !d.getDescription().isEmpty()) {
                                page_description.setText(android.text.Html.fromHtml(d.getDescription().toString()));
                            }
                            if (progress.isShowing()) {
                                progress.dismiss();
                            }
                        }
                    });

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
            public void onFailure(Call<PageDetailsList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (progress.isShowing()) {
                            progress.dismiss();
                        }

                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                    }

                });
            }


        });
    }

}
