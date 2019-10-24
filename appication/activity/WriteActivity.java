package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WriteActivity extends AppCompatActivity {

    String id;
    int prikey;
    EditText editText;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        editText = (EditText)findViewById(R.id.text_ext);
        checkBox = (CheckBox)findViewById(R.id.ano_chk);

        id = getIntent().getStringExtra("id");
        prikey = getIntent().getIntExtra("index", -1);
    }

    public void back(View view) {
        Intent intent = new Intent(WriteActivity.this, MenuActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("index", prikey);
        startActivity(intent);
        finish();
    }

    public void OKBtn(View view) {
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
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/upload.jsp");

                String text_ = editText.getText().toString();
                String type_ = "message";

                FormBody.Builder builder2 = new FormBody.Builder();
                if (checkBox.isChecked()) {
                    builder2.add("an", "anonymous");
                }
                else {
                    builder2.add("an", "Nanonymous");
                }
                builder2.add("id_", id);
                builder2.add("text_", text_);
                builder2.add("type_", type_);

                FormBody body = builder2.build();
                builder = builder.post(body);

                Request request = builder.build();
                Call call = client.newCall(request);
                call.execute();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast t = Toast.makeText(WriteActivity.this, "성공적으로 메세지를 보냈습니다.", Toast.LENGTH_SHORT);
                        t.show();
                        Intent intent = new Intent(WriteActivity.this, MenuActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("index", prikey);
                        startActivity(intent);
                        finish();
                    }
                });
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
