package fyp.com.riona.CartPayment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.UserAddress;

import java.util.List;

public class CartShippingList {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("addresses")
    @Expose
    private List<UserAddress> userAddress = null;

    public CartShippingList(String status, String message, List<UserAddress> userAddress) {
        this.status = status;
        this.message = message;
        this.userAddress = userAddress;
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

    public List<UserAddress> getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(List<UserAddress> userAddress) {
        this.userAddress = userAddress;
    }
}
