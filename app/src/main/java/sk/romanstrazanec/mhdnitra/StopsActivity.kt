package sk.romanstrazanec.mhdnitra

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import com.example.nay.mhdnitra.R
import sk.romanstrazanec.mhdnitra.entities.FavouriteStop

class StopsActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    private var sv: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stops)
        lv = findViewById(R.id.stops_list_view)
        sv = findViewById(R.id.stops_search_view)
        val tv = findViewById<TextView>(R.id.stops_text_view)
        tv.setBackgroundColor(Color.rgb(255, 230, 0))
        tv.setTextColor(Color.BLACK)
        tv.setText(R.string.stop)
        connectAdapter(null)
        addOnItemClickListener()
        addOnItemLongClickListener()
        addOnQueryTextListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.stop_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.stop_menu_lines -> {
                finish()
                true
            }
            R.id.stop_menu_favourites -> {
                startActivity(Intent(this, FavouriteStopsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun connectAdapter(s: String?) {
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.stop_list_layout,
                dbh.getCursor(null, MyContract.Stop.TABLE_NAME, null, null, null, s, null, null), arrayOf(MyContract.Stop.COLUMN_ID, MyContract.Stop.COLUMN_NAME), intArrayOf(R.id.stop_id, R.id.stop_name), 0)
    }

    fun addOnItemClickListener() {
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val lv = findViewById<ListView>(R.id.stops_list_view)
            val c = (lv.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val i = Intent(this@StopsActivity, LineStopsActivity::class.java)
            i.putExtra("stops_id", c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID)))
            i.putExtra("title", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)))
            startActivity(i)
        }
    }

    private fun addOnItemLongClickListener() {
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, _, position, _ ->
            val builder = AlertDialog.Builder(parent.context)
            var c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val id = c.getLong(c.getColumnIndex(MyContract.Stop.COLUMN_ID))
            c = dbh.getCursor(null, MyContract.FavouriteStop.TABLE_NAME, null, null, null,
                    MyContract.FavouriteStop.COLUMN_ID_STOP + " = " + id, null, null)
            if (c.moveToFirst()) {
                builder.setMessage(R.string.delete_from_favourites).setTitle(R.string.delete)
                builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                    dbh.deleteFavouriteStop(id)
                    dialogInterface.dismiss()
                }
            } else {
                builder.setMessage(R.string.add_to_favourites).setTitle(R.string.add)
                builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                    dbh.addFavouriteStop(FavouriteStop(1, id))
                    dialogInterface.dismiss()
                }
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.show()
            true
        }
    }

    private fun addOnQueryTextListener() {
        sv!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                var s: String? = s
                s = if (s == "") null else MyContract.Stop.COLUMN_NAME + " LIKE '%" + s + "%'"
                connectAdapter(s)
                return true
            }
        })
    }
}