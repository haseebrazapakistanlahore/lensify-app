package fyp.com.riona.CartFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("discount_percentage")
    @Expose
    private String discountPercentage;
    @SerializedName("message")
    @Expose
    private String message;

    public CouponList(String status, String discountPercentage, String message) {
        this.status = status;
        this.discountPercentage = discountPercentage;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
