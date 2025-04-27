package fyp.com.riona.Notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationDataModel{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("consumer_id")
    @Expose
    private String consumerId;
    @SerializedName("professional_id")
    @Expose
    private String professionalId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("is_read")
    @Expose
    private String isRead;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public NotificationDataModel(String id, String consumerId, String professionalId, String content, String isRead, String createdAt, String updatedAt) {
        this.id = id;
        this.consumerId = consumerId;
        this.professionalId = professionalId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
