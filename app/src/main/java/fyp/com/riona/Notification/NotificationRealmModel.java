package fyp.com.riona.Notification;

public class NotificationRealmModel {

    private String id;
    private String consumerId;
    private String professionalId;
    private String content;
    private String isRead;
    private String createdAt;
    private String updatedAt;
    public NotificationRealmModel()
    {

    }
    public NotificationRealmModel(String id, String consumerId, String professionalId, String content, String isRead, String createdAt, String updatedAt) {
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
