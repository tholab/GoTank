package com.example.black.go_tankuser.service;

import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.model.JamModel;
import com.example.black.go_tankuser.model.PesanModel;
import com.example.black.go_tankuser.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservasiService {

    @GET("company/{id}")
    Call<WrappedResponse<Company>> showCompany(@Path("id") String id);

    @GET("user/{id}")
    Call<WrappedResponse<User>> showUser(@Path("id") String id);

//    @GET("showJam")
//    Call<WrappedListResponse<JamModel>> all();

    @GET("pesan/getjam")
    Call<WrappedListResponse<JamModel>> getSemuaJam(@Query("company_id") int company_id, @Query("date") String date);

    @FormUrlEncoded
    @POST("pesan")
    Call<WrappedResponse<PesanModel>> pesan(@Field("company_id") int company_id,
                                            @Field("user_id") int user_id,
                                            @Field("tgl_pesan") String tgl_pesan,
                                            @Field("jam_id") int jam_id);
}
