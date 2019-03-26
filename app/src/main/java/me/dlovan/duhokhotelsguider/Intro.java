package me.dlovan.duhokhotelsguider;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

public class Intro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ConnectionDetector2 cd = new ConnectionDetector2(this);
        if (cd.isConnected()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent startActivity = new Intent(Intro.this, MainPage.class);
                    startActivity(startActivity);
                    finish();
                }
            }, 3000);
        } else {
            Toast.makeText(getApplicationContext(), R.string.app_name +"Says: There is no internet connection!.Please connect to internet", Toast.LENGTH_LONG).show();

        }
    }

    public class ConnectionDetector2 {
        private Context context;
        public ConnectionDetector2(Context context) { this.context = context; }
        public boolean isConnected() {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
            if ( connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
