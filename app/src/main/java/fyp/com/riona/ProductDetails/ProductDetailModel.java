package fyp.com.riona.ProductDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("sub_child_category_id")
    @Expose
    private String  subChildCategoryId;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("available_quantity")
    @Expose
    private String availableQuantity;
    @SerializedName("min_order_level")
    @Expose
    private String minOrderLevel;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("price_for_professional")
    @Expose
    private String priceForProfessional;
    @SerializedName("offer_available")
    @Expose
    private String offerAvailable;
    @SerializedName("offer_price")
    @Expose
    private String  offerPrice;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("color_no")
    @Expose
    private String colorNo;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("colors")
    @Expose
    private List<ColorsModel> colors = null;
    @SerializedName("reviews")
    @Expose
    private List<Reviews> reviews = null;
    @SerializedName("product_images")
    @Expose
    private List<ProductImagesModel> productImages = null;

    public ProductDetailModel(String id, String title, String description, String categoryId, String subCategoryId, String subChildCategoryId, String productType, String availableQuantity, String minOrderLevel, String price, String priceForProfessional, String offerAvailable, String offerPrice, String size, String colorNo, String thumbnail, String categoryName, String subCategoryName, List<ColorsModel> colors, List<Reviews> reviews, List<ProductImagesModel> productImages) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.subChildCategoryId = subChildCategoryId;
        this.productType = productType;
        this.availableQuantity = availableQuantity;
        this.minOrderLevel = minOrderLevel;
        this.price = price;
        this.priceForProfessional = priceForProfessional;
        this.offerAvailable = offerAvailable;
        this.offerPrice = offerPrice;
        this.size = size;
        this.colorNo = colorNo;
        this.thumbnail = thumbnail;
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.colors = colors;
        this.reviews = reviews;
        this.productImages = productImages;
    }

    public ProductDetailModel() {

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubChildCategoryId() {
        return subChildCategoryId;
    }

    public void setSubChildCategoryId(String subChildCategoryId) {
        this.subChildCategoryId = subChildCategoryId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(String availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getMinOrderLevel() {
        return minOrderLevel;
    }

    public void setMinOrderLevel(String minOrderLevel) {
        this.minOrderLevel = minOrderLevel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceForProfessional() {
        return priceForProfessional;
    }

    public void setPriceForProfessional(String priceForProfessional) {
        this.priceForProfessional = priceForProfessional;
    }

    public String getOfferAvailable() {
        return offerAvailable;
    }

    public void setOfferAvailable(String offerAvailable) {
        this.offerAvailable = offerAvailable;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColorNo() {
        return colorNo;
    }

    public void setColorNo(String colorNo) {
        this.colorNo = colorNo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public List<ColorsModel> getColors() {
        return colors;
    }

    public void setColors(List<ColorsModel> colors) {
        this.colors = colors;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    public List<ProductImagesModel> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImagesModel> productImages) {
        this.productImages = productImages;
    }
}
