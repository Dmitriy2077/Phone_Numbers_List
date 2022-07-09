package mingaz.lubinskiy.app.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import mingaz.lubinskiy.app.databinding.ActivityEmployeesListBinding
import mingaz.lubinskiy.app.department.Department
import mingaz.lubinskiy.app.department.DEPARTMENT
import mingaz.lubinskiy.app.employee_info.EMPLOYEE
import mingaz.lubinskiy.app.employee_info.EmployeeInfoActivity

class EmployeesListActivity : AppCompatActivity(), EmployeesAdapter.OnItemClickListener {
    private lateinit var binding: ActivityEmployeesListBinding
    private var adapter = EmployeesAdapter(this)
    private var employeesList: MutableList<Employee>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.employeeToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val department = intent.getSerializableExtra(DEPARTMENT) as Department
        employeesList = department.employees
        binding.titleToolbar.text = department.name.toString()
        binding.titleToolbar.isSelected = true

        buildRecyclerView()
    }

    override fun onClick(employee: Employee) {
        startActivity(
            Intent(
                this,
                EmployeeInfoActivity::class.java
            ).apply {
                putExtra(EMPLOYEE, employee)
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    private fun buildRecyclerView() = with(binding) {
        employeesList?.sortWith(compareBy { it.name })
        adapter.submitList(employeesList)
        employeesListRv.adapter = adapter
    }
}
