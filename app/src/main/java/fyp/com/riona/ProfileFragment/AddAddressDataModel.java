package fyp.com.riona.ProfileFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddAddressDataModel implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("consumer_id")
    @Expose
    private String consumer_id;

    @SerializedName("professional_id")
    @Expose
    private ProfileDataModel professional_id;

    @SerializedName("address")
    @Expose
    private ProfileDataModel address;

    @SerializedName("city")
    @Expose
    private ProfileDataModel city;

    @SerializedName("country")
    @Expose
    private ProfileDataModel country;

    @SerializedName("address_type")
    @Expose
    private ProfileDataModel address_type;

    public AddAddressDataModel(String id, String consumer_id, ProfileDataModel professional_id, ProfileDataModel address, ProfileDataModel city, ProfileDataModel country, ProfileDataModel address_type) {
        this.id = id;
        this.consumer_id = consumer_id;
        this.professional_id = professional_id;
        this.address = address;
        this.city = city;
        this.country = country;
        this.address_type = address_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumer_id() {
        return consumer_id;
    }

    public void setConsumer_id(String consumer_id) {
        this.consumer_id = consumer_id;
    }

    public ProfileDataModel getProfessional_id() {
        return professional_id;
    }

    public void setProfessional_id(ProfileDataModel professional_id) {
        this.professional_id = professional_id;
    }

    public ProfileDataModel getAddress() {
        return address;
    }

    public void setAddress(ProfileDataModel address) {
        this.address = address;
    }

    public ProfileDataModel getCity() {
        return city;
    }

    public void setCity(ProfileDataModel city) {
        this.city = city;
    }

    public ProfileDataModel getCountry() {
        return country;
    }

    public void setCountry(ProfileDataModel country) {
        this.country = country;
    }

    public ProfileDataModel getAddress_type() {
        return address_type;
    }

    public void setAddress_type(ProfileDataModel address_type) {
        this.address_type = address_type;
    }
}
