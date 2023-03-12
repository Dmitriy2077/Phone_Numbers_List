package mingaz.lubinskiy.app.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mingaz.lubinskiy.app.R
import mingaz.lubinskiy.app.databinding.ActivityDepartmentsListBinding
import mingaz.lubinskiy.app.entities.Department
import mingaz.lubinskiy.app.entities.Employee
import mingaz.lubinskiy.app.ui.adapters.department.DepartmentsAdapter
import mingaz.lubinskiy.app.ui.adapters.employee.EmployeesAdapter
import mingaz.lubinskiy.app.utils.DEPARTMENT
import mingaz.lubinskiy.app.utils.DialogManager
import mingaz.lubinskiy.app.utils.EMPLOYEE
import mingaz.lubinskiy.app.utils.IS_ADMIN

class DepartmentsEmployeesListActivity : AppCompatActivity(),
    DepartmentsAdapter.OnItemClickListener, DepartmentsAdapter.OnItemLongClickListener,
    EmployeesAdapter.OnItemClickListener, EmployeesAdapter.OnItemLongClickListener {
    private lateinit var binding: ActivityDepartmentsListBinding
    var departmentsAdapter = DepartmentsAdapter(this, this)
    var employeesAdapter = EmployeesAdapter(this, this)
    private lateinit var departmentsList: MutableList<Department>
    private lateinit var employeesList: MutableList<Employee>
    var isDepartment = true
    var isAdmin = IS_ADMIN
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.departmentsToolbar)

        val database = Firebase.database(getString(R.string.db_url))
        val ref = database.getReference("departments")
        reference = ref
        ref.keepSynced(true)// подкачивание изменений
        Thread { onChangeListener(ref) }.start()

        buildRecyclerView()
    }

    override fun onClickDepartment(department: Department) {
        startActivity(
            Intent(
                this@DepartmentsEmployeesListActivity,
                EmployeesListActivity::class.java
            ).apply {
                putExtra(DEPARTMENT, department)
            })
        finishAffinity()
    }

    override fun onLongClickDepartment(department: Department, position: Int) {
        if (isAdmin) {
            DialogManager.changeDepNameDialog(this, department, object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    departmentsList.forEach {
                        if (it == department) {
                            it.name = name
                        }
                    }
                    val setLink =
                        reference.database.getReferenceFromUrl(department.referenceUrl.toString())
                    setLink.setValue(name)
                    buildRecyclerView()
                    departmentsAdapter.submitList(departmentsList)
                }
            })
        }
    }

    override fun onClickEmployee(employee: Employee) {
        departmentsList.forEach { dep ->
            dep.employees?.forEach { emp ->
                if (emp == employee) {
                    startActivity(
                        Intent(
                            this@DepartmentsEmployeesListActivity,
                            EmployeeInfoActivity::class.java
                        ).apply {
                            putExtra(DEPARTMENT, dep)
                            putExtra(EMPLOYEE, employee)
                        })
                }
            }
        }
    }

    override fun onLongClickEmployee(employee: Employee, position: Int) {
        if (isAdmin) {
            DialogManager.changeEmpNamePosDialog(
                this,
                employee,
                object : DialogManager.EmpListener {
                    override fun onEmpClick(name: String?, empPosition: String?) {
                        employeesList.forEach {
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
                        employeesAdapter.submitList(employeesList)
                    }
                })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_common, menu)
        val search = menu.findItem(R.id.action_search)
        val sv = search.actionView as SearchView
        sv.queryHint = "Поиск..."
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String?): Boolean {
                if (isDepartment) filterDepartmentsList(s)
                else filterEmployeesList(s)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deps_menu -> {
                isDepartment = true
                binding.departmentsToolbar.title = getString(R.string.appbar_dep_list)
                binding.departmentsListRv.adapter = departmentsAdapter
                departmentsAdapter.submitList(departmentsList)
            }
            R.id.emps_menu -> {
                isDepartment = false
                binding.departmentsToolbar.title = getString(R.string.appbar_emp_list)
                binding.departmentsListRv.adapter = employeesAdapter
                employeesAdapter.submitList(employeesList)
            }
        }
        return true
    }

    private fun filterDepartmentsList(input: String?) {
        val filteredList = ArrayList<Department>()
        for (item in departmentsList) {
            if (item.name?.lowercase()!!.contains(input?.lowercase() ?: "-")) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Таких подразделений нет!", Toast.LENGTH_SHORT)
                .show()
        }
        departmentsAdapter.submitList(filteredList)
    }

    private fun filterEmployeesList(input: String?) {
        val filteredList = ArrayList<Employee>()
        for (item in employeesList) {
            if (item.name?.lowercase()!!.contains(input?.lowercase() ?: "-")) {
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Таких сотрудников нет!", Toast.LENGTH_SHORT)
                .show()
        }
        employeesAdapter.submitList(filteredList)
    }

    private fun buildRecyclerView() {
        binding.departmentsListRv.adapter = departmentsAdapter
    }

    private fun onChangeListener(ref: DatabaseReference) {
        // Read from the database
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            override fun onDataChange(snapshot: DataSnapshot) {
                employeesList = mutableListOf()
                departmentsList = createDepartmentsList(snapshot)
                departmentsList.sortWith(compareBy { it.name })
                departmentsAdapter.submitList(departmentsList)
                employeesList.sortWith(compareBy { it.name })
            }

            // Failed to read value
            override fun onCancelled(error: DatabaseError) {
                Log.d("Departments01234", "Failed to read value.", error.toException())
            }
        })
    }

    fun createDepartmentsList(snapshot: DataSnapshot): MutableList<Department> {
        val list: MutableList<Department> = mutableListOf()
        snapshot.children.forEach { departments ->
            var departmentsName: String
            var employeesList: MutableList<Employee> = mutableListOf()
            departments.children.forEach { department ->
                if (employeesList.isEmpty()) {
                    employeesList = createEmployeesList(department)
                }
                if (department.value?.javaClass == String::class.java) {
                    departmentsName = department.value.toString()
                    list.add(Department(departmentsName, employeesList, department.ref.toString()))
                }
            }
        }
        return list
    }

    private fun createEmployeesList(employee: DataSnapshot): MutableList<Employee> {
        val list: MutableList<Employee> = mutableListOf()
        employee.children.forEach {
            val item = it.getValue(Employee::class.java)
            if (item != null) {
                item.referenceUrl = it.ref.toString()
                list.add(item)
                employeesList.add(item)
            }
        }
        return list
    }
}
