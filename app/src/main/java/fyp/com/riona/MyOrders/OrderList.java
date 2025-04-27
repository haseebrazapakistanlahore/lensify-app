package fyp.com.riona.MyOrders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("orders")
    @Expose
    private List<OrderDataModel> orders = null;

    public OrderList(String status, String message, List<OrderDataModel> orders) {
        this.status = status;
        this.message = message;
        this.orders = orders;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderDataModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDataModel> orders) {
        this.orders = orders;
    }
}
