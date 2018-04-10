package com.example.nay.mhdnitra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class TimesActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    long lineStopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);

        Intent i = getIntent();
        lineStopId = i.getLongExtra("line_stop_id", 0);

        lv = findViewById(R.id.times_list_view);
        connectAdapter();
    }

    public void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.times_list_layout, dbh.getCursor(null, MyContract.Time.TABLE_NAME,
                null, null, null, MyContract.Time.COLUMN_ID_LINESTOP + " = " + lineStopId,
                null, MyContract.Time.COLUMN_HOUR + ", " + MyContract.Time.COLUMN_MINUTE),
                new String[]{MyContract.Time.COLUMN_ID, MyContract.Time.COLUMN_HOUR, MyContract.Time.COLUMN_MINUTE},
                new int[]{R.id.times_id, R.id.hour, R.id.minutes}, 0);
        lv.setAdapter(sca);
    }
}
