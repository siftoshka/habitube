package az.siftoshka.habitube.entities.personcredits;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Crew implements Parcelable {

    @SerializedName("id") @Expose private Integer id;
    @SerializedName("original_title") @Expose private String originalTitle;
    @SerializedName("job") @Expose private String job;
    @SerializedName("title") @Expose private String title;
    @SerializedName("poster_path") @Expose private String posterPath;
    @SerializedName("name") @Expose private String name;
    @SerializedName("vote_average") @Expose private double voteAverage;
    @SerializedName("adult") @Expose private Boolean adult;
    @SerializedName("release_date") @Expose private String releaseDate;
    @SerializedName("credit_id") @Expose private String creditId;

    protected Crew(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        originalTitle = in.readString();
        job = in.readString();
        title = in.readString();
        posterPath = in.readString();
        name = in.readString();
        voteAverage = in.readDouble();
        byte tmpAdult = in.readByte();
        adult = tmpAdult == 0 ? null : tmpAdult == 1;
        releaseDate = in.readString();
        creditId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(originalTitle);
        dest.writeString(job);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(name);
        dest.writeDouble(voteAverage);
        dest.writeByte((byte) (adult == null ? 0 : adult ? 1 : 2));
        dest.writeString(releaseDate);
        dest.writeString(creditId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
