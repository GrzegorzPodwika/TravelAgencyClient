package backend.api;

import backend.model.Attraction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface AttractionService {


    @GET("/getAttraction")
    Call<Optional<Attraction>> get(@Body Integer id);

    @GET("/getAllAttractions")
    Call<List<Attraction>> getAll();

    @POST("/saveAttraction")
    Call<Integer> save(@Body Attraction attraction);

    @POST("/updateAttraction")
    Call<Attraction> update(@Body Attraction attraction);

    @POST("/deleteAttraction")
    Call<Void> delete(@Body Attraction attraction);
}
