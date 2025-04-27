package fyp.com.riona.CategoriesFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import fyp.com.riona.HomeFragment.CategoryDataModel;

import java.util.List;


public class CategoriesList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("categories")
    @Expose
    private List<CategoryDataModel> categories = null;

    public CategoriesList(String status, String message, List<CategoryDataModel> categories) {
        this.status = status;
        this.message = message;
        this.categories = categories;
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

    public List<CategoryDataModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDataModel> categories) {
        this.categories = categories;
    }
}
