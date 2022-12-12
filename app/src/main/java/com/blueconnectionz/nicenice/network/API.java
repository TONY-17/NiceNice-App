package com.blueconnectionz.nicenice.network;

import com.blueconnectionz.nicenice.network.model.LoginReq;
import com.blueconnectionz.nicenice.network.model.ProfileInfo;

import java.util.List;

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

public interface API {
    // Login functionality
    @POST("auth/login")
    Call<ResponseBody> login(@Body LoginReq request);

    @Multipart
    @POST("auth/register-driver")
    Call<ResponseBody> registerDriver(
            @Part("data1") String user,
            @Part("data2") String driver,
            @Part List<MultipartBody.Part> documents
    );

    @Multipart
    @POST("auth/register-owner")
    Call<ResponseBody> registerOwner(@Part("data1") String userReq, @Part("data2") String ownerReq, @Part MultipartBody.Part document);

    @GET("driver/{userId}/profile-info")
    Call<ResponseBody> getProfileInfo(@Path("userId") Long userId);

    @PUT("driver/{userId}/update-profile")
    Call<ResponseBody> updateProfileInfo(@Path("userId") Long userId, @Body ProfileInfo request);

    @GET("driver/all-cars")
    Call<ResponseBody> getAllCars();

    @PUT("driver/{carID}/{userID}/update-view")
    Call<ResponseBody> updateNumViews(@Path("carID") Long carID, @Path("userID") Long userID);

    @GET("admin/drivers")
    Call<ResponseBody> getAllDrivers();

    @POST("owner/{userID}/{driverID}/connect-driver")
    Call<ResponseBody> connectWithDriver(@Path("userID") Long userID, @Path("driverID") Long driverID);
}
