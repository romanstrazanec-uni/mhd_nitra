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

import com.example.nay.mhdnitra.Entities.FavouriteLine;

public class LineStopsActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;
    long stopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_stops);

        Intent i = getIntent();
        stopId = i.getLongExtra("stops_id", 0);

        lv = findViewById(R.id.line_stops_list_view);
        TextView tv = findViewById(R.id.line_stops_text_view);
        tv.setBackgroundColor(Color.rgb(190, 190, 220));
        tv.setText(String.format("Zastávka %s", i.getStringExtra("title")));

        connectAdapter();
        addOnItemClickListener();
        addOnItemLongClickListener();
    }

    private void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.line_list_layout,
                dbh.getCursor(null, MyContract.LineStop.TABLE_NAME, new String[]{MyContract.Line.TABLE_NAME},
                        new String[]{MyContract.LineStop.COLUMN_ID_LINE}, new String[]{MyContract.Line.COLUMN_ID},
                        MyContract.LineStop.COLUMN_ID_STOP + "=" + stopId, MyContract.Line.TABLE_NAME + "." + MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_ID),
                new String[]{MyContract.LineStop.COLUMN_ID_LINE, MyContract.Line.COLUMN_LINE},
                new int[]{R.id.line_id, R.id.line}, 0);
        lv.setAdapter(sca);
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(LineStopsActivity.this, LineActivity.class);
                i.putExtra("line_id", c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID_LINE)));
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
                final long ID = c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID_LINE));

                c = dbh.getCursor(null, MyContract.FavouriteLine.TABLE_NAME, null, null, null,
                        MyContract.FavouriteLine.COLUMN_ID_LINE + " = " + ID, null, null);
                if (c.moveToFirst()) {
                    builder.setMessage("Odobrať z obľúbených?").setTitle("Odstrániť");
                    builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbh.deleteFavouriteLine(ID);
                            dialogInterface.dismiss();
                        }
                    });
                } else {
                    builder.setMessage("Pridať medzi obľúbené?").setTitle("Pridať");
                    builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbh.addFavouriteLine(new FavouriteLine(1, ID));
                            dialogInterface.dismiss();
                        }
                    });
                }
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
