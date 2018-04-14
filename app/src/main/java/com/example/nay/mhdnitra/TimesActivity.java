package com.example.nay.mhdnitra;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

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

        TextView tv = findViewById(R.id.times_text_view);
        tv.setText("Časy linky " + lineStopId);

        lv = findViewById(R.id.times_list_view);
        connectAdapter("all");
        addOnItemClickListener();
        addOnItemLongClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.time_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        MenuItem tm;
        switch (item.getItemId()) {
            case R.id.time_menu_add:
                Intent i = new Intent(this, TimeAddActivity.class);
                i.putExtra("line_stop_id", lineStopId);
                startActivity(i);
                return true;
            case R.id.time_menu_all:
                item.setVisible(false);
                tm = findViewById(R.id.time_menu_weekend);
                tm.setVisible(true);
                tm = findViewById(R.id.time_menu_holidays);
                tm.setVisible(true);
                connectAdapter("all");
                return true;
            case R.id.time_menu_weekend:
                item.setVisible(false);
                tm = findViewById(R.id.time_menu_all);
                tm.setVisible(true);
                tm = findViewById(R.id.time_menu_holidays);
                tm.setVisible(true);
                connectAdapter("weekend");
                return true;
            case R.id.time_menu_holidays:
                item.setVisible(false);
                tm = findViewById(R.id.time_menu_all);
                tm.setVisible(true);
                tm = findViewById(R.id.time_menu_weekend);
                tm.setVisible(true);
                connectAdapter("holidays");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connectAdapter(String filter) {
        String where = MyContract.Time.COLUMN_ID_LINESTOP + " = " + lineStopId;
        switch (filter) {
            case "weekend":
                where += " AND NOT " + MyContract.Time.COLUMN_WEEKEND + " = 0";
                break;
            case "holidays":
                where += " AND NOT " + MyContract.Time.COLUMN_HOLIDAYS + " = 0";
                break;
            default:
        }
        sca = new SimpleCursorAdapter(this, R.layout.times_list_layout, dbh.getCursor(null, MyContract.Time.TABLE_NAME,
                null, null, null, where,
                null, MyContract.Time.COLUMN_HOUR + ", " + MyContract.Time.COLUMN_MINUTE),
                new String[]{MyContract.Time.COLUMN_ID, MyContract.Time.COLUMN_HOUR, MyContract.Time.COLUMN_MINUTE},
                new int[]{R.id.times_id, R.id.hour, R.id.minutes}, 0);
        lv.setAdapter(sca);
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(TimesActivity.this, LineActivity.class);
                i.putExtra("time_id", c.getLong(c.getColumnIndex(MyContract.Time.COLUMN_ID)));
                startActivity(i);
            }
        });
    }

    private void addOnItemLongClickListener() {
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());

                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                final long ID = c.getLong(c.getColumnIndex(MyContract.Time.COLUMN_ID));

                builder.setMessage("Naozaj si prajete odstrániť túto položku?").setTitle("Odstrániť");
                builder.setPositiveButton("Áno", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbh.deleteTime(ID);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
                return false;
            }
        });
    }
}
