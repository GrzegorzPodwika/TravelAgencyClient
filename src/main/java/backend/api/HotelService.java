package backend.api;

import backend.model.Hotel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface HotelService {

    @GET("/getHotel")
    Call<Optional<Hotel>> get(@Body Integer id);

    @GET("/getAllHotels")
    Call<List<Hotel>> getAll();

    @POST("/saveHotel")
    Call<Integer> save(@Body Hotel hotel);

    @POST("/updateHotel")
    Call<Hotel> update(@Body Hotel hotel);

    @POST("/deleteHotel")
    Call<Void> delete(@Body Hotel hotel);
}
