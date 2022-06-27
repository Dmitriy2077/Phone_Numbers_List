package mingaz.lubinskiy.app.employee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import mingaz.lubinskiy.app.R
import mingaz.lubinskiy.app.department.DEPARTMENT_NAME

const val EMPLOYEE_NAME = "employeeName"

class EmployeeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_info)
        val toolbar = findViewById<Toolbar>(R.id.employee_info_toolbar)
        val bundle: Bundle = intent.extras!!
        val employeeName = bundle.get(EMPLOYEE_NAME).toString()
        toolbar?.title = employeeName
    }
}