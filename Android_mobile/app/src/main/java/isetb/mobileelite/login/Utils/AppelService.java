package isetb.mobileelite.login.Utils;

import java.util.List;

import isetb.mobileelite.login.Model.Appel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AppelService {

    // Endpoint to get all calls for the logged-in user
    @GET("/calls/getcalls")
    Call<List<Appel>> getAllCalls(@Header("Authorization") String accessToken);
    @PUT("/calls/update/{id}")
    Call<Appel> updateCall(@Path("id") Long id, @Body Appel updatedCall);

    @DELETE("/calls/delete/{id}")
    Call<Void> deleteCall(@Path("id") Long id);

    @GET("/calls/getbyid/{id}")
    Call<Appel> getCallById(@Path("id") Long id);
    @POST("/calls/add")
    Call<Appel> addCall(@Header("Authorization") String accessToken, @Body Appel newCall);


}
