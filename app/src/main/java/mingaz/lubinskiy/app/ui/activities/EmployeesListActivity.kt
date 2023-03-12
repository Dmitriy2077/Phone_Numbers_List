package mingaz.lubinskiy.app.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mingaz.lubinskiy.app.databinding.ActivityEmployeesListBinding
import mingaz.lubinskiy.app.entities.Department
import mingaz.lubinskiy.app.entities.Employee
import mingaz.lubinskiy.app.ui.adapters.employee.EmployeesAdapter
import mingaz.lubinskiy.app.utils.*

class EmployeesListActivity : AppCompatActivity(), EmployeesAdapter.OnItemClickListener,
EmployeesAdapter.OnItemLongClickListener{
    private lateinit var binding: ActivityEmployeesListBinding
    private lateinit var department: Department
    private var adapter = EmployeesAdapter(this, this)
    private var employeesList: MutableList<Employee>? = mutableListOf()
    var isAdmin = IS_ADMIN
    private val database = Firebase.database
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.employeeToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        department = intent.getSerializableExtra(DEPARTMENT) as Department
        reference = database.getReferenceFromUrl(department.referenceUrl.toString())
        employeesList = department.employees
        binding.titleToolbar.text = department.name.toString()
        binding.titleToolbar.isSelected = true

        buildRecyclerView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DepartmentsEmployeesListActivity::class.java))
    }

    override fun onClickEmployee(employee: Employee) {
        startActivity(
            Intent(
                this,
                EmployeeInfoActivity::class.java
            ).apply {
                putExtra(DEPARTMENT, department)
                putExtra(EMPLOYEE, employee)
            })
    }

    override fun onLongClickEmployee(employee: Employee, position: Int) {
        if (isAdmin) {
            DialogManager.changeEmpNamePosDialog(
                this,
                employee,
                object : DialogManager.EmpListener {
                    override fun onEmpClick(name: String?, empPosition: String?) {
                        employeesList?.forEach {
                            if (it == employee) {
                                it.name = name
                                it.info?.position = empPosition
                            }
                        }
                        val setEmpNameLink =
                            reference.database.getReferenceFromUrl("${employee.referenceUrl.toString()}/name")
                        setEmpNameLink.setValue(name)
                        val setEmpPosLink =
                            reference.database.getReferenceFromUrl("${employee.referenceUrl.toString()}/info/position")
                        setEmpPosLink.setValue(empPosition)
                        buildRecyclerView()
                        adapter.submitList(employeesList)
                    }
                })
        }
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
