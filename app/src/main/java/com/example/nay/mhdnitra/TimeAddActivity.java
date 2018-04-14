package com.example.nay.mhdnitra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class TimeAddActivity extends AppCompatActivity {
    Spinner s1, s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_add);
        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
    }


    public void addTime(View view) {
        Intent i = new Intent();
        EditText et = findViewById(R.id.editText);
        i.putExtra("hour", et.getText().toString().split(":")[0]);
        i.putExtra("minute", et.getText().toString().split(":")[1]);
        i.putExtra("weekend", s1.getSelectedItemPosition());
        i.putExtra("holidays", s2.getSelectedItemPosition());
        setResult(RESULT_OK, i);
        finish();
    }
}
