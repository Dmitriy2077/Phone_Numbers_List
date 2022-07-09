package mingaz.lubinskiy.app.employee_info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import mingaz.lubinskiy.app.databinding.ActivityEmployeeInfoBinding
import mingaz.lubinskiy.app.employee.Employee

const val EMPLOYEE = "currentEmployee"

class EmployeeInfoActivity : AppCompatActivity(), NumberAdapter.OnItemClickListener {
    private lateinit var binding: ActivityEmployeeInfoBinding
    private var info: EmployeeInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.employeeInfoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val employee = intent.getSerializableExtra(EMPLOYEE) as Employee
        info = employee.info
        binding.titleToolbar.text = employee.name.toString()
        binding.titleToolbar.isSelected = true

        buildRecyclerView(employee)
    }

    private fun buildRecyclerView(employee: Employee) {
        val numbersHashMap = employee.info?.numbers
        val numbersList : MutableList<HashMap<String, Long>?> = mutableListOf()

        numbersHashMap!!.keys.forEach { key ->
            val map: HashMap<String, Long> = HashMap()
            map[key] = numbersHashMap.getValue(key).toLong()
            numbersList.add(map)
        }

        val positionAdapter = PositionAdapter(info?.position.toString())
        val numberAdapter = NumberAdapter(this)

        numberAdapter.submitList(numbersList)
        val commonAdapter = ConcatAdapter(positionAdapter, numberAdapter)

        binding.employeeInfoRv.adapter = commonAdapter
    }

    override fun onClick(view: TextView) {
        startActivity(Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+${view.text}")
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressed()
        }
        return true
    }
}
