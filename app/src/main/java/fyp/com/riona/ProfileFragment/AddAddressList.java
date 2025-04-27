package fyp.com.riona.ProfileFragment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAddressList {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("addresses")
    @Expose
    private AddAddressDataModel addresses;



    public AddAddressList(String status, String message, AddAddressDataModel addresses) {
        this.status = status;
        this.message = message;
        this.addresses = addresses;
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

    public AddAddressDataModel getAddresses() {
        return addresses;
    }

    public void setAddresses(AddAddressDataModel addresses) {
        this.addresses = addresses;
    }


}
