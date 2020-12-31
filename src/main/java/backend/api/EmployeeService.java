package backend.api;

import backend.model.Employee;
import retrofit2.Call;
import retrofit2.http.*;

public interface EmployeeService {

    @FormUrlEncoded
    @POST("/loginEmployee")
    Call<Employee> loginEmployee(@Field("nick") String nick, @Field("password") String password);

    @POST("updateEmployee")
    Call<Employee> updateEmployee(@Body Employee employeeToUpdate);

}
