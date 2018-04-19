package com.example.nay.mhdnitra;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class FavouriteStopsActivity extends AppCompatActivity {
    DBHelper dbh = new DBHelper(this);
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_stops);

        lv = findViewById(R.id.favourite_stops_list_view);
        TextView tv = findViewById(R.id.favourite_stops_text_view);
        tv.setBackgroundColor(Color.rgb(255, 230, 0));
        tv.setTextColor(Color.BLACK);
        tv.setText(R.string.stop);

        connectAdapter();
        addOnItemClickListener();
        addOnItemLongClickListener();
    }

    private void connectAdapter() {
        lv.setAdapter(new SimpleCursorAdapter(this, R.layout.stop_list_layout,
                dbh.getCursor(null, MyContract.FavouriteStop.TABLE_NAME, new String[]{MyContract.Stop.TABLE_NAME},
                        new String[]{MyContract.FavouriteStop.COLUMN_ID_STOP}, new String[]{MyContract.Stop.COLUMN_ID},
                        null, null, null),
                new String[]{MyContract.FavouriteStop.COLUMN_ID, MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME},
                new int[]{R.id.favourite_stop_id, R.id.stop_id, R.id.stop_name}, 0));
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(FavouriteStopsActivity.this, LineStopsActivity.class);
                i.putExtra("title", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)));
                i.putExtra("stops_id", c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID)));
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
                final long ID = c.getLong(c.getColumnIndex(MyContract.FavouriteStop.COLUMN_ID));

                builder.setMessage(R.string.delete_from_favourites).setTitle(R.string.delete);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbh.deleteFavouriteStop(ID);
                        connectAdapter();
                        dialogInterface.dismiss();
                    }
                });
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
}
