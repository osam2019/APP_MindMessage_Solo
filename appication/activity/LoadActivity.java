package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;

public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        t tt = new t(this);
        tt.start();
    }
    class t extends Thread {
        Activity a;
        t(Activity aa) {
            a = aa;
        }

        @Override
        public void run() {
            super.run();
            try {
                sleep(2000);

            }catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(a, MainActivity.class);
            startActivity(intent);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadActivity.this.finish();
                }
            });
        }
    }
}
