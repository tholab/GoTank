package com.example.black.go_tankuser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class UbahAkunActivity extends AppCompatActivity {

    Button btn_ubahAkun;
    CardView root_ubahAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_akun);
        btn_ubahAkun = findViewById(R.id.btn_ubah);
        root_ubahAkun = findViewById(R.id.root_ubahAkun);
        btn_ubahAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root_ubahAkun.setVisibility(View.VISIBLE);
            }
        });
    }
}
