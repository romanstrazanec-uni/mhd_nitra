package sk.romanstrazanec.mhdnitra

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class TimeAddActivity : AppCompatActivity() {
    private var s1: Spinner? = null
    private var s2: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_add)
        s1 = findViewById(R.id.spinner1)
        s2 = findViewById(R.id.spinner2)
        connectSpinnerAdapter(s1, R.array.weekend)
        connectSpinnerAdapter(s2, R.array.holidays)
    }

    fun addTime() {
        val et = findViewById<EditText>(R.id.editText)
        val time = et.text.toString()
        if (validTime(time)) {
            val i = Intent()
            i.putExtra("hour", et.text.toString().split(":".toRegex()).toTypedArray()[0].toInt())
            i.putExtra("minute", et.text.toString().split(":".toRegex()).toTypedArray()[1].toInt())
            i.putExtra("weekend", s1!!.selectedItemPosition)
            i.putExtra("holidays", s2!!.selectedItemPosition)
            setResult(RESULT_OK, i)
            finish()
        } else {
            Toast.makeText(this, "Nesprávny formát času.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectSpinnerAdapter(s: Spinner?, array: Int) {
        val adapter = ArrayAdapter.createFromResource(this, array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        s!!.adapter = adapter
    }

    private fun validTime(time: String): Boolean {
        try {
            val hour = time.split(":".toRegex()).toTypedArray()[0].toInt()
            val minute = time.split(":".toRegex()).toTypedArray()[1].toInt()
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) return false
        } catch (e: NumberFormatException) {
            return false
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
        return true
    }
}