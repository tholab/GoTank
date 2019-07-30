package com.example.black.go_tankuser.service;

import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.model.KomentarModel;
import com.example.black.go_tankuser.model.PesanModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CompanyService {
    @GET("company")
    Call<WrappedListResponse<Company>> all();

    @GET("company/{id}")
    Call<WrappedResponse<Company>> showById(@Path("id") String id);

    @GET("company/komentar/{id}")
    Call<WrappedListResponse<KomentarModel>> showKomentar(@Path("id") String id);

    @FormUrlEncoded
    @POST("company/search")
    Call<WrappedListResponse<Company>> search (@Field("search") String search
    );
}
