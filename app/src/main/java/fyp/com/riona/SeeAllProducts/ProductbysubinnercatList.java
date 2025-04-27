package fyp.com.riona.SeeAllProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;

import java.util.List;


public class ProductbysubinnercatList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("products")
    @Expose
    private List<FeaturedProductDataModel> products = null;

    public ProductbysubinnercatList(String status, String message, List<FeaturedProductDataModel> products) {
        this.status = status;
        this.message = message;
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

    public List<FeaturedProductDataModel> getProducts() {
        return products;
    }

    public void setProducts(List<FeaturedProductDataModel> products) {
        this.products = products;
    }
}
