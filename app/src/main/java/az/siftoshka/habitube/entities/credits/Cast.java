package az.siftoshka.habitube.entities.credits;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable {

    @SerializedName("cast_id") @Expose private int castId;
    @SerializedName("character") @Expose private String character;
    @SerializedName("credit_id") @Expose private String creditId;
    @SerializedName("id") @Expose private int id;
    @SerializedName("name") @Expose private String name;
    @SerializedName("order") @Expose private long order;
    @SerializedName("profile_path") @Expose private String profilePath;

    protected Cast(Parcel in) {
        castId = in.readInt();
        character = in.readString();
        creditId = in.readString();
        id = in.readInt();
        name = in.readString();
        order = in.readLong();
        profilePath = in.readString();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(castId);
        parcel.writeString(character);
        parcel.writeString(creditId);
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeLong(order);
        parcel.writeString(profilePath);
    }
}
