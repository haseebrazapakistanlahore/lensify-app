package fyp.com.riona.OrderDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsList {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("order")
    @Expose
    private OrderDetailsModel order;
    @SerializedName("orderProducts")
    @Expose
    private List<OrderDetailsProduct> orderProducts = null;

    public OrderDetailsList(String status, String message, OrderDetailsModel order, List<OrderDetailsProduct> orderProducts) {
        this.status = status;
        this.message = message;
        this.order = order;
        this.orderProducts = orderProducts;
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

    public OrderDetailsModel getOrder() {
        return order;
    }

    public void setOrder(OrderDetailsModel order) {
        this.order = order;
    }

    public List<OrderDetailsProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderDetailsProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
