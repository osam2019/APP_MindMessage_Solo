package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReplyActivity extends AppCompatActivity {

    TextView txt;
    int MN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        txt = (TextView)findViewById(R.id.text3_txt);

        MN = getIntent().getIntExtra("MN", -1);

        GetReply getReply = new GetReply();
        getReply.start();
    }

    public void backBtn(View view) {
        finish();
    }

    class GetReply extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/reply_download.jsp");

                FormBody.Builder b = new FormBody.Builder();
                Log.d("sss", "" + MN);
                b.add("MN",MN+"");
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
                Log.d("sss", "onr");
                String result = response.body().string();

                JSONArray root = new JSONArray(result);

                if (root.length() == 1) {
                    final String s = root.getJSONObject(0).getString("text_");
                    final String s2 = root.getJSONObject(0).getString("name_");
                    Log.d("sss", "onrl");
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
            Log.d("sss", "f");
        }
    }
}
