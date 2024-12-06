package isetb.mobileelite.login.Utils;
import isetb.mobileelite.login.Model.LoginRequest;
import isetb.mobileelite.login.Model.LoginResponse;
import isetb.mobileelite.login.Model.RefreshTokenRequest;
import isetb.mobileelite.login.Model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;
public interface UserService {

    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("/auth/refresh")  // Add this method for refreshing the access token
    Call<LoginResponse> refresh(@Body RefreshTokenRequest refreshTokenRequest);  // Accept refresh token request
    @POST("/Users/add")
    Call<Void> addUser(@Body User user); // Utilisez votre modèle User pour envoyer les données
    @POST("/Users/addWithApproval")
    Call<Void> addUserWithApprouval(@Body User user); // Utilisez votre modèle User pour envoyer les données
    @PUT("Users/update/{id}")
    Call<User> updateUser(@Path("id") long id, @Body User user);
    @DELETE("Users/delete/{id}")
    Call<Void> deleteUser(@Path("id") Long id);

    @GET("/Users/alluser")
    Call<List<User>> getAllUser();





}
