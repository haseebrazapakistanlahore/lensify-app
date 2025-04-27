package fyp.com.riona.SignUp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerUserList {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("consumer")
    @Expose
    private UserDataModel consumer;
    @SerializedName("notificationsCount")
    @Expose
    private String  notificationsCount;

    public ConsumerUserList(String status, String message, String accessToken, UserDataModel consumer, String notificationsCount) {
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
        this.consumer = consumer;
        this.notificationsCount = notificationsCount;
    }

    public String getNotificationsCount() {
        return notificationsCount;
    }

    public void setNotificationsCount(String notificationsCount) {
        this.notificationsCount = notificationsCount;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserDataModel getConsumer() {
        return consumer;
    }

    public void setConsumer(UserDataModel consumer) {
        this.consumer = consumer;
    }
}
