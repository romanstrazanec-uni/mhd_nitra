
package com.example.nay.mhdnitra;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.LineListView);
        connectAdapter();
        addOnItemClickListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.line_menu_zastavky:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void connectAdapter(){
        sca = new SimpleCursorAdapter(this, R.layout.line_list_layout, dbh.getCursor(MyContract.Line.TABLE_NAME),
                new String[]{MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE},
                new int[]{R.id.line_id, R.id.line}, 0);
        lv.setAdapter(sca);
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ListView lv = (ListView) findViewById(R.id.LineListView);
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
            }
        });
    }
}
