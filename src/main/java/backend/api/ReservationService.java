package backend.api;

import backend.model.Reservation;
import backend.model.Tour;
import backend.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    @GET("/getReservation")
    Call<Optional<Reservation>> get(@Body Integer id);

    @GET("/getAllReservations")
    Call<List<Reservation>> getAll();

    @POST("/getAllReservationsByUser")
    Call<List<Reservation>> getAllByUser(@Body User user);

    @POST("/getAllReservationsByTour")
    Call<List<Reservation>> getAllByTour(@Body Tour tour);

    @POST("/saveReservation")
    Call<Integer> save(@Body Reservation attraction);

    @POST("/updateReservation")
    Call<Reservation> update(@Body Reservation attraction);

    @POST("/deleteReservation")
    Call<Void> delete(@Body Reservation attraction);

}
