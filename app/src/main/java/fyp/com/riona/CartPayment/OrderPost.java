package fyp.com.riona.CartPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderPost {
    @SerializedName("products")
    @Expose
    private ArrayList<TempDataModel> order;

    public OrderPost(ArrayList<TempDataModel> order) {
        this.order = order;
    }
}
