package fyp.com.riona.ProductDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailsList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("product")
    @Expose
    private ProductDetailModel product;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("noOfReviews")
    @Expose
    private String noOfReviews;

    public ProductDetailsList(String status, String message, ProductDetailModel product, String rating, String noOfReviews) {
        this.status = status;
        this.message = message;
        this.product = product;
        this.rating = rating;
        this.noOfReviews = noOfReviews;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(String noOfReviews) {
        this.noOfReviews = noOfReviews;
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

    public ProductDetailModel getProduct() {
        return product;
    }

    public void setProduct(ProductDetailModel product) {
        this.product = product;
    }
}
