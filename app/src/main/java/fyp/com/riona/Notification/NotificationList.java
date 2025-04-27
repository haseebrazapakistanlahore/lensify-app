package fyp.com.riona.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("notifications")
    @Expose
    private List<NotificationDataModel> notifications = null;

    public NotificationList(String status, String message, List<NotificationDataModel> notifications) {
        this.status = status;
        this.message = message;
        this.notifications = notifications;
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

    public List<NotificationDataModel> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDataModel> notifications) {
        this.notifications = notifications;
    }
}
