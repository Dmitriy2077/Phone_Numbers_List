package mingaz.lubinskiy.app.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import mingaz.lubinskiy.app.databinding.ActivityEmployeeInfoBinding
import mingaz.lubinskiy.app.ui.adapters.employee_info.DepartmentNameAdapter
import mingaz.lubinskiy.app.ui.adapters.employee_info.NumberAdapter
import mingaz.lubinskiy.app.ui.adapters.employee_info.PositionAdapter
import mingaz.lubinskiy.app.entities.Department
import mingaz.lubinskiy.app.entities.Employee
import mingaz.lubinskiy.app.entities.EmployeeInfo
import mingaz.lubinskiy.app.utils.DEPARTMENT
import mingaz.lubinskiy.app.utils.EMPLOYEE

class EmployeeInfoActivity : AppCompatActivity(), NumberAdapter.OnItemClickListener,
    DepartmentNameAdapter.OnItemClickListener {
    private lateinit var binding: ActivityEmployeeInfoBinding
    private lateinit var department: Department
    private var info: EmployeeInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.employeeInfoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        department = intent.getSerializableExtra(DEPARTMENT) as Department
        val employee = intent.getSerializableExtra(EMPLOYEE) as Employee
        info = employee.info
        binding.titleToolbar.text = employee.name.toString()
        binding.titleToolbar.isSelected = true

        buildRecyclerView(employee)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, EmployeesListActivity::class.java).apply {
            putExtra(DEPARTMENT, department)
        })
        finishAffinity()
    }

    private fun buildRecyclerView(employee: Employee) {
        val numbersHashMap = employee.info?.numbers
        val numbersList: MutableList<HashMap<String, Long>?> = mutableListOf()

        numbersHashMap!!.keys.forEach { key ->
            val map: HashMap<String, Long> = HashMap()
            map[key] = numbersHashMap.getValue(key).toLong()
            numbersList.add(map)
        }

        val departmentAdapter = DepartmentNameAdapter(department.name.toString(), this)
        val positionAdapter = PositionAdapter(info?.position.toString())
        val numberAdapter = NumberAdapter(this)

        numberAdapter.submitList(numbersList)
        val commonAdapter = ConcatAdapter(departmentAdapter, positionAdapter, numberAdapter)

        binding.employeeInfoRv.adapter = commonAdapter
    }

    override fun onClickNumber(view: TextView) {
        startActivity(Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${view.text}")
        })
    }

    override fun onClickDepartment() {
        startActivity(Intent(this, EmployeesListActivity::class.java).apply {
            putExtra(DEPARTMENT, department)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}
