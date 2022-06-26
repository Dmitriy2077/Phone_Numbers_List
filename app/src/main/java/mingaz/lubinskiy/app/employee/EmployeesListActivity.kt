package mingaz.lubinskiy.app.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import mingaz.lubinskiy.app.R
import mingaz.lubinskiy.app.department.EMPLOYEE_NAME

class EmployeesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees_list)
        val toolbar = findViewById<Toolbar>(R.id.employee_toolbar)
        val bundle: Bundle = intent.extras!!
        val eName = bundle.get(EMPLOYEE_NAME).toString()
        toolbar?.title = eName
    }
}