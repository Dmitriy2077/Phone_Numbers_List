package mingaz.lubinskiy.app.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.OnItemClickListener
import mingaz.lubinskiy.app.R
import mingaz.lubinskiy.app.department.DEPARTMENT_NAME
import kotlin.collections.ArrayList

class EmployeesListActivity : AppCompatActivity() {
    private lateinit var employeeRV: RecyclerView

    // variable for our adapter class and array list
    private lateinit var employeesAdapter: EmployeesAdapter
    private lateinit var employeesArrayList: ArrayList<Employee>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employees_list)
        val toolbar = findViewById<Toolbar>(R.id.employee_toolbar)
        val bundle: Bundle = intent.extras!!
        val eName = bundle.get(DEPARTMENT_NAME).toString()
        toolbar?.title = eName
        buildRecyclerView()
    }

    private fun buildRecyclerView() {
        // initializing our variables.
        employeeRV = findViewById(R.id.employees_list_rv)
        // below line we are creating a new array list
        employeesArrayList = ArrayList()
        employeesArrayList = employeesList()

        // initializing our adapter class.
        employeesAdapter = EmployeesAdapter(employeesArrayList)

        // setting adapter to our recycler view.
        employeeRV.adapter = employeesAdapter
        employeesAdapter.setOnClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@EmployeesListActivity, EmployeeInfoActivity::class.java)
                val employeeName = employeesArrayList[position].fullName
                intent.putExtra(EMPLOYEE_NAME, employeeName)
                startActivity(intent)
            }
        })
    }
}