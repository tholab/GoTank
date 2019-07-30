package com.example.black.go_tankuser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.black.go_tankuser.converter.WrappedListResponse;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.Driver;
import com.example.black.go_tankuser.model.HistoryModel;
import com.example.black.go_tankuser.model.PesanModel;
import com.example.black.go_tankuser.model.User;
import com.example.black.go_tankuser.service.HistoryService;
import com.example.black.go_tankuser.utils.ApiUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_histori extends AppCompatActivity {

    TextView namaCV, namaDriver, alamatPesan,biayaPesan, statusPesan, judulRekening, isiRekening;
    Button btn_upload_bukti;
    ImageView imageView, iv_histori;
    private EditText edt_komentar;
    private CardView rootUploadBukti,rootKomentar;
    private Button btn_posting, btn_batal;
    private PesanModel mPesan = new PesanModel();
    HistoryService api;

//    private AlertDialog.Builder dialogRattig;
//    private LayoutInflater inflater;
//    private View dialogView;

    public static final int REQUEST_CODE_CAMERA = 0012;
//    public static final int REQUEST_CODE_GALLERY = 0013;
    String urlPath;

//    private String [] items = {"Camera","Gallery","Cancel"};
    private String [] items = {"Camera","Cancel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_histori);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        btn_upload_bukti = (Button) findViewById(R.id.upload_bukti);
        imageView = (ImageView) findViewById(R.id.gambar_bukti);
        rootUploadBukti = findViewById(R.id.root_upload_bukti);
        rootKomentar = findViewById(R.id.root_komentar);
        btn_upload_bukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage2();
            }
        });

        btn_batal = findViewById(R.id.btn_batal);
        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String status = "Batal".trim();
                konfirmasiSelesai(getId(),status);
            }
        });
        btn_posting = (Button) findViewById(R.id.btn_posting);
        btn_posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateKomentar();
            }
        });

        initComponents();
        fetchDataHistori();
        Log.d("catatan",getId());
    }

    private void initComponents(){
        iv_histori = findViewById(R.id.iv_histori);
        namaCV = findViewById(R.id.namaCV_histori);
        namaDriver = findViewById(R.id.namaSupir_histori);
        alamatPesan = findViewById(R.id.alamat_histori);
        biayaPesan = findViewById(R.id.biaya_histori);
        statusPesan = findViewById(R.id.status_histori);
        judulRekening = findViewById(R.id.judul_rekening);
        isiRekening = findViewById(R.id.isi_rekening);
        edt_komentar = findViewById(R.id.et_komentar);
        api = ApiUtils.getHistoryService();
    }

    private String getId(){
        return String.valueOf(getIntent().getIntExtra("ID", 0));
    }

    private void konfirmasiSelesai(String id, String status){
        api.konfirmasiSelesai(id,status).enqueue(new Callback<WrappedResponse<PesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PesanModel>> call, Response<WrappedResponse<PesanModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse<PesanModel> body = response.body();
                    if (body.getStatus() == 1){
                        mPesan = body.getData();
                        if(mPesan != null){
                            Intent intent = new Intent(Detail_histori.this,HistoriActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(Detail_histori.this,"Pesan Dibatalkan",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Detail_histori.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Detail_histori.this,"Pesan Gagal",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<PesanModel>> call, Throwable t) {
                Toast.makeText(Detail_histori.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchDataHistori(){
        Call<WrappedResponse<HistoryModel>> request = api.detailHistory(getId());
        request.enqueue(new Callback<WrappedResponse<HistoryModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<HistoryModel>> call, Response<WrappedResponse<HistoryModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        if(body.getData() != null){
                            HistoryModel historyModel = (HistoryModel) body.getData();
                            fillDetailHistori(historyModel);
                        }
                    }
                }else {
                    Toast.makeText(Detail_histori.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<HistoryModel>> call, Throwable t) {
                Toast.makeText(Detail_histori.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fillDetailHistori(HistoryModel historyModel){

        namaCV.setText(historyModel.getName());
        Driver driver = historyModel.getDriver();
        if(driver == null){ namaDriver.setText("Belum ada driver"); }else{ namaDriver.setText(driver.getName()); }
        User u = historyModel.getUser();
        if(u == null){ alamatPesan.setText("Tidak dapat mengambil alamat pemesan"); }else { alamatPesan.setText(u.getAddress());}
        if(historyModel == null){
            biayaPesan.setText("Harga belum ditentukan");
        }else{
            biayaPesan.setText(String.valueOf("Rp. "+money(historyModel.getHarga()) + "/tangki"));
        }
        statusPesan.setText(historyModel.getStatus());
        if(historyModel.getStatus().equals("Belum Dibayar")){
            rootUploadBukti.setVisibility(View.VISIBLE);
            judulRekening.setVisibility(View.VISIBLE);
            isiRekening.setVisibility(View.VISIBLE);
        }else{
            rootUploadBukti.setVisibility(View.GONE);
            judulRekening.setVisibility(View.GONE);
            isiRekening.setVisibility(View.GONE);
        }

        if (historyModel.getStatus().equals("Selesai")){
           rootKomentar.setVisibility(View.VISIBLE);
           if (historyModel.getKomentar() == null){
               rootKomentar.setVisibility(View.VISIBLE);
           }else {
               rootKomentar.setVisibility(View.GONE);
           }
        }else {
            rootKomentar.setVisibility(View.GONE);
        }
        Glide.with(getApplicationContext())
                .load(ApiUtils.ENDPOINT+"img/"+historyModel.getAvatar())
                .placeholder(R.drawable.no_image)
                .error(R.drawable.no_image)
                .into(iv_histori);

    }

    String money(int harga){
        NumberFormat formatter = new DecimalFormat("#,###");
        double myNumber = harga;
        String formattedNumber = formatter.format(myNumber);
        return  formattedNumber;
    }


    private void selectImage2() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setIcon(R.mipmap.ic_gotankcher_round);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(items[i].equals("Camera")){
                    EasyImage.openCamera(Detail_histori.this,REQUEST_CODE_CAMERA);
                }

//                else if(items[i].equals("Gallery")){
//                    EasyImage.openGallery(Detail_histori.this, REQUEST_CODE_GALLERY);
//                }

                else if (items[i].equals("Cancel")){
                    dialog.dismiss();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        Glide.with(Detail_histori.this)
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f).centerCrop().into(imageView);
                        urlPath = imageFile.getAbsolutePath();
                        //uploadIMG(urlPath);
                        break;
//                    case REQUEST_CODE_GALLERY:
//                        Glide.with(Detail_histori.this)
//                                .load(imageFile)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .into(imageView);
//                        urlPath = imageFile.getAbsolutePath();
                        //Toast fungsi dibawah ini untuk menandakan bahwa data berbentuk path
//                        Toast.makeText(PengaturanActivity.this, ""+urlPath, Toast.LENGTH_SHORT).show();
//                        uploadIMG(urlPath);
//                        break;
                }
            }
        });
    }

    private void updateKomentar(){

        final EditText edt_komen = findViewById(R.id.et_komentar);

        Call<WrappedResponse<PesanModel>> request = api.updateUlasan(getId(),edt_komen.getText().toString());
        request.enqueue(new Callback<WrappedResponse<PesanModel>>() {
            @Override
            public void onResponse(Call<WrappedResponse<PesanModel>> call, Response<WrappedResponse<PesanModel>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        Toast.makeText(Detail_histori.this, "Terima kasih atas ulasan Anda", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(Detail_histori.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<PesanModel>> call, Throwable t) {

                Toast.makeText(Detail_histori.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void btn_kirim_upload(View view) {
        try{
            File file = new File(urlPath);
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath ());
            bitmap.compress (Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(file));
            RequestBody requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part photo =  MultipartBody.Part.createFormData("bukti_pembayaran",file.getName(),requestBodyForFile);
            Call<WrappedResponse<PesanModel>> req = api.uploadBukti(getId(), photo);
            req.enqueue(new Callback<WrappedResponse<PesanModel>>() {
                @Override
                public void onResponse(Call<WrappedResponse<PesanModel>> call, Response<WrappedResponse<PesanModel>> response) {
                    if(response.isSuccessful()){
                        WrappedResponse body = response.body();
                        if(body != null){
                            if(body.getStatus() == 1){
                                //update status ke belum dikonfirmasi
                                //api dll
                                Toast.makeText(Detail_histori.this, "Berhasil mengupload bukti pembayaran", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(Detail_histori.this, "Gagal mengupload..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        Toast.makeText(Detail_histori.this, "Response gagal", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WrappedResponse<PesanModel>> call, Throwable t) {
                    Log.d("black", t.getMessage());
                    Toast.makeText(Detail_histori.this, "Tidak dapat mengupload bukti pembayaran", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Kesalahan ", Toast.LENGTH_SHORT).show();
            System.err.println("black exception : "+ e.getMessage());
        }
    }

}
