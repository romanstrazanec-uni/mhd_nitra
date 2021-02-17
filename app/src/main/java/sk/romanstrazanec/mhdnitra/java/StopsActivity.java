package sk.romanstrazanec.mhdnitra.java;

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
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.nay.mhdnitra.R;

import sk.romanstrazanec.mhdnitra.entities.FavouriteStop;

public class StopsActivity extends AppCompatActivity {
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    SearchView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops);

        lv = findViewById(R.id.stops_list_view);
        sv = findViewById(R.id.stops_search_view);
        TextView tv = findViewById(R.id.stops_text_view);
        tv.setBackgroundColor(Color.rgb(255, 230, 0));
        tv.setTextColor(Color.BLACK);
        tv.setText(R.string.stop);

        connectAdapter(null);
        addOnItemClickListener();
        addOnItemLongClickListener();
        addOnQueryTextListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stop_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.stop_menu_lines:
                finish();
                return true;
            case R.id.stop_menu_favourites:
                startActivity(new Intent(this, FavouriteStopsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void connectAdapter(String s) {
        lv.setAdapter(new SimpleCursorAdapter(this, R.layout.stop_list_layout,
                dbh.getCursor(null, MyContract.Stop.TABLE_NAME, null, null, null, s, null, null),
                new String[]{MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.stop_id, R.id.stop_name}, 0));
    }

    public void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                ListView lv = findViewById(R.id.stops_list_view);
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(StopsActivity.this, LineStopsActivity.class);
                i.putExtra("stops_id", c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID)));
                i.putExtra("title", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)));
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
                final long ID = c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID));

                c = dbh.getCursor(null, MyContract.FavouriteStop.TABLE_NAME, null, null, null,
                        MyContract.FavouriteStop.COLUMN_ID_STOP + " = " + ID, null, null);
                if (c.moveToFirst()) {
                    builder.setMessage(R.string.delete_from_favourites).setTitle(R.string.delete);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbh.deleteFavouriteStop(ID);
                            dialogInterface.dismiss();
                        }
                    });
                } else {
                    builder.setMessage(R.string.add_to_favourites).setTitle(R.string.add);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbh.addFavouriteStop(new FavouriteStop(1, ID));
                            dialogInterface.dismiss();
                        }
                    });
                }
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });
    }

    private void addOnQueryTextListener() {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.equals("")) s = null;
                else s = MyContract.Stop.COLUMN_NAME + " LIKE '%" + s + "%'";
                connectAdapter(s);
                return true;
            }
        });
    }
}
