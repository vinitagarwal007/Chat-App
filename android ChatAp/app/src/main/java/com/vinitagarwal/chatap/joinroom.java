package com.vinitagarwal.chatap;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.number.IntegerWidth;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.onesignal.OSSubscriptionState;
import com.onesignal.OneSignal;

public class joinroom extends AppCompatActivity {
    SharedPreferences.Editor editor;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinroom);
        SharedPreferences sharedPreferences = getSharedPreferences("globalchat", MODE_PRIVATE);
         editor = sharedPreferences.edit();
    }

    public void btnonclick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText nametb = findViewById(R.id.nametb);
        EditText numbertb = findViewById(R.id.numbertb);
        if (nametb.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Blank Not Allowed", Toast.LENGTH_SHORT).show();
        } else {
            editor.putString("user", nametb.getText().toString());
            editor.putString("number", numbertb.getText().toString());
            editor.apply();
            intent.putExtra("name", nametb.getText().toString());
            intent.putExtra("number", numbertb.getText().toString());
            startActivity(intent);
            this.finish();
        }
    }

    public void getstatus(View view) {
        String toastmsg = OneSignal.getDeviceState().getUserId();
        Toast.makeText(this, toastmsg, Toast.LENGTH_SHORT).show();
    }
}
