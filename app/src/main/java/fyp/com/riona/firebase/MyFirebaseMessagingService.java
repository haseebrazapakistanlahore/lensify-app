package fyp.com.riona.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("MyNotification", remoteMessage.getNotification().toString());
        Log.d("MyNotification", remoteMessage.getNotification().toString());

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily

        if (remoteMessage.getData().size() > 0) {
            //handle the data message here
        }

        //getting the title and the body
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        //then here we can use the title and body to build a notification

        MyNotificationManager.getInstance(getApplicationContext()).displayNotification(title, body);
        Log.d("MyNotification", remoteMessage.getNotification().toString());
        Log.d("MyNotification", remoteMessage.getNotification().toString());
    }
}
