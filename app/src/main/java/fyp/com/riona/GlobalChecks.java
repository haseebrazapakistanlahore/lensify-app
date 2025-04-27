package fyp.com.riona;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;

public class GlobalChecks {
    Context context;

    Realm realm = Realm.getDefaultInstance();

    public GlobalChecks(Context context) {
        this.context = context;
    }
    public int realmCheck (){
        Realm.getDefaultInstance();

        realm.refresh();
        RealmResults<CartProductRealmModel> realmModels = realm.where(CartProductRealmModel.class).findAllAsync();
        realmModels.load();
            int cartTotalItems = 0;
            long count = realm.where(CartProductRealmModel.class).count();

            cartTotalItems = Integer.parseInt(String.valueOf(count));

            if (cartTotalItems !=0){

                return  cartTotalItems;
            }

            else
                return 0;
    }




}
