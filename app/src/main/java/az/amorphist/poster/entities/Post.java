package az.amorphist.poster.entities;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("userId") private String userId;
    @SerializedName("id") private int postId;
    @SerializedName("title") private String postTitle;
    @SerializedName("body") private String postBody;

    public Post(String userId, int postId, String postTitle, String postBody) {
        this.userId = userId;
        this.postId = postId;
        this.postTitle = postTitle;
        this.postBody = postBody;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return postId == post.postId;
    }

    @Override
    public int hashCode() {
        return postId;
    }

    @Override
    public String toString() {
        return "Post{" +
                "userId='" + userId + '\'' +
                ", postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postBody='" + postBody + '\'' +
                '}';
    }
}
