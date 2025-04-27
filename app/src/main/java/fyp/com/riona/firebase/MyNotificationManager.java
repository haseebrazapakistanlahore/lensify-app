package fyp.com.riona.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import fyp.com.riona.R;

import java.util.Objects;

import io.realm.Realm;
import fyp.com.riona.FriendlyDB;
import fyp.com.riona.GlobalChecks;
import fyp.com.riona.MainActivity.MainActivity;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;


public class MyNotificationManager {

    private Context mCtx;
    private static MyNotificationManager mInstance;
    private static final String NOTIFICATION = "notification";
    /////////////////////Local DataBase////////////////
    private Realm myRealm;
    GlobalChecks globalChecks;
    SharedPreferences notification_pref;
    SharedPreferences.Editor sharedPreferencesEditor ;
    long count;
    FriendlyDB db;
    private MyNotificationManager(Context context) {
        mCtx = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }

    public void displayNotification(String title, String body) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_skylight_notification)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setLargeIcon(BitmapFactory.decodeResource(mCtx.getResources(),
                                R.drawable.notify))
                .setColor(ContextCompat.getColor(mCtx, R.color.white))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setSummaryText(body))
                .setShowWhen(true)
                .setSound(sound)
                .setAutoCancel(true);
        Intent intent = new Intent(mCtx, MainActivity.class);
        intent.putExtra(NOTIFICATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(mCtx, 100, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
           db = new FriendlyDB(mCtx);
           db.addOrder(title);
        long count = db.getProfilesCount();

        //  ((MainActivity) Objects.requireNonNull(getApplicationContext())).addBadgeView(Integer.parseInt(String.valueOf(count)));
        notification_pref = mCtx.getSharedPreferences("notificationitem", MODE_PRIVATE);
        sharedPreferencesEditor = notification_pref.edit();
        sharedPreferencesEditor.putString(
                "notificationValue", String.valueOf(count));
        sharedPreferencesEditor.apply();


        ((MainActivity) Objects.requireNonNull(MainActivity.getInstance())).addBadgeView_notification(Integer.parseInt(String.valueOf(count)));













    /*
        * The first parameter is the notification id
        * better don't give a literal here (right now we are giving a int literal)
        * because using this id we can modify it later
        * */
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
//        /*
//        *  Clicking on the notification will take us to this intent
//        *  Right now we are using the MainActivity as this is the only activity we have in our application
//        *  But for your project you can customize it as you want
//        * */
//
//        Intent resultIntent = new Intent(mCtx, MainActivity.class);
//
//        /*
//        *  Now we will create a pending intent
//        *  The method getActivity is taking 4 parameters
//        *  All paramters are describing themselves
//        *  0 is the request code (the second parameter)
//        *  We can detect this code in the activity that will open by this we can get
//        *  Which notification opened the activity
//        * */
//        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        /*
//        *  Setting the pending intent to notification builder
//        * */
//
//        mBuilder.setContentIntent(pendingIntent);
//
//        NotificationManager mNotifyMgr =
//                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
//
//
//        /*
//        * The first parameter is the notification id
//        * better don't give a literal here (right now we are giving a int literal)
//        * because using this id we can modify it later
//        * */
//        if (mNotifyMgr != null) {
//            mNotifyMgr.notify(1, mBuilder.build());
//        }
    }

}
