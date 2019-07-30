package com.example.black.go_tankuser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.black.go_tankuser.converter.WrappedResponse;
import com.example.black.go_tankuser.model.User;
import com.example.black.go_tankuser.service.UserService;
import com.example.black.go_tankuser.utils.ApiUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView pagetitle, pagesubtitle;
    private TextView nameuser;
    private CircleImageView iv_User;
    private UserService api;

    Animation atg, atgtwo, atgthree;
    ImageView imageView3;
    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("TOKEN",MODE_PRIVATE);

        atg = AnimationUtils.loadAnimation(this, R.anim.atg);
        atgtwo = AnimationUtils.loadAnimation(this, R.anim.atgtwo);
        atgthree = AnimationUtils.loadAnimation(this, R.anim.atgthree);

        iv_User = findViewById(R.id.imageView2);
        nameuser = findViewById(R.id.nameuser);
        api = ApiUtils.getUserService();


        imageView3 = findViewById(R.id.imageView3);
        pagetitle = findViewById(R.id.pagetitle);
        pagesubtitle = findViewById(R.id.pagesubtitle);

        // pass an animation
        imageView3.startAnimation(atg);

        pagetitle.startAnimation(atgtwo);
        pagesubtitle.startAnimation(atgtwo);

        fecthData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (isNotLoggedIn()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
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
                    Toast.makeText(MainActivity.this, "Cannot get data from server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WrappedResponse<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Something went wrong with error code"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fillAll(User user){
       nameuser.setText(user.getName());
        Glide.with(getApplicationContext()).load(ApiUtils.ENDPOINT+"img/"+user.getAvatar()).into(iv_User);


    }


    private boolean isNotLoggedIn() {
        String token = settings.getString("TOKEN","UNDEFINED");
        return token == null || token.equals("UNDEFINED");

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }

    public void btn_list(View view) {
        Intent intent = new Intent(MainActivity.this,ListCvActivity.class);
        startActivity(intent);
    }

    public void btn_profile(View view) {
        Intent intent = new Intent(MainActivity.this,PengaturanActivity.class);
        startActivity(intent);
    }

    public void btn_histori(View view) {
        Intent intent = new Intent(MainActivity.this,HistoriActivity.class);
        startActivity(intent);
    }

    public void btn_bantuan(View view) {
        Intent intent = new Intent(MainActivity.this,BantuanActivity.class);
        startActivity(intent);
    }

}
