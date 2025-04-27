package fyp.com.riona;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class CartProductRealmModel extends RealmObject {


    private String id;
    private String title;
    private String description;
    private String available_quantity;
    private String min_order_level;
    private String shipping_cost;
    private String price;
    private String price_for_professional;
    private String color_name;
    private String color_number;
    private String category_id;
    private String product_sku;
    private String offer_available;
    private String offer_price;
    private String product_type;
    private String thumbnail;
    private String category_name;
    private String product_total;
    private String product_quantity;
    private String dummy;
    private String left_engrave;
    private String right_engrave;
    private String size;

    public CartProductRealmModel() {
    }

    public CartProductRealmModel(String id,String left_engrave,String right_engrave,String size, String title, String description, String available_quantity, String min_order_level, String shipping_cost, String price, String price_for_professional, String color_name, String color_number, String category_id, String product_sku, String offer_available, String offer_price, String product_type, String thumbnail, String category_name, String product_total, String product_quantity, String dummy) {
        this.id = id;
        this.left_engrave = left_engrave;
        this.right_engrave = right_engrave;
        this.size = size;
        this.title = title;
        this.description = description;
        this.available_quantity = available_quantity;
        this.min_order_level = min_order_level;
        this.shipping_cost = shipping_cost;
        this.price = price;
        this.price_for_professional = price_for_professional;
        this.color_name = color_name;
        this.color_number = color_number;
        this.category_id = category_id;
        this.product_sku = product_sku;
        this.offer_available = offer_available;
        this.offer_price = offer_price;
        this.product_type = product_type;
        this.thumbnail = thumbnail;
        this.category_name = category_name;
        this.product_total = product_total;
        this.product_quantity = product_quantity;
        this.dummy = dummy;
    }

    public String getLeft_engrave() {
        return left_engrave;
    }

    public void setLeft_engrave(String left_engrave) {
        this.left_engrave = left_engrave;
    }

    public String getRight_engrave() {
        return right_engrave;
    }

    public void setRight_engrave(String right_engrave) {
        this.right_engrave = right_engrave;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
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

    public String getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(String available_quantity) {
        this.available_quantity = available_quantity;
    }

    public String getMin_order_level() {
        return min_order_level;
    }

    public void setMin_order_level(String min_order_level) {
        this.min_order_level = min_order_level;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_for_professional() {
        return price_for_professional;
    }

    public void setPrice_for_professional(String price_for_professional) {
        this.price_for_professional = price_for_professional;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_number() {
        return color_number;
    }

    public void setColor_number(String color_number) {
        this.color_number = color_number;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_sku() {
        return product_sku;
    }

    public void setProduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }

    public String getOffer_available() {
        return offer_available;
    }

    public void setOffer_available(String offer_available) {
        this.offer_available = offer_available;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getProduct_total() {
        return product_total;
    }

    public void setProduct_total(String product_total) {
        this.product_total = product_total;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }


}
