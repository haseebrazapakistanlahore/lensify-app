package fyp.com.riona.FeaturedProductsFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.HomeFragment.CategoryDataModel;

import java.util.List;



public class ProfessionalHomeList {
    @SerializedName("categories")
    @Expose
    private List<CategoryDataModel> categories = null;
    @SerializedName("featured")
    @Expose
    private List<FeaturedProductDataModel> topSellingProducts = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public ProfessionalHomeList(List<CategoryDataModel> categories, List<FeaturedProductDataModel> topSellingProducts, String status, String message) {
        this.categories = categories;
        this.topSellingProducts = topSellingProducts;
        this.status = status;
        this.message = message;
    }

    public List<CategoryDataModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDataModel> categories) {
        this.categories = categories;
    }

    public List<FeaturedProductDataModel> getTopSellingProducts() {
        return topSellingProducts;
    }

    public void setTopSellingProducts(List<FeaturedProductDataModel> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
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
}
