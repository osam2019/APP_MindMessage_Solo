package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String ipadress = "192.168.122.100";
    Button login, signup;
    EditText id, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = (Button) findViewById(R.id.login_btn);
        signup = (Button) findViewById(R.id.signup_btn);
        id = (EditText) findViewById(R.id.id_ext);
        password = (EditText) findViewById(R.id.pw_ext);


    }

    public void signup(View view) {
        Intent i = new Intent(MainActivity.this, signupActivity.class);
        startActivity(i);
    }

    class NetworkThread extends Thread {
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/get.jsp");

                String i = id.getText().toString();
                FormBody.Builder b = new FormBody.Builder();
                b.add("id", i);
                FormBody body = b.build();
                builder = builder.post(body);
                Request request = builder.build();

                Callback2 callback1 = new Callback2();
                Call call = client.newCall(request);
                call.enqueue(callback1);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Callback2 implements Callback {
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                String result = response.body().string();

                JSONArray root = new JSONArray(result);
                if (root.length() >= 1) {
                    if (root.getJSONObject(0).getString("pw_").equals(password.getText().toString())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = Toast.makeText(MainActivity.this, "성공적으로 로그인하였습니다.", Toast.LENGTH_LONG);
                                t.show();
                            }
                        });
                        Intent i = new Intent(MainActivity.this, MenuActivity.class);
                        i.putExtra("index", root.getJSONObject(0).getInt("index_"));
                        i.putExtra("id", root.getJSONObject(0).getString("id_"));
                        startActivity(i);
                        finish();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast t = Toast.makeText(MainActivity.this, "아이디와 패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG);
                                t.show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = Toast.makeText(MainActivity.this, "존재하지 않는 id입니다.", Toast.LENGTH_LONG);
                            t.show();
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
//TODO 네트워크가 꺼져있을 떄 처리
//TODO 게시판 만들기

    public void testBtn(View view) {
        NetworkThread thread = new NetworkThread();
        thread.start();
    }
}

