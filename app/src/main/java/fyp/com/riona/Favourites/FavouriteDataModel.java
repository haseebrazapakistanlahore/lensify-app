package fyp.com.riona.Favourites;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteDataModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("offer_available")
    @Expose
    private String offerAvailable;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("description")
    @Expose
    private String description;

    public FavouriteDataModel(String id, String title, String price, String offerPrice, String offerAvailable, String thumbnail, String productType, String description) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.offerPrice = offerPrice;
        this.offerAvailable = offerAvailable;
        this.thumbnail = thumbnail;
        this.productType = productType;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferAvailable() {
        return offerAvailable;
    }

    public void setOfferAvailable(String offerAvailable) {
        this.offerAvailable = offerAvailable;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
