package fyp.com.riona.HomeFragment;


public class HomeSliderModel {


//    @SerializedName("image_url")
//    @Expose
//    private String imageUrl;


    private int image;

    public HomeSliderModel(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    //    public HomeSliderModel(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
}
