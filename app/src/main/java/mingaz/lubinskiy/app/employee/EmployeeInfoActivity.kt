package mingaz.lubinskiy.app.employee

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import mingaz.lubinskiy.app.R

const val EMPLOYEE_NAME = "employeeName"

class EmployeeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_info)
        val toolbar = findViewById<Toolbar>(R.id.employee_info_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle = intent.extras!!
        val employeeName = bundle.get(EMPLOYEE_NAME).toString()
        toolbar?.title = employeeName
    }

    fun dialCall(v: View?){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:+375001234567")
        //intent.data = Uri.parse(v?.text.toString())
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }
}