package com.example.nay.mhdnitra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class LineActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    long lineId;
    String order = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lv = findViewById(R.id.line_stop_list_view);
        lineId = getIntent().getLongExtra("line_id", 0);
        connectAdapter(order);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line_stop_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.line_stop_menu_order:
                if (order.equals("")) order = " DESC";
                else order = "";
                connectAdapter(order);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connectAdapter(String order) {
        sca = new SimpleCursorAdapter(this, R.layout.line_stop_list_layout,
                dbh.getCursor(MyContract.LineStop.TABLE_NAME, new String[]{MyContract.Stop.TABLE_NAME},
                        new String[]{MyContract.LineStop.COLUMN_ID_STOP}, new String[]{MyContract.Stop.COLUMN_ID},
                        MyContract.LineStop.COLUMN_ID_LINE + " = " + lineId, MyContract.LineStop.COLUMN_NUMBER + order),
                new String[]{MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.line_stop_id, R.id.stop_name}, 0);
        lv.setAdapter(sca);
    }

}
