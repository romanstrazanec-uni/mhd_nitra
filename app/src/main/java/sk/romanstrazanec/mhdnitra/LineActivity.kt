package sk.romanstrazanec.mhdnitra

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.TextView

class LineActivity : AppCompatActivity() {
    var dbh = DBHelper(this)
    var lv: ListView? = null
    var tv: TextView? = null
    private var lineId: Long = 0
    var direction = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_line)
        lineId = intent.getLongExtra("line_id", 0)
        lv = findViewById(R.id.line_stop_list_view)
        tv = findViewById(R.id.line_text_view)
        tv?.setBackgroundColor(Color.rgb(190, 190, 220))
        connectAdapter(direction)
        addOnItemClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.line_stop_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.line_stop_menu_order -> {
                direction = if (direction == 0) 1 else 0
                connectAdapter(direction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun connectAdapter(direction: Int) {
        lv!!.adapter = SimpleCursorAdapter(this, R.layout.line_stop_list_layout,
                dbh.getCursor(arrayOf(MyContract.LineStop.TABLE_NAME + "." + MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME),
                        MyContract.LineStop.TABLE_NAME, arrayOf(MyContract.Stop.TABLE_NAME), arrayOf(MyContract.LineStop.COLUMN_ID_STOP), arrayOf(MyContract.Stop.COLUMN_ID),
                        MyContract.LineStop.COLUMN_ID_LINE + " = " + lineId + " AND " + MyContract.LineStop.COLUMN_DIRECTION + " = " + direction,
                        null, null), arrayOf(MyContract.LineStop.COLUMN_ID, MyContract.Stop.COLUMN_NAME), intArrayOf(R.id.line_stop_id, R.id.stop_name), 0)
        tv!!.text = String.format("Trasa linky %s, smer %s", lineName, lastStopName)
    }

    private fun addOnItemClickListener() {
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            c.moveToPosition(position)
            val i = Intent(this@LineActivity, TimesActivity::class.java)
            i.putExtra("line", lineName)
            i.putExtra("stop", c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)))
            i.putExtra("direction", lastStopName)
            i.putExtra("line_stop_id", c.getLong(c.getColumnIndex(MyContract.LineStop.COLUMN_ID)))
            startActivity(i)
        }
    }

    private val lastStopName: String?
        get() {
            val c = (lv!!.adapter as SimpleCursorAdapter).cursor
            return if (c.moveToLast()) c.getString(c.getColumnIndex(MyContract.Stop.COLUMN_NAME)) else null
        }
    private val lineName: String
        get() {
            val c = dbh.getCursor(null, MyContract.Line.TABLE_NAME, null, null, null,
                    MyContract.Line.COLUMN_ID + " = " + lineId, null, null)
            c.moveToFirst()
            return c.getString(c.getColumnIndex(MyContract.Line.COLUMN_LINE))
        }
}