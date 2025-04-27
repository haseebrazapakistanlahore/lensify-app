package fyp.com.riona.HomeFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerDataModel {
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public BannerDataModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
