package com.example.nay.mhdnitra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TimeAddActivity extends AppCompatActivity {
    Spinner s1, s2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_add);
        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        connectSpinnerAdapter(s1, R.array.weekend);
        connectSpinnerAdapter(s2, R.array.holidays);
    }


    public void addTime(View view) {
        EditText et = findViewById(R.id.editText);
        String time = et.getText().toString();

        if (validTime(time)) {
            Intent i = new Intent();
            i.putExtra("hour", Integer.parseInt(et.getText().toString().split(":")[0]));
            i.putExtra("minute", Integer.parseInt(et.getText().toString().split(":")[1]));
            i.putExtra("weekend", s1.getSelectedItemPosition());
            i.putExtra("holidays", s2.getSelectedItemPosition());
            setResult(RESULT_OK, i);
            finish();
        } else {
            Toast.makeText(this, "Nesprávny formát času.", Toast.LENGTH_SHORT).show();
        }
    }

    private void connectSpinnerAdapter(Spinner s, int array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    private boolean validTime(String time) {
        try {
            int hour = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);
            if ((hour < 0 || hour > 23) || (minute < 0 || minute > 59)) return false;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}
