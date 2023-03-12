package mingaz.lubinskiy.app.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mingaz.lubinskiy.app.databinding.ActivityEmployeeInfoBinding
import mingaz.lubinskiy.app.entities.Department
import mingaz.lubinskiy.app.entities.Employee
import mingaz.lubinskiy.app.entities.EmployeeInfo
import mingaz.lubinskiy.app.ui.adapters.employee_info.DepartmentNameAdapter
import mingaz.lubinskiy.app.ui.adapters.employee_info.NumberAdapter
import mingaz.lubinskiy.app.ui.adapters.employee_info.PositionAdapter
import mingaz.lubinskiy.app.utils.DEPARTMENT
import mingaz.lubinskiy.app.utils.DialogManager
import mingaz.lubinskiy.app.utils.EMPLOYEE
import mingaz.lubinskiy.app.utils.IS_ADMIN

class EmployeeInfoActivity : AppCompatActivity(), NumberAdapter.OnItemClickListener,
    DepartmentNameAdapter.OnItemClickListener, PositionAdapter.OnItemLongClickListener,
    NumberAdapter.OnItemLongClickListener {
    private lateinit var binding: ActivityEmployeeInfoBinding
    private lateinit var department: Department
    private lateinit var employee: Employee
    private var info: EmployeeInfo? = null
    private lateinit var positionAdapter: PositionAdapter
    private lateinit var employeePosition: String
    private lateinit var numberAdapter: NumberAdapter
    private lateinit var numbersList: MutableList<HashMap<String, Long>?>

    var isAdmin = IS_ADMIN
    private val database = Firebase.database
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.employeeInfoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        department = intent.getSerializableExtra(DEPARTMENT) as Department
        employee = intent.getSerializableExtra(EMPLOYEE) as Employee
        reference = database.getReferenceFromUrl(employee.referenceUrl.toString())
        info = employee.info
        binding.titleToolbar.text = employee.name.toString()
        binding.titleToolbar.isSelected = true

        employeePosition = info?.position.toString()
        numbersList = mutableListOf()

        buildRecyclerView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, EmployeesListActivity::class.java).apply {
            putExtra(DEPARTMENT, department)
        })
        finishAffinity()
    }

    private fun buildRecyclerView() {
        val numbersHashMap = employee.info?.numbers
        if (numbersList.isEmpty()) {
            numbersHashMap!!.keys.forEach { key ->
                val map: HashMap<String, Long> = HashMap()
                map[key] = numbersHashMap.getValue(key).toLong()
                numbersList.add(map)
            }
        }

        val departmentAdapter = DepartmentNameAdapter(department.name.toString(), this)
        positionAdapter = PositionAdapter(employeePosition, this)
        numberAdapter = NumberAdapter(this, this)

        numberAdapter.submitList(numbersList)
        val commonAdapter = ConcatAdapter(departmentAdapter, positionAdapter, numberAdapter)

        binding.employeeInfoRv.adapter = commonAdapter
    }

    override fun onLongClickPosition(position: String?) {
        if (isAdmin) {
            DialogManager.changeEmpPosDialog(this, position, object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    employeePosition = name ?: "ошибка"
                    val setPosLink =
                        reference.database.getReferenceFromUrl("${employee.referenceUrl.toString()}/info/position")
                    setPosLink.setValue(employeePosition)
                    buildRecyclerView()
                }
            })
        }
    }

    override fun onClickNumber(view: TextView) {
        startActivity(Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${view.text}")
        })
    }

    override fun onLongClickNumber(number: String, name: String, position: Int) {
        if (isAdmin) {
            DialogManager.changeEmpNumDialog(
                this,
                number,
                object : DialogManager.Listener {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onClick(newNumber: String?) {
                        var tempPair: HashMap<String, Long> = hashMapOf()
                        numbersList.forEach { map ->
                            if (map?.contains(name) == true) {
                                tempPair = map
                            }
                        }
                        numbersList.remove(tempPair)
                        tempPair.clear()
                        tempPair[name] = newNumber?.toLong() ?: number.toLong()
                        numbersList.add(position - 2, tempPair)
                        val setNumLink =
                            reference.database.getReferenceFromUrl("${employee.referenceUrl.toString()}/info/numbers")
                        setNumLink.child(name).setValue(tempPair.getValue(name))
                        buildRecyclerView()
                    }
                })
        }
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
