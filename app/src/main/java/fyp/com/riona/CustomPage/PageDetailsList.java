package fyp.com.riona.CustomPage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PageDetailsList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("page")
    @Expose
    private PageDetailsModel page;
    @SerializedName("message")
    @Expose
    private String message;

    public PageDetailsList(String status, PageDetailsModel page, String message) {
        this.status = status;
        this.page = page;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PageDetailsModel getPage() {
        return page;
    }

    public void setPage(PageDetailsModel page) {
        this.page = page;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
