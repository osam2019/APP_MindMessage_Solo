package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InfoActivity extends AppCompatActivity {

    TextView name, id, permission;
    int idx;
    String id_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        idx = getIntent().getIntExtra("index", -1);
        id_ = getIntent().getStringExtra("id");

        name = (TextView)findViewById(R.id.name_txt);
        id = (TextView)findViewById(R.id.id_txt);
        permission = (TextView)findViewById(R.id.permission_txt);

        SetInfo setInfo = new SetInfo();
        setInfo.start();
    }

    public void okBtn(View v) {
        Intent i = new Intent(InfoActivity.this, MenuActivity.class);
        i.putExtra("id", id_);
        i.putExtra("index", idx);
        startActivity(i);
        finish();
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

                final int n = root.length();
                final String s1 = root.getJSONObject(0).getString("name_");
                final String s2 = root.getJSONObject(0).getString("id_");
                final int i1 = root.getJSONObject(0).getInt("permission");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (n == 1) {
                            name.setText(s1);
                            id.setText(s2);
                            permission.setText(i1+"");
                        }
                        else {
                            Toast t = Toast.makeText(InfoActivity.this, "존재하지 않는 계정입니다.", Toast.LENGTH_LONG);
                            t.show();
                        }
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {

        }
    }
}
