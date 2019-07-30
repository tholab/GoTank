package com.example.black.go_tankuser;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.User;
import com.example.black.go_tankuser.service.UserService;
import com.example.black.go_tankuser.utils.ApiUtils;
import com.example.black.go_tankuser.utils.PrefManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private UserService userService = ApiUtils.getUserService();
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private SharedPreferences settings;
    private User mUser = new User();
    private static final String TAG = "LoginActivity";
    private ProgressBar mProgressBar;
    PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initComp();

        prefManager = new PrefManager(this);
        requestPermissions();

        doLogin();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (!isNotLoggedIn()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void initComp(){
        etEmail = findViewById(R.id.et_Email);
        etPassword= findViewById(R.id.et_Password);
        mProgressBar =findViewById(R.id._loader);
        btnLogin = findViewById(R.id.btn_login);
        settings = getSharedPreferences("TOKEN",MODE_PRIVATE);
    }

    private void doLogin(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = etEmail.getText().toString().trim();
                String p = etPassword.getText().toString().trim();
                String t = prefManager.getTokenGcm().toString().trim();
                if(e.isEmpty()){
                    etEmail.setError("Email harus diisi");
                    etEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
                    etEmail.setError("Email tidak Valid");
                    etEmail.requestFocus();
                    return;
                }
                if(p.isEmpty()){
                    etPassword.setError("Password harus diisi");
                    etPassword.requestFocus();
                    return;
                }
                if(p.length() < 8 ){
                    etPassword.setError("Password harus minimal 8 character");
                    etPassword.requestFocus();
                    return;
                }

                    mProgressBar.setVisibility(View.VISIBLE);
                    btnLogin.setEnabled(false);
                    Call<WrappedResponse<User>> user = userService.login(e,p,t);//di kirim maring server
                    user.enqueue(new Callback<WrappedResponse<User>>() {
                        @Override
                        public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                            if (response.isSuccessful()){
                                WrappedResponse<User> body = response.body();
                                if (body.getStatus() == 1){
                                    mUser = body.getData();
                                    if (mUser != null){
                                        System.out.println(mUser.getApi_token());
                                        setLoggedIn(mUser.getApi_token(), mUser.getId(), mUser.getAvatar());
                                        Toast.makeText(LoginActivity.this,body.getMessage(),Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(LoginActivity.this,"Response success with error", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(LoginActivity.this,body.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            }else {
                                Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
                            }
                            mProgressBar.setVisibility(View.INVISIBLE);
                            btnLogin.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                            Toast.makeText(LoginActivity.this,"Cannot request to server. Please check your connection",Toast.LENGTH_SHORT).show();
                            System.err.println(t.getMessage());
                            mProgressBar.setVisibility(View.INVISIBLE);
                            btnLogin.setEnabled(true);

                        }
                    });
            }
        });
    }

    private boolean isNotLoggedIn(){
        String token = settings.getString("TOKEN","UNDEFINED");
        return token == null || token.equals("UNDEFINED");
    }

    private void setLoggedIn (String token, int user_id, String avatar){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("TOKEN",token);
        editor.putInt("USER_ID", user_id);
        editor.putString("AVATAR", avatar);
        editor.putString("token_gcm", settings.getString("token_gcm",""));
        editor.commit();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    public void btn_register(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }


    private void requestPermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {openMultipleThings();}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
        }).check();
    }

    private void openMultipleThings(){
//        Toast.makeText(this,"Sukses",Toast.LENGTH_LONG).show();
    }
}
