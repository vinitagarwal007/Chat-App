package com.vinitagarwal.chatap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.DateTimePatternGenerator;
import android.icu.text.MeasureFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.emitter.Emitter.Listener;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    Button sendbtn;
    EditText sendtextbox;
    RecyclerView recyclerView;
    private Socket mSocket;
    OSPermissionSubscriptionState onesignalstatus = OneSignal.getPermissionSubscriptionState();
    private msgadapter msgadapter;
    String msg;
    String username;
    String number;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        SharedPreferences sharedPreferences = getSharedPreferences("globalchat", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.getString("user", "").equals("")) {
            Intent Main = new Intent(this, joinroom.class);
            Toast.makeText(this, sharedPreferences.getString("user", ""), Toast.LENGTH_SHORT).show();
            startActivity(Main);
        } else {
            username = sharedPreferences.getString("user", "");
            number = sharedPreferences.getString("number", "");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }
        try {
            IO.Options opt = new IO.Options();
            opt.query = "user=" + username + "&number=" + number + "&id=" + onesignalstatus.getSubscriptionStatus().getUserId();
            mSocket = IO.socket("http://vinitgarg111103.loclx.io", opt);
        } catch (URISyntaxException e) {

        }
        mSocket.on("msg", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                msg = (String) args[0].toString();
                runOnUiThread(new Runnable() {
                    public void run() {
                        JSONObject list = null;
                        try {
                            list = new JSONObject(msg);
                            list.put("sent", false);
                            msgadapter.additem(list);
                            recyclerView.scrollToPosition(msgadapter.getItemCount() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mSocket.on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Connected To Server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mSocket.on("newuser", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                msg = args[0].toString() + " Connected";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        JSONObject list = new JSONObject();
                        try {
                            list.put("name", " ");
                            list.put("sent", false);
                            list.put("msg", msg);
                            msgadapter.additem(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        mSocket.on("userleft", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                msg = args[0].toString() + " Disconnected";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        JSONObject list = new JSONObject();
                        try {
                            list.put("name", "");
                            list.put("sent", false);
                            list.put("msg", msg);
                            msgadapter.additem(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        mSocket.on(mSocket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "disconnected", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        mSocket.connect();
        setup();

    }


    private void setup() {
        sendbtn = findViewById(R.id.sendbtn);
        recyclerView = findViewById(R.id.recyclerview1);
        sendtextbox = findViewById(R.id.sendtextbox);
        msgadapter = new msgadapter(getLayoutInflater());
        recyclerView.setAdapter(msgadapter);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setReverseLayout(false);
        recyclerView.setLayoutManager(layout);

    }

    public void btnonclick(View view) {
        String msgtosend = sendtextbox.getText().toString();
        JSONObject list = new JSONObject();
        try {

            list.put("msg", msgtosend);
            list.put("name", username);
            mSocket.emit("msg", list.toString());
            list.put("sent", true);
            msgadapter.additem(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendtextbox.setText("");
        recyclerView.scrollToPosition(msgadapter.getItemCount() - 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

}