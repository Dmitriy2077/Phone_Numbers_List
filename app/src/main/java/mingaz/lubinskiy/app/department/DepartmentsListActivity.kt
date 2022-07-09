package mingaz.lubinskiy.app.department

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mingaz.lubinskiy.app.employee.Employee
import mingaz.lubinskiy.app.R
import mingaz.lubinskiy.app.databinding.ActivityDepartmentsListBinding
import mingaz.lubinskiy.app.employee.EmployeesListActivity

const val DEPARTMENT = "currentDepartment"

class DepartmentsListActivity : AppCompatActivity(), DepartmentsAdapter.OnItemClickListener {
    private lateinit var binding: ActivityDepartmentsListBinding
    var adapter = DepartmentsAdapter(this)
    lateinit var departmentsList: MutableList<Department>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.departmentsToolbar)

        val database = Firebase.database(getString(R.string.db_url))
        val ref = database.getReference("departments")
        ref.keepSynced(true)// подкачивание изменений
        Thread { onChangeListener(ref) }.start()

        buildRecyclerView()
    }

    override fun onClick(department: Department) {
        startActivity(
            Intent(
                this@DepartmentsListActivity,
                EmployeesListActivity::class.java
            ).apply {
                putExtra(DEPARTMENT, department)
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_departments, menu)
        val search = menu.findItem(R.id.action_search)
        val sv = search.actionView as SearchView
        sv.queryHint = "Поиск..."
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(s: String?): Boolean {
                filterList(s)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filterList(input: String?) {
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
        adapter.submitList(filteredList)
    }

    private fun buildRecyclerView() = with(binding) {
        departmentsListRv.adapter = adapter
    }

    private fun onChangeListener(ref: DatabaseReference) {
        // Read from the database
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            override fun onDataChange(snapshot: DataSnapshot) {
                departmentsList = createDepartmentsList(snapshot)
                departmentsList.sortWith(compareBy { it.name })
                adapter.submitList(departmentsList)
            }

            // Failed to read value
            override fun onCancelled(error: DatabaseError) {
                Log.d("Departments2077", "Failed to read value.", error.toException())
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
                    list.add(Department(departmentsName, employeesList))
                }
            }
        }
        return list
    }

    private fun createEmployeesList(employee: DataSnapshot): MutableList<Employee> {
        val list: MutableList<Employee> = mutableListOf()
        employee.children.forEach {
            val item = it.getValue(Employee::class.java)
            if (item != null) list.add(item)
        }
        return list
    }
}
