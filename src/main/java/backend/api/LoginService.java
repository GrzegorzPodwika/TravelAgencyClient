package backend.api;

import backend.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface LoginService {

    @FormUrlEncoded
    @POST("/verifyTypeOfUser")
    Call<LoginResponse> verifyTypeOfUser(@Field("nick") String nick, @Field("password")  String password);
}
