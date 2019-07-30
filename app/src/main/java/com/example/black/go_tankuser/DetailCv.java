package com.example.black.go_tankuser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.black.go_tankuser.adapter.ComentUserAdapter;
import com.example.black.go_tankuser.adapter.HistoriAdapter;
import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.ComentUser;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.model.KomentarModel;
import com.example.black.go_tankuser.service.CompanyService;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCv extends AppCompatActivity {
    private TextView name, alamat, deskripsi, harga;
    private ImageView iv_avatar;
    private CompanyService api;
    private Button btn_booking;
    public CardView cv_komen;
    public int t_ada = 0, t_null = 0;

    RecyclerView rvComent;
    ComentUserAdapter adapter;

    private RecyclerView recyclerView;
    private List<KomentarModel> comentUserList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cv);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cv_komen = findViewById(R.id.cv_komen);
        recyclerView = findViewById(R.id.rv_coment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initComponents();
        fetchData();

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadDataKomen();
    }

    private void loadDataKomen(){
        Call<WrappedListResponse<KomentarModel>> request = api.showKomentar(getId());
        request.enqueue(new Callback<WrappedListResponse<KomentarModel>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<KomentarModel>> call, Response<WrappedListResponse<KomentarModel>> response) {
                if (response.isSuccessful()){
                    WrappedListResponse body = response.body();
                    if(body != null && body.getStatus() == 1){
                        comentUserList = body.getData();
                        recyclerView.setAdapter(new ComentUserAdapter(comentUserList, DetailCv.this));
                        setParsing();
                        if (comentUserList.size()==0){
                            cv_komen.setVisibility(View.GONE);
                        }
                    }else {
                        //Belum ada data
                        Toast.makeText(DetailCv.this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("Gotank", response.message());
                    Toast.makeText(DetailCv.this, "Failed to retrieve data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<KomentarModel>> call, Throwable t) {

                Log.d("Gotank", t.getMessage());
                Toast.makeText(DetailCv.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setParsing(){
        for (int i=0;i<comentUserList.size();i++){
            KomentarModel komentarModel = comentUserList.get(i);
            if (komentarModel.getKomentar()!=null){
                stt_comment(true);
            } else {
                stt_comment(false);
            }
        }
    }

    public void stt_comment(boolean stt){
        if (stt){
            t_ada++;
            Log.d("catatan","ada");
        } else{
            t_null++;
            Log.d("catatan","tidak ada");
        }
        Log.d("catatan","ada="+t_ada+", tidak ada="+t_null);
        if (t_ada<=0){
            cv_komen.setVisibility(View.GONE);
        }
    }


    private void initComponents(){
        name = findViewById(R.id.tv_name);
        harga = findViewById(R.id.tv_harga);
        alamat = findViewById(R.id.tv_alamat);
        deskripsi = findViewById(R.id.tv_deskripsi);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        btn_booking = (Button) findViewById(R.id.boking);
        rvComent = (RecyclerView) findViewById(R.id.rv_coment);
        api = ApiUtils.getCompanyService();
    }

    private String getId(){
        return String.valueOf(getIntent().getIntExtra("ID", 0));
    }

    private void fetchData(){
        Call<WrappedResponse<Company>> request = api.showById(getId());
        request.enqueue(new Callback<WrappedResponse<Company>>() {
            @Override
            public void onResponse(Call<WrappedResponse<Company>> call, Response<WrappedResponse<Company>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        fillAll((Company) body.getData());
                    }
                }else {
                    Toast.makeText(DetailCv.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<Company>> call, Throwable t) {
                Toast.makeText(DetailCv.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fillAll(final Company company){
        name.setText(company.getName());
        String hargaFix = money(company.getHarga());
        harga.setText("Rp.  "+hargaFix + "/Tangki");
        alamat.setText(company.getAddress());
        deskripsi.setText(company.getDescription());
        Glide.with(getApplicationContext())
                .load(ApiUtils.ENDPOINT+"img/"+company.getAvatar())
                  .placeholder(R.drawable.no_image)
                  .error(R.drawable.no_image)
                .into(iv_avatar);

        btn_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailCv.this, Reservasi.class);
                intent.putExtra("ID", company.getId());
                startActivity(intent);
            }
        });
    }

    String money(int harga){
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = harga;
        String formattedNumber = formatter.format(myNumber);
        return  formattedNumber;
    }

}
