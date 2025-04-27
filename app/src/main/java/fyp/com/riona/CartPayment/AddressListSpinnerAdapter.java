package fyp.com.riona.CartPayment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fyp.com.riona.R;
import fyp.com.riona.UserAddress;

import java.util.ArrayList;

public class AddressListSpinnerAdapter extends BaseAdapter {

    Context context;
    ArrayList<UserAddress> addressesModels;

    public AddressListSpinnerAdapter(Context context,  ArrayList<UserAddress> addressesModels) {
        this.context = context;
        this.addressesModels = addressesModels;
    }

    @Override
    public int getCount() {
        return addressesModels.size();
    }

    @Override
    public UserAddress getItem(int i) {
        return addressesModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.payment_spinner_view,viewGroup,false);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(R.drawable.ic_home_black_24dp);
        String myaddress=addressesModels.get(i).getAddress() + "," + addressesModels.get(i).getCity() +  "," + addressesModels.get(i).getCountry();
        names.setText(myaddress);
        return view;
    }
}