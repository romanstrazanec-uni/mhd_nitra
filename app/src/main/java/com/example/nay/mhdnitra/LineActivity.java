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
    int direction = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        lineId = getIntent().getLongExtra("line_id", 0);

        lv = findViewById(R.id.line_stop_list_view);
        tv = findViewById(R.id.line_text_view);
        tv.setBackgroundColor(Color.rgb(190, 190, 220));
        connectAdapter(direction);
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
                if (direction == 0) direction = 1;
                else direction = 0;
                connectAdapter(direction);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connectAdapter(int direction) {
        sca = new SimpleCursorAdapter(this, R.layout.line_stop_list_layout,
                dbh.getCursor(new String[]{MyContract.LineStop.TABLE_NAME + "." + MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                        MyContract.LineStop.TABLE_NAME, new String[]{MyContract.Stop.TABLE_NAME},
                        new String[]{MyContract.LineStop.COLUMN_ID_STOP}, new String[]{MyContract.Stop.COLUMN_ID},
                        MyContract.LineStop.COLUMN_ID_LINE + " = " + lineId + " AND " + MyContract.LineStop.COLUMN_DIRECTION + " = " + direction,
                        null, null),
                new String[]{MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.line_stop_id, R.id.stop_name}, 0);
        lv.setAdapter(sca);

        tv.setText(String.format("Trasa linky %s, smer %s", getLineName(), getLastStopName()));
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(LineActivity.this, TimesActivity.class);
                i.putExtra("line", getLineName());
                i.putExtra("stop", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)));
                i.putExtra("direction", getLastStopName());
                i.putExtra("line_stop_id", c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID)));
                startActivity(i);
            }
        });
    }

    private String getLastStopName() {
        Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
        if (c.moveToLast()) return c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME));
        else return null;
    }

    private String getLineName() {
        Cursor c = dbh.getCursor(null, MyContract.Line.TABLE_NAME, null, null, null,
                MyContract.Line.COLUMN_ID + " = " + lineId, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(MyContract.Line.COLUMN_LINE));
    }
}
