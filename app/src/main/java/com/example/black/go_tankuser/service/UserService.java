package com.example.black.go_tankuser.service;

import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {

    @FormUrlEncoded
    @POST("user/login")
    Call<WrappedResponse<User>> login(@Field("email") String email, @Field("password") String password, @Field("api_token_gcm") String api_token_gcm);//kie ngirime namae api_token_gcm


    @FormUrlEncoded
    @POST("user/register")
    Call<WrappedResponse<User>> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @GET("user/{id}")
    Call<WrappedResponse<User>> showById(@Path("id") String id);

    @FormUrlEncoded
    @PUT("user/{id}")
    Call<WrappedResponse<User>> updateProfile(@Path("id") String id, @Field("name") String name);

    @Multipart
    @POST("user/image/{id}")
    Call<WrappedResponse<User>> updateImage(@Path("id") String id, @Part MultipartBody.Part image);
}
