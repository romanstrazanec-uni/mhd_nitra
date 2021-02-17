package sk.romanstrazanec.mhdnitra

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import com.example.nay.mhdnitra.R
import sk.romanstrazanec.mhdnitra.entities.FavouriteLine

class LineStopsActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    private var stopId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line_stops)
        val i = intent
        stopId = i.getLongExtra("stops_id", 0)
        lv = findViewById(R.id.line_stops_list_view)
        val tv = findViewById<TextView>(R.id.line_stops_text_view)
        tv.setBackgroundColor(Color.rgb(190, 190, 220))
        tv.text = String.format("Zastávka %s", i.getStringExtra("title"))
        connectAdapter()
        addOnItemClickListener()
        addOnItemLongClickListener()
    }

    private fun connectAdapter() {
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.line_list_layout,
                dbh.getCursor(null, MyContract.LineStop.TABLE_NAME, arrayOf(MyContract.Line.TABLE_NAME), arrayOf(MyContract.LineStop.COLUMN_ID_LINE), arrayOf(MyContract.Line.COLUMN_ID),
                        MyContract.LineStop.COLUMN_ID_STOP + "=" + stopId, MyContract.Line.TABLE_NAME + "." + MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_ID), arrayOf(MyContract.LineStop.COLUMN_ID_LINE, MyContract.Line.COLUMN_LINE), intArrayOf(R.id.line_id, R.id.line), 0)
    }

    private fun addOnItemClickListener() {
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val i = Intent(this@LineStopsActivity, LineActivity::class.java)
            i.putExtra("line_id", c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID_LINE)))
            startActivity(i)
        }
    }

    private fun addOnItemLongClickListener() {
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, _, position, _ ->
            val builder = AlertDialog.Builder(parent.context)
            var c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val id = c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID_LINE))
            c = dbh.getCursor(null, MyContract.FavouriteLine.TABLE_NAME, null, null, null,
                    MyContract.FavouriteLine.COLUMN_ID_LINE + " = " + id, null, null)
            if (c.moveToFirst()) {
                builder.setMessage(R.string.delete_from_favourites).setTitle(R.string.delete)
                builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                    dbh.deleteFavouriteLine(id)
                    dialogInterface.dismiss()
                }
            } else {
                builder.setMessage(R.string.add_to_favourites).setTitle(R.string.add)
                builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                    dbh.addFavouriteLine(FavouriteLine(1, id))
                    dialogInterface.dismiss()
                }
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.show()
            true
        }
    }
}