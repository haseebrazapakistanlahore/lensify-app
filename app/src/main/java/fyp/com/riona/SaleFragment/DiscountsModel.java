package fyp.com.riona.SaleFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountsModel {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("min_amount")
    @Expose
    private String minAmount;
    @SerializedName("max_amount")
    @Expose
    private String maxAmount;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("discount_percentage")
    @Expose
    private String discountPercentage;
    @SerializedName("image")
    @Expose
    private String image;

    public DiscountsModel() {
    }

    public DiscountsModel(String id, String minAmount, String maxAmount, String startDate, String endDate, String discountPercentage, String image) {
        this.id = id;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
