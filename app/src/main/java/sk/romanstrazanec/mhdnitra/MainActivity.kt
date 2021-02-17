package sk.romanstrazanec.mhdnitra

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import com.example.nay.mhdnitra.R
import sk.romanstrazanec.mhdnitra.entities.FavouriteLine

class MainActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lv = findViewById(R.id.line_list_view)
        val tv = findViewById<TextView>(R.id.main_text_view)
        tv.setBackgroundColor(Color.rgb(255, 230, 0))
        tv.setTextColor(Color.BLACK)
        tv.setText(R.string.line)
        connectAdapter()
        addOnItemClickListener()
        addOnItemLongClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.line_menu_stops -> {
                startActivity(Intent(this, StopsActivity::class.java))
                true
            }
            R.id.line_menu_favourites -> {
                startActivity(Intent(this, FavouriteLinesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun connectAdapter() {
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.line_list_layout,
                dbh.getCursor(null, MyContract.Line.TABLE_NAME, null, null, null, null, null, null), arrayOf(MyContract.Line.COLUMN_ID, MyContract.Line.COLUMN_LINE), intArrayOf(R.id.line_id, R.id.line), 0)
    }

    private fun addOnItemClickListener() {
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val i = Intent(this@MainActivity, LineActivity::class.java)
            i.putExtra("line_id", c.getLong(c.getColumnIndex(MyContract.Line.COLUMN_ID)))
            startActivity(i)
        }
    }

    private fun addOnItemLongClickListener() {
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, _, position, _ ->
            val builder = AlertDialog.Builder(parent.context)
            var c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val id = c.getLong(c.getColumnIndex(MyContract.Line.COLUMN_ID))
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