package backend.api;

import backend.model.AdditionalService;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface AdditionalServiceService {

    @GET("/getAdditionalService")
    Call<Optional<AdditionalService>> get(@Body Integer id);

    @GET("/getAllAdditionalServices")
    Call<List<AdditionalService>> getAll();

    @POST("/saveAdditionalService")
    Call<Integer> save(@Body AdditionalService additionalService);

    @POST("/updateAdditionalService")
    Call<AdditionalService> update(@Body AdditionalService additionalService);

    @POST("/deleteAdditionalService")
    Call<Void> delete(@Body AdditionalService additionalService);
}
