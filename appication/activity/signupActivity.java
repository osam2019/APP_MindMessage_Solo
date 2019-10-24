package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class signupActivity extends AppCompatActivity {

    Button signup, cancel;
    EditText id, pw, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button)findViewById(R.id.signup_ok_btn);
        cancel = (Button)findViewById(R.id.signup_cancel_btn);
        id = (EditText)findViewById(R.id.signup_id_ext);
        pw = (EditText)findViewById(R.id.signup_pw_ext);
        name = (EditText)findViewById(R.id.signup_name_ext);

    }

    public void cancelBtn(View view) {
        this.finish();
    }


    public void signup2Btn(View view) {
        CheckThread thread = new CheckThread();
        thread.start();
    }
    class UploadThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/server.jsp");

                String i = id.getText().toString();
                String i2 = pw.getText().toString();
                String i3 = name.getText().toString();

                FormBody.Builder builder2 = new FormBody.Builder();
                builder2.add("id", i);
                builder2.add("pw", i2);
                builder2.add("name", i3);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call = client.newCall(request);
                //call.execute();
                NetworkCallback callback = new NetworkCallback();
                call.enqueue(callback);

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class CheckThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/get.jsp");

                String i = id.getText().toString();
                FormBody.Builder b = new FormBody.Builder();
                b.add("id", i);
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

    class NetworkCallback implements Callback{
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try{
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast tt = Toast.makeText(getBaseContext(), "성공적으로 id를 만들었습니다..", Toast.LENGTH_SHORT);
                        tt.show();
                        finish();
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Callback1 implements  Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();

                JSONArray root = new JSONArray(result);
                if (root.length() >= 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = Toast.makeText(signupActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
                }
                else {
                    UploadThread thread = new UploadThread();
                    thread.start();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
        }
    }
}
