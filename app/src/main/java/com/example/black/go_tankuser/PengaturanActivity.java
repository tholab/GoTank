package com.example.black.go_tankuser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.User;
import com.example.black.go_tankuser.service.UserService;
import com.example.black.go_tankuser.utils.ApiUtils;
import com.example.black.go_tankuser.utils.PrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PengaturanActivity extends AppCompatActivity {
    private TextView namePP, emailPP, hpPP, alamatPP;
    private CircleImageView iv_PP;
    private UserService api;
    private EditText edt_name;
    private BottomSheetDialog mbottomSheetDialog;
    String urlPath;
    public static final int REQUEST_CODE_CAMERA = 0012;
    public static final int REQUEST_CODE_GALLERY = 0013;
    private EasyImage easyImage;

    private String [] items = {"Camera","Gallery","Cancel"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //easyImage = new EasyImage.Builder(PengaturanActivity.this).allowMultiple(false).build();
        // enable home button untuk navigasi
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        LinearLayout ln_name = (LinearLayout) findViewById(R.id.ln_nama);
        ln_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfileName();
            }
        });

        initComponents();

        fecthData();

    }

    private void initComponents() {
        namePP = findViewById(R.id.tv_nama_profil);
        emailPP = findViewById(R.id.tv_email_profil);
        hpPP = findViewById(R.id.tv_hp_profil);
        alamatPP = findViewById(R.id.tv_alamat_profil);
        iv_PP = findViewById(R.id.iv_profile);
        edt_name = findViewById(R.id.et_EdtNama);
        mbottomSheetDialog = new BottomSheetDialog(PengaturanActivity.this);
        api = ApiUtils.getUserService();
    }


    private void SelectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setIcon(R.mipmap.ic_gotankcher_round);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if(items[i].equals("Camera")){
                    EasyImage.openCamera(PengaturanActivity.this,REQUEST_CODE_CAMERA);
                }else if(items[i].equals("Gallery")){
                    EasyImage.openGallery(PengaturanActivity.this, REQUEST_CODE_GALLERY);
                }else if (items[i].equals("Cancel")){
                    dialog.dismiss();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        Glide.with(PengaturanActivity.this)
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .thumbnail(0.5f).centerCrop().into(iv_PP);
                        urlPath = imageFile.getAbsolutePath();
                        uploadIMG(urlPath);
                        break;
                    case REQUEST_CODE_GALLERY:
                        Glide.with(PengaturanActivity.this)
                                .load(imageFile)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(iv_PP);
                        urlPath = imageFile.getAbsolutePath();
                        //Toast fungsi dibawah ini untuk menandakan bahwa data berbentuk path
//                        Toast.makeText(PengaturanActivity.this, ""+urlPath, Toast.LENGTH_SHORT).show();
                        uploadIMG(urlPath);
                        break;
                }
            }
        });
    }




    private void uploadIMG(String url){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Tunggu sebentar...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        File file = new File(url);
        Toast.makeText(this, "Sebelum dicompress "+file.length(), Toast.LENGTH_SHORT).show();
        System.out.println("Sebelum dicompress : " + file.length());
        try{
            Bitmap bitmap = BitmapFactory.decodeFile (file.getPath());
            bitmap.compress (Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(file));
            Toast.makeText(this, "After compressed : "+file.length(), Toast.LENGTH_SHORT).show();
            System.out.println("Sesudah dicompress : " + file.length());
        }catch (Exception e){
            Toast.makeText(this, "Kesalahan saat mengcompress file", Toast.LENGTH_SHORT).show();
            System.err.println("ydhnwb : "+ e.getMessage());
        }

        RequestBody requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photo =  MultipartBody.Part.createFormData("avatar",file.getName(),requestBodyForFile);


        Call<WrappedResponse<User>> request = api.updateImage(String.valueOf(getIdUser()), photo);
        request.enqueue(new Callback<WrappedResponse<User>>() {
            @Override
            public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                pDialog.dismiss();
                Log.d("catatan",response.toString());
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        Toast.makeText(PengaturanActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        finish();
                    }
                }else {
                    pDialog.dismiss();
                    Toast.makeText(PengaturanActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(PengaturanActivity.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateProfileName() {
        final View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_profile_nama,null);
        mbottomSheetDialog.setContentView(dialogLayout);
        mbottomSheetDialog.show();

        final EditText edt_name = dialogLayout.findViewById(R.id.et_EdtNama);

        TextView tv_simpan =  dialogLayout.findViewById(R.id.tv_simpan);
        tv_simpan.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Call<WrappedResponse<User>> request = api.updateProfile(
                       String.valueOf(getIdUser()),
                       edt_name.getText().toString()

               );

               request.enqueue(new Callback<WrappedResponse<User>>() {
                   @Override
                   public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                       if (response.isSuccessful()){
                           WrappedResponse body = response.body();
                           if (body.getStatus() == 1){
                               Toast.makeText(PengaturanActivity.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                               finish();
                           }
                       }else {
                           Toast.makeText(PengaturanActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                       Toast.makeText(PengaturanActivity.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

                   }
               });

//                namePP.setText(edt_nama.getText().toString());
//                mbottomSheetDialog.dismiss();
            }
        });

        TextView tv_batal = (TextView) dialogLayout.findViewById(R.id.tv_batal);
        tv_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbottomSheetDialog.dismiss();
            }
        });

    }





    private int getIdUser(){
        SharedPreferences sp = getSharedPreferences("TOKEN",MODE_PRIVATE);
        int id = sp.getInt("USER_ID", 0);
        return id;
    }

    private void fecthData() {
        Call<WrappedResponse<User>> request = api.showById(String.valueOf(getIdUser()));
        request.enqueue(new Callback<WrappedResponse<User>>() {
            @Override
            public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                if (response.isSuccessful()){
                    WrappedResponse body = response.body();
                    if (body.getStatus() == 1){
                        fillAll((User) body.getData());
                    }
                }else {
                    Toast.makeText(PengaturanActivity.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                Toast.makeText(PengaturanActivity.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fillAll(User user){
        namePP.setText(user.getName());
        emailPP.setText(user.getEmail());
        hpPP.setText(user.getPhone());
        alamatPP.setText(user.getAddress());
        Glide.with(getApplicationContext()).load(ApiUtils.ENDPOINT+"img/"+user.getAvatar()).into(iv_PP);
    }


    public void btn_logOut(View view) {
        SharedPreferences settings = getSharedPreferences("TOKEN", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        finish();
    }

    public void btn_ubahAkun(View view) {
        Intent intent = new Intent(PengaturanActivity.this,UbahAkunActivity.class);
        startActivity(intent);
    }
}
