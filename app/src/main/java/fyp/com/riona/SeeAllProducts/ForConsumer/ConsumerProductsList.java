package fyp.com.riona.SeeAllProducts.ForConsumer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;

import java.util.List;


public class ConsumerProductsList {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("totalPages")
    @Expose
    private String totalPages;

    @SerializedName("products")
    @Expose
    private List<FeaturedProductDataModel> products = null;

    public ConsumerProductsList(String status, String message, String totalPages, List<FeaturedProductDataModel> products) {
        this.status = status;
        this.message = message;
        this.totalPages = totalPages;
        this.products = products;
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

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public List<FeaturedProductDataModel> getProducts() {
        return products;
    }

    public void setProducts(List<FeaturedProductDataModel> products) {
        this.products = products;
    }
}
