package com.example.black.go_tankuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.black.go_tankuser.R;

public class ProfilActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PrefManager prefManager = new PrefManager(getApplicationContext());
//                prefManager.setIsFirstTimeLaunch(true);
//                startActivity(new Intent(ProfilActivity.this, IntroActivity.class));
//
//       finish();
//
//
//            }
//        });
    }


}
