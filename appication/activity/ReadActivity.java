package com.example.mindmessage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReadActivity extends AppCompatActivity {

    ArrayList<String> data;
    ArrayList<Integer> msgN;
    ArrayList<Integer> status;

    ListView list1;
    String id_;
    int idx;
    int permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
    }

    public void refresh() {
        Intent intent = new Intent(ReadActivity.this, ReadActivity.class);
        intent.putExtra("id", id_);
        intent.putExtra("index", idx);
        startActivity(intent);
        finish();
        /*data = new ArrayList<>();
        msgN = new ArrayList<>();
        status = new ArrayList<>();

        getPermission getPermission = new getPermission();
        getPermission.start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        list1 = (ListView)findViewById(R.id.message_list);
        id_ = getIntent().getStringExtra("id");
        idx = getIntent().getIntExtra("index", -1);

        data = new ArrayList<>();
        msgN = new ArrayList<>();
        status = new ArrayList<>();

        getPermission getPermission = new getPermission();
        getPermission.start();
    }

    class ListAdapter extends BaseAdapter {

        BtnListener listener = new BtnListener();

        @Override
        // 리스트 뷰의 항목 개수를 반환하는 메서드
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 재사용 가능한 뷰가 없다면 뷰를 만들어준다
            if(convertView == null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.row, null);
            }
            // 뷰를 구성한다.
            Button sub_btn1 = (Button)convertView.findViewById(R.id.row1_btn);
            ImageButton sub_btn2 = (ImageButton)convertView.findViewById(R.id.del_btn);
            TextView status_text = (TextView)convertView.findViewById(R.id.status_txt);

            sub_btn1.setOnClickListener(listener);
            sub_btn2.setOnClickListener(listener);

            // 버튼에 인덱스 값을 저장한다.
            sub_btn1.setTag(position);
            sub_btn2.setTag(position);

            sub_btn1.setText(data.get(position));
            String re = "";
            switch (status.get(position)) {
                case 0:
                    re = "읽지 않음";
                    break;
                case 1:
                    re = "읽음";
                    break;
                case 2:
                    re = "삭제함";
                    break;
                case 4:
                    re = "답장함";
                    break;
                case 5:
                    re = "답장함";
                    break;
            }
            status_text.setText(re);
            // 뷰를 반환한다.

            return convertView;
        }
    }

    class BtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // 버튼의 아이디를 추출한다.
            int id = v.getId();

            // 리스트의 인덱스 값을 추출한다.
            int position = (Integer)v.getTag();

            switch (id){
                case R.id.row1_btn :
                    Log.d("btn", "row2" + position);
                    Intent intent = new Intent(ReadActivity.this, ListActivity.class);
                    intent.putExtra("position", msgN.get(position));
                    if (permission == 1) {
                        if (status.get(position) != 4) {
                            changeStatus changeStatus = new changeStatus(position, 1);
                            changeStatus.start();
                        }
                    }
                    intent.putExtra("status", status.get(position));
                    intent.putExtra("permission", permission);
                    intent.putExtra("id_", id_);
                    startActivity(intent);
                    break;
                case R.id.del_btn :
                    Log.d("btn", "row2" + position);
                    if (permission != 0) {
                        if (status.get(position) != 4) {
                            changeStatus changeStatus = new changeStatus(position, 2);
                            changeStatus.start();
                        }
                        else {
                            changeStatus changeStatus = new changeStatus(position, 5);
                            changeStatus.start();
                        }
                    }
                    else {
                        deleteMsg delete = new deleteMsg(position);
                        delete.start();
                    }
                    refresh();
                    break;
            }
        }
    }

    class changeStatus extends  Thread{
        int position;
        int value;
        public changeStatus(int p, int value) {
            position = p;
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

                b.add("MN", msgN.get(position)+"");
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

    class deleteMsg extends  Thread{
        int position;
        public deleteMsg(int p) {
            super();
            Log.d("sss", "??0");
            position = p;
        }
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/deletemsg.jsp");

                FormBody.Builder b = new FormBody.Builder();

                b.add("MN", msgN.get(position)+"");
                FormBody body = b.build();
                builder = builder.post(body);
                Request request = builder.build();

                Call call = client.newCall(request);
                call.execute();
                Log.d("sss", "done " + msgN.get(position));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void okBtn(View v) {
        Intent i = new Intent(ReadActivity.this, MenuActivity.class);
        i.putExtra("id", id_);
        i.putExtra("index", idx);
        startActivity(i);
        finish();
    }

    class getPermission extends  Thread{
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

                Callback2 callback1 = new Callback2();
                Call call = client.newCall(request);
                call.enqueue(callback1);
            }catch (Exception e) {
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
                if(root.length() == 1) {
                    permission = root.getJSONObject(0).getInt("permission");

                    SetInfo setInfo = new SetInfo();
                    setInfo.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {

        }
    }


    class SetInfo extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder =  new Request.Builder();
                builder = builder.url("http://" + MainActivity.ipadress +":8080/Test/download.jsp");

                FormBody.Builder b = new FormBody.Builder();

                b.add("t", "-1");

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
                for (int i = 0; i < root.length(); i++) {
                    JSONObject j = root.getJSONObject(i);
                    if (permission == 1) {
                        if (j.getInt("status") != 2 && j.getInt("status") != 5) {
                            data.add(j.getString("name"));
                            msgN.add(j.getInt("messagenum"));
                            status.add(j.getInt("status"));
                        }
                    }
                    else {
                        int n = j.getInt("idx");
                        if (n == idx) {
                            data.add(j.getString("name"));
                            msgN.add(j.getInt("messagenum"));
                            status.add(j.getInt("status"));
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListAdapter adapter = new ListAdapter();
                        list1.setAdapter(adapter);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {

        }
    }
}
