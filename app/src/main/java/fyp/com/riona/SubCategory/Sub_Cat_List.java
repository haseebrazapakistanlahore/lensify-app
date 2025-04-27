package fyp.com.riona.SubCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.HomeFragment.CategoryDataModel;

import java.util.List;

public class Sub_Cat_List {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("products")
    @Expose
    private List<FeaturedProductDataModel> products = null;

    @SerializedName("category")
    @Expose
    private CategoryDataModel category;

    public Sub_Cat_List(String status, String message, List<FeaturedProductDataModel> products, CategoryDataModel category) {
        this.status = status;
        this.message = message;
        this.products = products;
        this.category = category;
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

    public CategoryDataModel getCategory() {
        return category;
    }

    public void setCategory(CategoryDataModel category) {
        this.category = category;
    }
}
