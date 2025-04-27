package fyp.com.riona.SubInnerCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;
import fyp.com.riona.HomeFragment.CategoryDataModel;

import java.util.List;

public class SubInnerList {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("products")
    @Expose
    private List<FeaturedProductDataModel> products = null;

    @SerializedName("sub_category")
    @Expose
    private CategoryDataModel subCategory;

    public SubInnerList(String status, String message, List<FeaturedProductDataModel> products, CategoryDataModel subCategory) {
        this.status = status;
        this.message = message;
        this.products = products;
        this.subCategory = subCategory;
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

    public CategoryDataModel getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(CategoryDataModel subCategory) {
        this.subCategory = subCategory;
    }
}

