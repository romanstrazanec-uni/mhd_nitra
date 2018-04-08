package com.example.nay.mhdnitra;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FavouriteLinesActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_lines);
        lv = findViewById(R.id.favourite_line_list_view);
        connectAdapter();
        addOnItemClickListener();
    }

    private void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.line_list_layout,
                dbh.getCursor(MyContract.FavouriteLine.TABLE_NAME, MyContract.Line.TABLE_NAME,
                        MyContract.FavouriteLine.COLUMN_ID_LINE, MyContract.Line.COLUMN_ID, null, null),
                new String[]{MyContract.FavouriteLine.COLUMN_ID, MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE},
                new int[]{R.id.favourite_line_id, R.id.line_id, R.id.line}, 0);
        lv.setAdapter(sca);
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListView lv = findViewById(R.id.favourite_line_list_view);
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(FavouriteLinesActivity.this, LineActivity.class);
                i.putExtra("line_id", c.getLong(c.getColumnIndex(MyContract.Line.COLUMN_ID)));
                startActivity(i);
            }
        });
    }
}
