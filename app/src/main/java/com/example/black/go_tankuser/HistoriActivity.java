package com.example.black.go_tankuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.black.go_tankuser.adapter.CompanyAdapter;
import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.model.PesanModel;
import com.example.black.go_tankuser.model.Product_histori;
import com.example.black.go_tankuser.adapter.HistoriAdapter;
import com.example.black.go_tankuser.service.HistoryService;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoriActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoriAdapter adapter;
    List<PesanModel> productHistoriList;
    private HistoryService historyService = ApiUtils.getHistoryService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);
        productHistoriList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewHistori);
        recyclerView.setHasFixedSize(true);
        //ini histori berdasarkan urautan yang terbaru masuk
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this)); -> recyle yang biasa, urutan yang lama diatas
        adapter = new HistoriAdapter(this,productHistoriList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private int getId(){
        SharedPreferences sp = getSharedPreferences("TOKEN",MODE_PRIVATE);
        int id = sp.getInt("USER_ID", 0);
        return id;
    }

    private void loadData(){
        String id = String.valueOf(getId());
        historyService.showHistory(id).enqueue(new Callback<WrappedListResponse<PesanModel>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<PesanModel>> call, Response<WrappedListResponse<PesanModel>> response) {
                if (response.isSuccessful()){
                    productHistoriList.clear();
                    WrappedListResponse body = response.body();
                    if(body != null && body.getStatus() == 1){
                        productHistoriList = body.getData();
                        recyclerView.setAdapter(new HistoriAdapter( HistoriActivity.this, productHistoriList));

                    }else {
                        //Belum ada data
                        Toast.makeText(HistoriActivity.this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(HistoriActivity.this, "Kesalahan saat mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<PesanModel>> call, Throwable t) {
                Log.d("GINK", t.getMessage());
                Toast.makeText(HistoriActivity.this, "Tidak dapat mengambil data dari server", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
