package com.wikav.student.studentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wikav.student.studentapp.MainActivties.HomeMenuActivity;

public class NoInternetActivity extends AppCompatActivity {
Config config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        config = new Config(this);
    }

    public void retry(View view) {
        if(config.haveNetworkConnection())
        {
            Intent intent = new Intent(NoInternetActivity.this, HomeMenuActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
}
