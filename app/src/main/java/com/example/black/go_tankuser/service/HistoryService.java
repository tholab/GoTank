package com.example.black.go_tankuser.service;

import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.HistoryModel;
import com.example.black.go_tankuser.model.PesanModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface HistoryService {
    @GET("pesan/{id}")
    Call<WrappedListResponse<PesanModel>> showHistory(@Path("id") String id);

    @GET("pesan/detail/{id}")
    Call<WrappedResponse<HistoryModel>> detailHistory(@Path("id") String id);

    @Multipart
    @POST("pesan/bukti/{id}")
    Call<WrappedResponse<PesanModel>> uploadBukti(@Path("id") String id, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @PUT("pesan/{id}")
    Call<WrappedResponse<PesanModel>> updateUlasan(@Path("id") String id, @Field("komentar") String komentar);

    @FormUrlEncoded
    @POST("pesan/konfirmasi/{id}")
    Call<WrappedResponse<PesanModel>> konfirmasiSelesai(@Path("id") String id, @Field("status") String status);

}
