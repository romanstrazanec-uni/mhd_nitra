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

class FavouriteLinesActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_lines)
        lv = findViewById(R.id.favourite_line_list_view)
        val tv = findViewById<TextView>(R.id.favourite_line_text_view)
        tv.setBackgroundColor(Color.rgb(255, 230, 0))
        tv.setTextColor(Color.BLACK)
        tv.setText(R.string.line)
        connectAdapter()
        addOnItemClickListener()
        addOnItemLongClickListener()
    }

    private fun connectAdapter() {
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.line_list_layout,
                dbh.getCursor(null, MyContract.FavouriteLine.TABLE_NAME, arrayOf(MyContract.Line.TABLE_NAME), arrayOf(MyContract.FavouriteLine.COLUMN_ID_LINE), arrayOf(MyContract.Line.COLUMN_ID),
                        null, null, MyContract.Line.COLUMN_LINE), arrayOf(MyContract.FavouriteLine.COLUMN_ID, MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE), intArrayOf(R.id.favourite_line_id, R.id.line_id, R.id.line), 0)
    }

    private fun addOnItemClickListener() {
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val i = Intent(this@FavouriteLinesActivity, LineActivity::class.java)
            i.putExtra("line_id", c.getLong(c.getColumnIndex(MyContract.Line.COLUMN_ID)))
            startActivity(i)
        }
    }

    private fun addOnItemLongClickListener() {
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, _, position, _ ->
            val builder = AlertDialog.Builder(parent.context)
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val id = c.getLong(c.getColumnIndex(MyContract.FavouriteLine.COLUMN_ID))
            builder.setMessage(R.string.delete_from_favourites).setTitle(R.string.delete)
            builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                dbh.deleteFavouriteLine(id)
                connectAdapter()
                dialogInterface.dismiss()
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.show()
            true
        }
    }
}