package sk.romanstrazanec.mhdnitra

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import sk.romanstrazanec.mhdnitra.entities.Time

class TimesActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    private var lineStopId: Long = 0
    private var filter: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_times)
        val i = intent
        lineStopId = i.getLongExtra("line_stop_id", 0)
        val tv = findViewById<TextView>(R.id.times_text_view)
        tv.setBackgroundColor(Color.rgb(190, 190, 220))
        tv.text = String.format("%s, %s --> %s", i.getStringExtra("line"), i.getStringExtra("stop"), i.getStringExtra("direction"))
        lv = findViewById(R.id.times_list_view)
        filter = "workdays"
        connectAdapter(filter)
        addOnItemLongClickListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                val b = data.extras
                if (b != null) {
                    dbh.addTime(null, Time(0, lineStopId,
                            b.getInt("hour", 0),
                            b.getInt("minute", 0),
                            b.getInt("weekend", 0),
                            b.getInt("holidays", 0)))
                    connectAdapter(filter)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.time_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.time_menu_add -> {
                val i = Intent(this, TimeAddActivity::class.java)
                i.putExtra("line_stop_id", lineStopId)
                startActivityForResult(i, 1)
                true
            }
            R.id.time_menu_all -> {
                filter = "workdays"
                connectAdapter(filter)
                true
            }
            R.id.time_menu_weekend -> {
                filter = "weekend"
                connectAdapter(filter)
                true
            }
            R.id.time_menu_holidays -> {
                filter = "holidays"
                connectAdapter(filter)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun connectAdapter(filter: String?) {
        var where = MyContract.Time.COLUMN_ID_LINESTOP + " = " + lineStopId
        when (filter) {
            "workdays" -> where += " AND NOT " + MyContract.Time.COLUMN_WEEKEND + " = 1 AND NOT " + MyContract.Time.COLUMN_HOLIDAYS + " = 1"
            "weekend" -> where += " AND NOT " + MyContract.Time.COLUMN_WEEKEND + " = 0"
            "holidays" -> where += " AND NOT " + MyContract.Time.COLUMN_HOLIDAYS + " = 0"
            else -> {
            }
        }
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.times_list_layout, dbh.getCursor(null, MyContract.Time.TABLE_NAME,
                null, null, null, where,
                null, MyContract.Time.COLUMN_HOUR + ", " + MyContract.Time.COLUMN_MINUTE), arrayOf(MyContract.Time.COLUMN_ID, MyContract.Time.COLUMN_HOUR, MyContract.Time.COLUMN_MINUTE), intArrayOf(R.id.times_id, R.id.hour, R.id.minutes), 0)
    }

    private fun addOnItemLongClickListener() {
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, _, position, _ ->
            val builder = AlertDialog.Builder(parent.context)
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val id = c.getLong(c.getColumnIndex(MyContract.Time.COLUMN_ID))
            builder.setMessage(R.string.do_you_really_want_to_delete_this_item).setTitle(R.string.delete)
            builder.setPositiveButton(R.string.yes) { dialogInterface, _ ->
                dbh.deleteTime(id)
                connectAdapter(filter)
                dialogInterface.dismiss()
            }
            builder.setNegativeButton(R.string.no) { dialogInterface, _ -> dialogInterface.dismiss() }
            builder.show()
            true
        }
    }
}