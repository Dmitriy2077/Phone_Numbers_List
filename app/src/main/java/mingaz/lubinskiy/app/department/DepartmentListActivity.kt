package mingaz.lubinskiy.app.department

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.R
import mingaz.lubinskiy.app.employee.EmployeesListActivity
import java.util.*

/*const val FLOWER_ID = "flower id"
const val FLOWER_NAME = "name"
const val FLOWER_DESCRIPTION = "description"*/
const val EMPLOYEE_NAME = "e_name"

class DepartmentListActivity : AppCompatActivity() {
    // creating variables for our ui components.
    private lateinit var departmentRV: RecyclerView

    // variable for our adapter class and array list
    private lateinit var departmentsAdapter: DepartmentsAdapter
    private lateinit var departmentsArrayList: ArrayList<Department>

    //lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_list)
        setSupportActionBar(findViewById(R.id.app_toolbar))
    // calling method to build recycler view.
        buildRecyclerView()
    }

    private fun buildRecyclerView() {

        // initializing our variables.
        departmentRV = findViewById(R.id.recyclerView_dep_list)
        // below line we are creating a new array list
        departmentsArrayList = ArrayList()

        // below line is to add data to our array list.
        departmentsArrayList.add(Department(1, "First Member"))
        departmentsArrayList.add(Department(2, "Second Member"))
        departmentsArrayList.add(Department(3, "Third Member"))
        departmentsArrayList.add(Department(4, "Fourth Member"))
        departmentsArrayList.add(Department(5, "Fifth Member"))

        // initializing our adapter class.
        departmentsAdapter = DepartmentsAdapter(departmentsArrayList)

        // setting adapter to our recycler view.
        departmentRV.adapter = departmentsAdapter
        departmentsAdapter.setOnClickListener(object : DepartmentsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    this@DepartmentListActivity,
                    "OnClick - $position",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(this@DepartmentListActivity, EmployeesListActivity::class.java)
                val eName = departmentsArrayList[position].name
                intent.putExtra(EMPLOYEE_NAME, eName)
                startActivity(intent)
            }
        })
        //departmentsAdapter.setOnClickListener(onClick())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // inside inflater we are inflating our menu file.
        menuInflater.inflate(R.menu.menu_departments, menu)

        // below line is to get our menu item.
        val searchItem = menu.findItem(R.id.action_search)

        // getting search view of our item.
        val searchView = searchItem.actionView as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText)
                //Toast.makeText(applicationContext, "onQueryTextChange", Toast.LENGTH_LONG).show()
                return false
            }
        })
        return true

    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList = ArrayList<Department>()

        // running a for loop to compare elements.
        for (item in departmentsArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are adding it to our filtered list.
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered list to our adapter class.
            departmentsAdapter.filterList(filteredList)
        }
    }
}
