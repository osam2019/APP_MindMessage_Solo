package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.util.Log;
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

public class ListActivity extends AppCompatActivity {

    TextView txt;
    int MN, status, permission;
    String id_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        txt = (TextView)findViewById(R.id.text2_txt);

        MN = getIntent().getIntExtra("position", -1);
        status = getIntent().getIntExtra("status", -1);
        permission = getIntent().getIntExtra("permission", 0);
        id_ = getIntent().getStringExtra("id_");


        Log.d("sss", MN + " " + status + " " + permission + " " + id_);
        GetMsg getMsg = new GetMsg();
        getMsg.start();

    }

    class GetMsg extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/download.jsp");

                FormBody.Builder b = new FormBody.Builder();
                b.add("t",MN+"");
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
                    final String s = root.getJSONObject(0).getString("text_");
                    final String s2 = root.getJSONObject(0).getString("name");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(" " + s +  "\r\n\r\n 작성자: " + s2);
                        }
                    });
                }
                else{
                    Log.d("sss", "error");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {

        }
    }

    public void backBtn(View view) {
        finish();
    }

    public void replyBtn(View view) {
        if (status == 4 || status == 5) {
            Intent intent = new Intent(ListActivity.this, ReplyActivity.class);
            intent.putExtra("MN", MN);
            startActivity(intent);
        }
        else {
            if (permission == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                Toast.makeText(ListActivity.this, "답장이 오지 않았습니다.", Toast.LENGTH_SHORT).show();
                Log.d("sss", "sdf");
            }
            else {
                Intent intent = new Intent(ListActivity.this, WriteReplyActivity.class);
                intent.putExtra("MN", MN);
                intent.putExtra("id_", id_);
                startActivity(intent);
                finish();
            }
        }
    }
}
