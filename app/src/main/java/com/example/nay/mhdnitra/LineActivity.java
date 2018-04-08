package com.example.nay.mhdnitra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LineActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    long lineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lv = findViewById(R.id.line_stop_list_view);
        lineId = getIntent().getLongExtra("line_id", 0);
        connectAdapter();
    }

    public void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.line_stop_list_layout,
                dbh.getCursor(MyContract.LineStop.TABLE_NAME, MyContract.Stop.TABLE_NAME,
                        MyContract.LineStop.COLUMN_ID_STOP, MyContract.Stop.COLUMN_ID,
                        MyContract.LineStop.COLUMN_ID_LINE + " = " + lineId, MyContract.LineStop.COLUMN_NUMBER),
                new String[]{MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.line_stop_id, R.id.stop_name}, 0);
        lv.setAdapter(sca);
    }

}
