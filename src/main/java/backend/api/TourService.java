package backend.api;

import backend.model.Tour;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface TourService {

    @GET("/getTour")
    Call<Optional<Tour>> get(@Body Integer id);

    @GET("/getAllTours")
    Call<List<Tour>> getAll();

    @POST("/saveTour")
    Call<Integer> save(@Body Tour tour);

    @POST("/updateTour")
    Call<Tour> update(@Body Tour tour);

    @POST("/deleteTour")
    Call<Void> delete(@Body Tour tour);

}
