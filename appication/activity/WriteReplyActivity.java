package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class WriteReplyActivity extends AppCompatActivity {

    EditText txt;
    int MN;
    String id_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_reply);

        txt = (EditText) findViewById(R.id.text_ext_r);
        MN = getIntent().getIntExtra("MN", -1);
        id_ = getIntent().getStringExtra("id_");


    }

    public void backBtn(View view) {
        Intent i = new Intent(WriteReplyActivity.this, ListActivity.class);
        i.putExtra("position", MN);
        i.putExtra("status", 1);
        i.putExtra("permission", 1);
        i.putExtra("id_", id_) ;
        startActivity(i);
        finish();
    }

    public void sendBtn(View view) {
        TextThread textThread = new TextThread();
        textThread.start();
    }

    class TextThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/reply_upload.jsp");

                String text_ = txt.getText().toString();

                FormBody.Builder builder2 = new FormBody.Builder();

                builder2.add("id_", id_);
                builder2.add("text_", text_);
                builder2.add("MN", MN+"");
                Log.d("sss", "" + MN);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call = client.newCall(request);
                call.execute();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast t = Toast.makeText(WriteReplyActivity.this, "성공적으로 답장를 보냈습니다.", Toast.LENGTH_SHORT);
                        t.show();

                        Intent i = new Intent(WriteReplyActivity.this, ListActivity.class);
                        i.putExtra("position", MN);
                        i.putExtra("status", 4);
                        i.putExtra("permission", 1);
                        i.putExtra("id_", id_);
                        startActivity(i);

                        WriteReplyActivity.this.finish();
                    }
                });
                changeStatus c = new changeStatus(MN, 4);
                c.start();

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class changeStatus extends  Thread{
        int messagen;
        int value;
        public changeStatus(int p, int value) {
            messagen = p;
            this.value = value;
        }
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/changemsg.jsp");

                FormBody.Builder b = new FormBody.Builder();

                b.add("MN", messagen+"");
                b.add("status", value +"");
                FormBody body = b.build();
                builder = builder.post(body);
                Request request = builder.build();

                Call call = client.newCall(request);
                call.execute();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
