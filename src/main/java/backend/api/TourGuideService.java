package backend.api;

import backend.model.TourGuide;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface TourGuideService {

    @GET("/getTourGuide")
    Call<Optional<TourGuide>> get(@Body Integer id);

    @GET("/getAllTourGuides")
    Call<List<TourGuide>> getAll();

    @POST("/saveTourGuide")
    Call<Integer> save(@Body TourGuide carrier);

    @POST("/updateTourGuide")
    Call<TourGuide> update(@Body TourGuide carrier);

    @POST("/deleteTourGuide")
    Call<Void> delete(@Body TourGuide carrier);
}
