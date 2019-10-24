package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {

    int prikey;
    String id_;
    Button msgBox;
    int invisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent i = getIntent();
        prikey = i.getIntExtra("index", -1);
        id_ = i.getStringExtra("id");
        msgBox = (Button)findViewById(R.id.msgBox_btn);

        invisible = View.INVISIBLE;
        SetInfo setInfo = new SetInfo();
        setInfo.start();

    }


    public void logoutBtn(View view) {
        Intent i = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    public void InfoBtn(View view) {
        Intent i = new Intent(MenuActivity.this, InfoActivity.class);
        i.putExtra("index", prikey);
        i.putExtra("id", id_);
        startActivity(i);
        this.finish();
    }
    public void writeBtn(View view) {
        Intent intent = new Intent(MenuActivity.this, WriteActivity.class);
        intent.putExtra("id", id_);
        intent.putExtra("index", prikey);
        startActivity(intent);
        this.finish();
    }
    public void ReadBtn(View v) {
        Intent intent = new Intent(MenuActivity.this, ReadActivity.class);
        intent.putExtra("id", id_);
        intent.putExtra("index", prikey);
        startActivity(intent);
        this.finish();
    }

    class SetInfo extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/get.jsp");

                FormBody.Builder b = new FormBody.Builder();
                b.add("id", id_);
                FormBody body = b.build();
                builder = builder.post(body);
                Request request = builder.build();

                Callback1 callback1 = new Callback1();
                Call call = client.newCall(request);
                call.enqueue(callback1);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Callback1 implements Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();

                JSONArray root = new JSONArray(result);
                if (root.length() == 1) {
                    final int p = root.getJSONObject(0).getInt("permission");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (p == 0) {
                                msgBox.setText("내가 쓴 메세지");
                            }
                            else {
                                msgBox.setText("받은 메세지");
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {

        }
    }
}
