package fyp.com.riona.CartPayment;

import com.google.gson.annotations.SerializedName;

public class TempDataModel {
    @SerializedName("product_id")
    String product_id;
    @SerializedName("frame_color")
    String frame_color;
    @SerializedName("product_quantity")
    String product_quantity;
    @SerializedName("left_engrave")
    String left_engrave;
    @SerializedName("right_engrave")
    String right_engrave;
    @SerializedName("size")
    String size;

    public TempDataModel(String product_id,   String product_quantity,String size, String right_engrave,String left_engrave,String frame_color) {
        this.product_id = product_id;
        this.product_quantity = product_quantity;
        this.size = size;
        this.right_engrave = right_engrave;
        this.left_engrave = left_engrave;
        this.frame_color = frame_color;
    }

    public TempDataModel() {
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getFrame_color() {
        return frame_color;
    }

    public void setFrame_color(String frame_color) {
        this.frame_color = frame_color;
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

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
