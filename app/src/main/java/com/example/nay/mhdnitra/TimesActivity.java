package com.example.nay.mhdnitra;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

import com.example.nay.mhdnitra.Entities.Time;

public class TimesActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    long lineStopId;
    String filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);

        Intent i = getIntent();
        lineStopId = i.getLongExtra("line_stop_id", 0);

        TextView tv = findViewById(R.id.times_text_view);
        tv.setBackgroundColor(Color.rgb(190, 190, 220));
        Cursor c = dbh.getCursor(null, MyContract.Line.TABLE_NAME, null, null, null,
                MyContract.Line.COLUMN_ID + " = " + i.getLongExtra("line_id", 0), null, null);
        c.moveToFirst();
        tv.setText(String.format("Časy linky %s", c.getString(c.getColumnIndex(MyContract.Line.COLUMN_LINE))));

        lv = findViewById(R.id.times_list_view);
        filter = "all";
        connectAdapter(filter);
        addOnItemClickListener();
        addOnItemLongClickListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    if (b != null) {
                        dbh.addTime(null, new Time(0, lineStopId,
                                b.getInt("hour", 0),
                                b.getInt("minute", 0),
                                b.getInt("weekend", 0),
                                b.getInt("holidays", 0)));
                        connectAdapter(filter);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.time_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time_menu_add:
                Intent i = new Intent(this, TimeAddActivity.class);
                i.putExtra("line_stop_id", lineStopId);
                startActivityForResult(i, 1);
                return true;
            case R.id.time_menu_all:
                filter = "all";
                connectAdapter(filter);
                return true;
            case R.id.time_menu_weekend:
                filter = "weekend";
                connectAdapter(filter);
                return true;
            case R.id.time_menu_holidays:
                filter = "holidays";
                connectAdapter(filter);
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
                Intent i = new Intent(TimesActivity.this, TimeAddActivity.class);
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
                        connectAdapter(filter);
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
