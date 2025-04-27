package fyp.com.riona.SaleFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaleList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("discounts")
    @Expose
    private List<DiscountsModel> discounts = null;

    public SaleList(String status, String message, List<DiscountsModel> discounts) {
        this.status = status;
        this.message = message;
        this.discounts = discounts;
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

    public List<DiscountsModel> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<DiscountsModel> discounts) {
        this.discounts = discounts;
    }
}
