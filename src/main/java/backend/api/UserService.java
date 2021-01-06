package backend.api;


import backend.model.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Optional;

public interface UserService {

    @GET("/getUser")
    Call<Optional<User>> get(@Body Integer id);

    @GET("/getAllUsers")
    Call<List<User>> getAll();

    @POST("/saveUser")
    Call<Integer> save(@Body User user);

    @POST("/updateUser")
    Call<User> update(@Body User user);

    @POST("/deleteUser")
    Call<Void> delete(@Body User user);

    @FormUrlEncoded
    @POST("/loginUser")
    Call<User> loginUser(@Field("nick") String nick, @Field("password") String password);

}
