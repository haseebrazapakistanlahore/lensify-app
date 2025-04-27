package fyp.com.riona.Notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import fyp.com.riona.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import fyp.com.riona.ApiClient;
import fyp.com.riona.ApiService;
import fyp.com.riona.FriendlyDB;
import fyp.com.riona.Login.LoginActivity;
import fyp.com.riona.MainActivity.MainActivity;
import fyp.com.riona.firebase.Constants;

import static android.content.Context.MODE_PRIVATE;


public class NotificationFragment extends Fragment {
    FriendlyDB db;
    private RecyclerView recyclerView;
    SharedPreferences notificationprefernce;
    String islogin,user_type;
    String token,newtoken;
   // ArrayList<FavouriteDataModel> dataModelArrayList = new ArrayList<>();
    ///  Progress Bar Declare  //////
    ProgressDialog progress;
    TextView tv_nodata;
    SharedPreferences notification_pref;
    SharedPreferences.Editor sharedPreferencesEditor ;
    ArrayList<NotificationDataModel> dataModelArrayList = new ArrayList<>();
    ArrayList<NotificationDataModel> dataModelArrayList_final = new ArrayList<>();
    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        }
        db = new FriendlyDB(getActivity());

        recyclerView = view.findViewById(R.id.notification_recyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        tv_nodata = view.findViewById(R.id.noData_notification)   ;
        progress= new ProgressDialog(getActivity());
        progress.setMessage("Loading..");
        ///////////////|Get Sharedprefernce Value for Auto Check Login ////////////
        if(getActivity()!=null)
        {
            notificationprefernce = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE);
            user_type = notificationprefernce.getString("user_type", "");
            islogin = notificationprefernce.getString("islogin", "");
            token = notificationprefernce.getString("token","");
            //////////////////////////////////////////////////////////////////////////////////////////////
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager =
                        (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }




        if (islogin != null && !islogin.isEmpty()) {
            if (user_type.contains("0")) {
                newtoken = "bearer   " + token;
              progress.show();
                getConsumerNotification(newtoken);
                db.deleteTableF();
                ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView_notification(Integer.parseInt("0"));
                clearnotification();


            }

        } else {
            Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
        }

        return view;
    }
    ////////////////////////////////////////////////////Function that is used to get ConsumerFavourite////////////////////
    public void getConsumerNotification(String token) {


        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<NotificationList> call = apiService.get_notification_consumer(token);

        call.enqueue(new Callback<NotificationList>() {
            @Override
            public void onResponse(Call<NotificationList> call, retrofit2.Response<NotificationList> response) {
                final NotificationList listofhome = response.body();

                if (listofhome != null) {
                    String status = listofhome.getStatus();
                    if(status.contains("401"))
                    {
                        if(getActivity()!=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Toast.makeText(getActivity(), "Login First", Toast.LENGTH_LONG).show();
                                    //Snackbar.make(context.getWindow().getDecorView().getRootView(), "Login First ", Snackbar.LENGTH_LONG).show();
                                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("signupdata", MODE_PRIVATE).edit();
                                    editor.clear(); //clear all stored data
                                    editor.apply();
                                    SharedPreferences.Editor editor2 = getActivity().getSharedPreferences("favrtListhead", MODE_PRIVATE).edit();
                                    editor2.clear(); //clear all stored data
                                    editor2.apply();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                        }

                    }
                    else if (status.contains("0")) {
                        if(getActivity()!=null)
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    progress.dismiss();

                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Try Again Later", Snackbar.LENGTH_LONG).show();

                                }
                            });
                        }



                    } else {
                        ////////////////////////////////////////////get data and store in Array List///////////////////////////////////////////////////////////////
                       if(getActivity()!=null)
                       {
                           dataModelArrayList = new ArrayList<>(listofhome.getNotifications());
                           getActivity().runOnUiThread(new Runnable() {

                               @Override
                               public void run() {
                                   progress.dismiss();
                                   if (dataModelArrayList != null && !dataModelArrayList.isEmpty()) {
                                       Collections.reverse(dataModelArrayList);
                                       recyclerView.setAdapter(new NotificationRecyclerAdapter(getActivity(),dataModelArrayList));

                                   } else {

                                       tv_nodata.setVisibility(View.VISIBLE);
                                   }





                               }
                           });

                       }

                    }
                } else {
                    if(getActivity()!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                progress.dismiss();

                                Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_LONG).show();

                            }
                        });

                    }


                }
            }

            @Override
            public void onFailure(Call<NotificationList> call, Throwable t) {
                /////Progress Dialog Dismiss  /////

                if(getActivity()!=null)
                {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            progress.dismiss();


                            Toast.makeText(getActivity(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();

                        }

                    });


                }


            }
        });
    }



    public void clearnotification() {

        if(getActivity()!=null)
        {
            notification_pref = getActivity().getSharedPreferences("notificationitem", MODE_PRIVATE);
            sharedPreferencesEditor = notification_pref.edit();
            sharedPreferencesEditor.putString(
                    "notificationValue", "0");
            sharedPreferencesEditor.apply();
            ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView_notification(Integer.parseInt("0"));

        }


    }
}
