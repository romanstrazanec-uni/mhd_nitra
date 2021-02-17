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

class FavouriteStopsActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_stops)
        lv = findViewById(R.id.favourite_stops_list_view)
        val tv = findViewById<TextView>(R.id.favourite_stops_text_view)
        tv.setBackgroundColor(Color.rgb(255, 230, 0))
        tv.setTextColor(Color.BLACK)
        tv.setText(R.string.stop)
        connectAdapter()
        addOnItemClickListener()
        addOnItemLongClickListener()
    }

    private fun connectAdapter() {
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.stop_list_layout,
                dbh.getCursor(null, MyContract.FavouriteStop.TABLE_NAME, arrayOf(MyContract.Stop.TABLE_NAME), arrayOf(MyContract.FavouriteStop.COLUMN_ID_STOP), arrayOf(MyContract.Stop.COLUMN_ID),
                        null, null, null), arrayOf(MyContract.FavouriteStop.COLUMN_ID, MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME), intArrayOf(R.id.favourite_stop_id, R.id.stop_id, R.id.stop_name), 0)
    }

    private fun addOnItemClickListener() {
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val i = Intent(this@FavouriteStopsActivity, LineStopsActivity::class.java)
            i.putExtra("title", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)))
            i.putExtra("stops_id", c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID)))
            startActivity(i)
        }
    }

    private fun addOnItemLongClickListener() {
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, _, position, _ ->
            val builder = AlertDialog.Builder(parent.context)
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val id = c.getLong(c.getColumnIndex(MyContract.FavouriteStop.COLUMN_ID))
            builder.setMessage(R.string.delete_from_favourites).setTitle(R.string.delete)
            builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                dbh.deleteFavouriteStop(id)
                connectAdapter()
                dialogInterface.dismiss()
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.show()
            true
        }
    }
}