package com.gitreader.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gitreader.R;
import com.gitreader.database.DBHelper;

/**
 * Created by admin on 11/13/2017.
 */

public class SplashActivity extends AppCompatActivity {
    ProgressBar progressBar;
    TextView txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.notificatrion));
        }
        setContentView(R.layout.splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(2f);

        txtMessage = (TextView) findViewById(R.id.txt);


        new CountDownTimer(5000, 25) {
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressBar.getProgress() + 1);
                if (progressBar.getProgress() == 100) {
                    cancel();
                    DBHelper dbHelper = new DBHelper(SplashActivity.this);
                    if (dbHelper.getBookCount() > 0) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, LandingActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

            public void onFinish() {
            }
        }.start();
    }
}
