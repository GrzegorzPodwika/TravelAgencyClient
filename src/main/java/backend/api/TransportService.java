package backend.api;

import backend.model.Transport;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
import java.util.Optional;

public interface TransportService {

    @GET("/getTransport")
    Call<Optional<Transport>> get(@Body Integer id);

    @GET("/getAllTransports")
    Call<List<Transport>> getAll();

    @POST("/saveTransport")
    Call<Integer> save(@Body Transport transport);

    @POST("/updateTransport")
    Call<Transport> update(@Body Transport transport);

    @POST("/deleteTransport")
    Call<Void> delete(@Body Transport transport);
}
