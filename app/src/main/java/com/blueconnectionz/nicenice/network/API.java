package com.blueconnectionz.nicenice.network;

import com.blueconnectionz.nicenice.network.model.LoginReq;
import com.blueconnectionz.nicenice.network.model.ProfileInfo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {
    // Login functionality
    @POST("auth/login")
    Call<ResponseBody> login(@Body LoginReq request);

    @Multipart
    @POST("auth/register-driver")
    Call<ResponseBody> registerDriver(
            @Part("data1") Map<String,Object> user,
            @Part("data2") Map<String,Object> driver,
            @Part List<MultipartBody.Part> documents
    );

    @Multipart
    @POST("auth/register-owner")
    Call<ResponseBody> registerOwner(@Part("data1") Map<String, Object> userReq,
                                     @Part("data2") Map<String, Object> ownerReq,
                                     @Part MultipartBody.Part document);

    @GET("driver/{userId}/profile-info")
    Call<ResponseBody> getProfileInfo(@Path("userId") Long userId);

    @PUT("driver/{userId}/update-profile")
    Call<ResponseBody> updateProfileInfo(@Path("userId") Long userId, @Body ProfileInfo request);

    @GET("driver/all-cars")
    Call<ResponseBody> getAllCars();

    @PUT("driver/{carID}/{userID}/update-view")
    Call<ResponseBody> updateNumViews(@Path("carID") Long carID, @Path("userID") Long userID);

    @GET("owner/{userID}/drivers")
    Call<ResponseBody> getAllDrivers(@Path("userID") Long userID);

    @POST("owner/{userID}/{driverID}/connect-driver")
    Call<ResponseBody> connectWithDriver(@Path("userID") Long userID, @Path("driverID") Long driverID);

    @Multipart
    @POST("owner/{userID}/car-application")
    Call<ResponseBody> addNewCar(@Path("userID") Long userID,
                                 @Part("car") Map<String,Object> carDetails,
                                 @Part MultipartBody.Part document);
}
