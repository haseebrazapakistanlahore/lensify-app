package fyp.com.riona.MainActivity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagesList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pages")
    @Expose
    private List<PagesDataModel> pages = null;
    @SerializedName("message")
    @Expose
    private String message;

    public PagesList(String status, List<PagesDataModel> pages, String message) {
        this.status = status;
        this.pages = pages;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PagesDataModel> getPages() {
        return pages;
    }

    public void setPages(List<PagesDataModel> pages) {
        this.pages = pages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
