package fyp.com.riona.HomeFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.FeaturedProductsFragment.FeaturedProductDataModel;

import java.util.List;


public class ConsumerHomeList {
    @SerializedName("banners")
    @Expose
    private List<BannerDataModel> banners = null;
    @SerializedName("categories")
    @Expose
    private List<CategoryDataModel> categories = null;
    @SerializedName("topSellingProducts")
    @Expose
    private List<FeaturedProductDataModel> topSellingProducts = null;
    @SerializedName("featuredProducts")
    @Expose
    private List<FeaturedProductDataModel> featuredProducts = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    public ConsumerHomeList(List<BannerDataModel> banners, List<CategoryDataModel> categories, List<FeaturedProductDataModel> topSellingProducts, List<FeaturedProductDataModel> featuredProducts, String status, String message) {
        this.banners = banners;
        this.categories = categories;
        this.topSellingProducts = topSellingProducts;
        this.featuredProducts = featuredProducts;
        this.status = status;
        this.message = message;
    }

    public List<BannerDataModel> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerDataModel> banners) {
        this.banners = banners;
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

    public List<FeaturedProductDataModel> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(List<FeaturedProductDataModel> featuredProducts) {
        this.featuredProducts = featuredProducts;
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

