package me.dlovan.duhokhotelsguider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;

public class Intro extends AppCompatActivity {

    //ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
           // progressBar = findViewById(R.id.progressBar1);
           // progressBar.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // progressBar.setVisibility(View.GONE);
                Intent startActivity = new Intent(Intro.this, MainPage.class);
                startActivity(startActivity);
                finish();
            }
        }, 1000);
    }
}
