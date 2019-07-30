package com.example.black.go_tankuser;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.User;
import com.example.black.go_tankuser.service.UserService;
import com.example.black.go_tankuser.utils.ApiUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private EditText etNamaUser, etEmail, etNo_Hp, etPassword, etAddress;
    private Button btnRegister;
    private UserService userService = ApiUtils.getUserService();
    private SharedPreferences settings;
    private User mUser = new User();
    private static final String TAG = "RegisterActivity";
    private ProgressBar mProgressBar;

    String alamat;
    double latitude ,longitude;
    GoogleMap mMap;
    SupportMapFragment mapFragment;
    View customView;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().hide();
//        setContentView(R.layout.activity_maps_activtiy);

        etNamaUser = findViewById(R.id.etNamaUser);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etNo_Hp = findViewById(R.id.etNo_Hp);
        etPassword = findViewById(R.id.etPassword);
        mProgressBar = findViewById(R.id.loader);
        settings = getSharedPreferences("TOKEN",MODE_PRIVATE);

        btnRegister = findViewById(R.id.btn_regis);
        doRegister();

        customView = (View) findViewById(R.id.customView);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nsv_regis);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        nestedScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        nestedScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        if (!isNotLoggedIn()){
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }
    }
    private void doRegister(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaUser = etNamaUser.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String no_hp = etNo_Hp.getText().toString().trim();
                String address = etAddress.getText().toString().trim();
                String _latitude = String.valueOf(latitude).toString();
                String _longitude = String.valueOf(longitude).toString();

                if(namaUser.isEmpty()){
                    etNamaUser.setError("Nama harus diisi");
                    etNamaUser.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    etEmail.setError("Email harus diisi");
                    etEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.setError("Email tidak Valid");
                    etEmail.requestFocus();
                    return;
                }

                if(no_hp.isEmpty()){
                    etNo_Hp.setError("No.Hp harus diisi");
                    etNo_Hp.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    etPassword.setError("Password harus diisi");
                    etPassword.requestFocus();
                    return;
                }
                if(password.length() < 8 ){
                    etPassword.setError("Password harus minimal 8 character");
                    etPassword.requestFocus();
                    return;
                }
                if(address.isEmpty()){
                    etAddress.setError("Alamat harus diisi");
                    etAddress.requestFocus();
                    return;
                }

                    mProgressBar.setVisibility(View.VISIBLE);
                    btnRegister.setEnabled(false);
                    Call<WrappedResponse<User>> user_ = userService.register(namaUser, email,password, no_hp, address, _latitude, _longitude);
                    user_.enqueue(new Callback<WrappedResponse<User>>() {
                        @Override
                        public void onResponse(Call<WrappedResponse<User>> call, Response<WrappedResponse<User>> response) {
                            if (response.isSuccessful()){
                                WrappedResponse<User> body = response.body();
                                if (body.getStatus() == 1){
                                    mUser = body.getData();
                                    if(mUser != null){
//                                        Log.d(TAG, mUser.getApi_token());
//                                        setLoggedIn(mUser.getApi_token(), mUser.getId());
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        Toast.makeText(RegisterActivity.this,"Silahkan Konfirmasi Email Anda",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(RegisterActivity.this,"Response succees with error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this,"Register Gagal",Toast.LENGTH_SHORT).show();
                            }
                            mProgressBar.setVisibility(View.INVISIBLE);
                            btnRegister.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                            Log.d(TAG, "tholabul ilmi"+t.getMessage());
                            mProgressBar.setVisibility(View.INVISIBLE);
                            btnRegister.setEnabled(true);
                            Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                        }
                    });
            }
        });
    }

    private boolean isNotLoggedIn(){
        String token = settings.getString("TOKEN","UNDEFINED");
        return token == null || token.equals("UNDEFINED");
    }


//    private void setLoggedIn (String token, int user_id){
//        SharedPreferences.Editor editor = settings.edit();
//        editor.putString("TOKEN", token);
//        editor.putInt("USER_ID", user_id);
//        editor.commit();
//        finish();
//
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double latitude = -6.895558;
        double longitude = 109.168228;
        LatLng point = new LatLng(latitude, longitude);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(15).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        latitude    = latLng.latitude;
        longitude   = latLng.longitude;
        alamat =  ApiUtils.getAddressSimple(latitude, longitude, this);
        MarkerOptions marker = new MarkerOptions().position(new LatLng(latLng.latitude, latLng.longitude))
                .title("Lokasi saya").snippet(alamat);
        mMap.clear();
        mMap.addMarker(marker);
        etAddress.setText(alamat);
        Log.d("catatan",String.valueOf(latitude)+"==="+alamat);
    }
}
