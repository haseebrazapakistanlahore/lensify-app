package fyp.com.riona.MainActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PagesDataModel {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;

    public PagesDataModel(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public PagesDataModel() {

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
}
