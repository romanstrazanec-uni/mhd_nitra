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

public class FavouriteLinesActivity extends AppCompatActivity {
    SimpleCursorAdapter sca;
    DBHelper dbh = new DBHelper(this);
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_lines);

        lv = findViewById(R.id.favourite_line_list_view);
        TextView tv = findViewById(R.id.favourite_line_text_view);
        tv.setBackgroundColor(Color.rgb(255, 230, 0));
        tv.setTextColor(Color.BLACK);
        tv.setText("Linka");

        connectAdapter();
        addOnItemClickListener();
        addOnItemLongClickListener();
    }

    private void connectAdapter() {
        sca = new SimpleCursorAdapter(this, R.layout.line_list_layout,
                dbh.getCursor(MyContract.FavouriteLine.TABLE_NAME, new String[]{MyContract.Line.TABLE_NAME},
                        new String[]{MyContract.FavouriteLine.COLUMN_ID_LINE}, new String[]{MyContract.Line.COLUMN_ID},
                        null, MyContract.Line.COLUMN_LINE),
                new String[]{MyContract.FavouriteLine.COLUMN_ID, MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE},
                new int[]{R.id.favourite_line_id, R.id.line_id, R.id.line}, 0);
        lv.setAdapter(sca);
    }

    private void addOnItemClickListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) lv.getAdapter()).getCursor();
                c.moveToPosition(position);
                Intent i = new Intent(FavouriteLinesActivity.this, LineActivity.class);
                i.putExtra("line_id", c.getLong(c.getColumnIndex(MyContract.Line.COLUMN_ID)));
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
                final long ID = c.getLong(c.getColumnIndex(MyContract.FavouriteLine.COLUMN_ID));

                builder.setMessage("Odobrať z obľúbených?" + ID).setTitle("Obľúbené");
                builder.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbh.deleteFavouriteLine(ID);
                        connectAdapter();
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
