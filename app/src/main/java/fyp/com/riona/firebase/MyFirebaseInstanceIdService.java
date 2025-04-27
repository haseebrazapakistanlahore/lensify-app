package fyp.com.riona.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;


//the class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseMessagingService {


    //this method will be called
    //when the token is generated
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);


    }
}
