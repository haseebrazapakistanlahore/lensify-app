package fyp.com.riona.Favourites;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Favourite_List {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("favourites")
    @Expose
    private List<FavouriteDataModel> favourites = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Favourite_List(String status, List<FavouriteDataModel> favourites, String message) {
        this.status = status;
        this.favourites = favourites;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FavouriteDataModel> getFavourites() {
        return favourites;
    }

    public void setFavourites(List<FavouriteDataModel> favourites) {
        this.favourites = favourites;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
