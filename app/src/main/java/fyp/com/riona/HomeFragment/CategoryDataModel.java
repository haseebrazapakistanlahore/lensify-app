package fyp.com.riona.HomeFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.SubCategory.SabCatDataModel;

import java.util.List;

public class CategoryDataModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("sub_categories")
    @Expose
    private List<SabCatDataModel> subCategories = null;
    @SerializedName("sub_child_categories")
    @Expose
    private List<SabCatDataModel> sub_child_categories = null;

    public CategoryDataModel(String id, String title, String image, String type, List<SabCatDataModel> subCategories, List<SabCatDataModel> sub_child_categories) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.type = type;
        this.subCategories = subCategories;
        this.sub_child_categories = sub_child_categories;
    }

    public CategoryDataModel() {

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SabCatDataModel> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SabCatDataModel> subCategories) {
        this.subCategories = subCategories;
    }

    public List<SabCatDataModel> getSub_child_categories() {
        return sub_child_categories;
    }

    public void setSub_child_categories(List<SabCatDataModel> sub_child_categories) {
        this.sub_child_categories = sub_child_categories;
    }
}
