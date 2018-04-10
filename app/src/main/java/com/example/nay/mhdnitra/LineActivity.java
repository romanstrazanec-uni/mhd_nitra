package com.example.nay.mhdnitra;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LineActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    TextView tv;
    long lineId;
    String order = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lv = findViewById(R.id.line_stop_list_view);
        tv = findViewById(R.id.line_text_view);
        tv.setBackgroundColor(Color.rgb(190, 190, 220));
        lineId = getIntent().getLongExtra("line_id", 0);
        connectAdapter(order);
        addOnItemClickListener();
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
                dbh.getCursor(null, MyContract.LineStop.TABLE_NAME, new String[]{MyContract.Stop.TABLE_NAME},
                        new String[]{MyContract.LineStop.COLUMN_ID_STOP}, new String[]{MyContract.Stop.COLUMN_ID},
                        MyContract.LineStop.COLUMN_ID_LINE + " = " + lineId, null, MyContract.LineStop.COLUMN_NUMBER + order),
                new String[]{MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.line_stop_id, R.id.stop_name}, 0);
        lv.setAdapter(sca);
        Cursor c = dbh.getCursor(null, MyContract.Line.TABLE_NAME, null, null, null,
                MyContract.Line.COLUMN_ID + "=" + lineId, null, null);
        tv.setText("Trasa linky " + c.getString(c.getColumnIndex(MyContract.Line.COLUMN_LINE)));
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(LineActivity.this, TimesActivity.class);
                i.putExtra("line_stop_id", c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID)));
                startActivity(i);
            }
        });
    }
}
