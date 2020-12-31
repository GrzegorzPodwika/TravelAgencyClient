package backend.api;

import backend.model.Carrier;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface CarrierService{

    @GET("/getCarrier")
    Call<Optional<Carrier>> get(@Body Integer id);

    @GET("/getAllCarriers")
    Call<List<Carrier>> getAll();

    @POST("/saveCarrier")
    Call<Integer> save(@Body Carrier carrier);

    @POST("/updateCarrier")
    Call<Carrier> update(@Body Carrier carrier);

    @POST("/deleteCarrier")
    Call<Void> delete(@Body Carrier carrier);
}
