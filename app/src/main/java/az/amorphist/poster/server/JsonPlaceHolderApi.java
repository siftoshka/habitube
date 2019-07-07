package az.amorphist.poster.server;

import az.amorphist.poster.entities.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("/posts/{id}")
    Call<Post> getPost(
            @Path("id") int id
    );
}
