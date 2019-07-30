package com.example.black.go_tankuser;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.black.go_tankuser.adapter.JamAdapter;
import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.Company;
import com.example.black.go_tankuser.model.JamModel;
import com.example.black.go_tankuser.model.PesanModel;
import com.example.black.go_tankuser.model.User;
import com.example.black.go_tankuser.service.CompanyService;
import com.example.black.go_tankuser.service.ReservasiService;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Reservasi extends AppCompatActivity {

    TextView namaCV, hargacv, namaUser, noHpUser;
    TextView etAlamat;
    ImageView iv_Company;
    ReservasiService api;

//    RecyclerView rv_jam;
    Spinner spinnerJam;
    LinearLayout ln_jam;
    TextView tv_jam;
    Button btn_pesan;


    Calendar myCalender;
    EditText txt_tgl;
    DatePickerDialog.OnDateSetListener date;

    ProgressDialog loading;
    Context context;

    private JamModel modeljam = new JamModel();
    private List<JamModel> jamModels = new ArrayList<>();
    private PesanModel mPesan = new PesanModel();

    public int JAM_ID;
    List<String> jam_name = new ArrayList<String>();
    List<Integer> jam_id = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasi);

        context = this;

        txt_tgl = (EditText) findViewById(R.id.date);
        txt_tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-Generate method stub
                new DatePickerDialog(Reservasi.this, date, myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        myCalender = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }
        };


        final Button cek = (Button) findViewById(R.id.btn_cek);
        cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String tgl = txt_tgl.getText().toString().trim();
                if (tgl.isEmpty()){
                    txt_tgl.setError("");
                    return;
                }else{
                    loadData(Integer.valueOf(getId()),tgl);
                    txt_tgl.setError(null);
                    btn_pesan.setVisibility(View.VISIBLE);
                }
            }
        });


        spinnerJam = findViewById(R.id.spinnerJam);
        spinnerJam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedName = parent.getItemAtPosition(position).toString();
                JAM_ID = jam_id.get(position);
//                Toast.makeText(context, "Kamu memilih CV " + selectedName + "id "+JAM_ID, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_pesan = findViewById(R.id.btn_pesan);
        btn_pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tgl = txt_tgl.getText().toString().trim();
                reservasi(Integer.valueOf(getId()),getUserId(),tgl, JAM_ID);
            }
        });

        initComponents();

        fetchDataCv();

        fetchDataUser();



    }


    private void reservasi(int company_id, int user_id, String tgl_pesan, int jam_id){

        api.pesan(company_id, user_id, tgl_pesan, jam_id).enqueue(new Callback<WrappedResponse<PesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PesanModel>> call, Response<WrappedResponse<PesanModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse<PesanModel> body = response.body();
                    if (body.getStatus() == 1){
                        mPesan = body.getData();
                        if(mPesan != null){
                            Intent intent = new Intent(Reservasi.this,Detail_histori.class);
                            intent.putExtra("ID",mPesan.getId());
                            startActivity(intent);
                            finish();
                            Toast.makeText(Reservasi.this,"Pesan Berhasil",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Reservasi.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Reservasi.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                }
//                mProgressBar.setVisibility(View.INVISIBLE);
//                btnRegister.setEnabled(true);
            }

            @Override
            public void onFailure(Call<WrappedResponse<PesanModel>> call, Throwable t) {
                Toast.makeText(Reservasi.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadData(int company_id, String date){

        api.getSemuaJam(company_id, date).enqueue(new Callback<WrappedListResponse<JamModel>>() {
            @Override
            public void onResponse(Call<WrappedListResponse<JamModel>> call, Response<WrappedListResponse<JamModel>> response) {
                if (response.isSuccessful()){
                    WrappedListResponse body = response.body();
                    if(body != null && body.getStatus() == 1){
                        jamModels = body.getData();
                        for (int i = 0; i < jamModels.size(); i++){
                            jam_name.add(jamModels.get(i).getJam());
                            jam_id.add(jamModels.get(i).getId());
                        }



                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_spinner_item, jam_name);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerJam.setAdapter(adapter);

                        tv_jam.setVisibility(View.VISIBLE);
                        ln_jam.setVisibility(View.VISIBLE);

                    }else {
                        //Belum ada data
                        Toast.makeText(Reservasi.this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("Gotank", response.message());
                    Toast.makeText(Reservasi.this, "Failed to retrieve data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedListResponse<JamModel>> call, Throwable t) {
                Log.d("Gotank", t.getMessage());
                Toast.makeText(Reservasi.this, "Cannot connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void initComponents(){
        namaCV = findViewById(R.id.tv_namaCV);
        hargacv = findViewById(R.id.tv_hargaCV);
        namaUser = findViewById(R.id.namaUser);
        noHpUser = findViewById(R.id.noHpUser);
        etAlamat = findViewById(R.id.alamatUser);
//        rv_jam = findViewById(R.id.rc_jam);
        ln_jam = findViewById(R.id.ln_jam);
        tv_jam = findViewById(R.id.tv_jam);
//        edt_jam = findViewById(R.id.edt_jam);

        iv_Company = (ImageView) findViewById(R.id.iv_company);
        api = ApiUtils.getReservasiService();
    }

//    String company_id = String.valueOf(getIntent().getIntExtra("ID", 0));

    private String getId(){
        return String.valueOf(getIntent().getIntExtra("ID", 0));
    }

    private int getUserId(){
        SharedPreferences sp = getSharedPreferences("TOKEN",MODE_PRIVATE);
        int id = sp.getInt("USER_ID", 0);
        return id;
    }

    private void fetchDataCv(){
        Call<WrappedResponse<Company>> request = api.showCompany(getId());
        request.enqueue(new Callback<WrappedResponse<Company>>() {
            @Override
            public void onResponse(Call<WrappedResponse<Company>> call, Response<WrappedResponse<Company>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        fillAll((Company) body.getData());
                    }
                }else {
                    Toast.makeText(Reservasi.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<Company>> call, Throwable t) {
                Toast.makeText(Reservasi.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchDataUser(){
        Call<WrappedResponse<User>> request = api.showUser(String.valueOf(getUserId()));
        request.enqueue(new Callback<WrappedResponse<User>>() {
            @Override
            public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        fillAllUser((User) body.getData());
                    }else{
                        Toast.makeText(Reservasi.this, "Status is not true", Toast.LENGTH_SHORT).show();
                    }
                }else { Toast.makeText(Reservasi.this, "Cannot get data from server", Toast.LENGTH_SHORT).show(); }
            }

            @Override
            public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                Toast.makeText(Reservasi.this, t.getMessage()+"", Toast.LENGTH_SHORT).show();
                Toast.makeText(Reservasi.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fillAllUser(User user){
        namaUser.setText(user.getName());
        noHpUser.setText(user.getPhone());
        etAlamat.setText(user.getAddress());

    }

    private void fillAll(Company company){
        namaCV.setText(company.getName());
        String hargaFix = money(company.getHarga());
        hargacv.setText("Rp.  "+hargaFix + "/Tangki");
        Glide.with(getApplicationContext())
                .load(ApiUtils.ENDPOINT+"img/"+company.getAvatar())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(iv_Company);

    }

    String money(int harga){
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = harga;
        String formattedNumber = formatter.format(myNumber);
        return  formattedNumber;
    }


    private void updateLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf =  new SimpleDateFormat(myFormat, Locale.US);
        txt_tgl.setText(sdf.format(myCalender.getTime()));
    }



//    public void btal_pesan(View view) {
//        Intent intent = new Intent(Reservasi.this,ListCvActivity.class);
//        startActivity(intent);
//    }
}
