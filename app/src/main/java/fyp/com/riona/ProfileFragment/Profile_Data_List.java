package fyp.com.riona.ProfileFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile_Data_List {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("consumer")
    @Expose
    private ProfileDataModel consumer;
    @SerializedName("professional")
    @Expose
    private ProfileDataModel professional;

    public Profile_Data_List(String status, String message, ProfileDataModel consumer, ProfileDataModel professional) {
        this.status = status;
        this.message = message;
        this.consumer = consumer;
        this.professional = professional;
    }

    public ProfileDataModel getProfessional() {
        return professional;
    }

    public void setProfessional(ProfileDataModel professional) {
        this.professional = professional;
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

    public ProfileDataModel getConsumer() {
        return consumer;
    }

    public void setConsumer(ProfileDataModel consumer) {
        this.consumer = consumer;
    }
}
