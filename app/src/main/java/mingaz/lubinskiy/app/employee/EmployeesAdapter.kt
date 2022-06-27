package mingaz.lubinskiy.app.employee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.OnItemClickListener
import mingaz.lubinskiy.app.R

class EmployeesAdapter(private var employeesArrayList: ArrayList<Employee>) :
    RecyclerView.Adapter<EmployeesAdapter.EmployeeVHolder?>() {
    private lateinit var clickListener: OnItemClickListener

    /* Creates and inflates view and return DepartmentVHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeVHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_rv_item, parent, false)
        return EmployeeVHolder(view, clickListener)
    }

    /* Gets current department and uses it to bind view. */
    override fun onBindViewHolder(holder: EmployeeVHolder, position: Int) {
        val employee = employeesArrayList[position]
        holder.employeeFullNameTV.text = employee.fullName
        holder.employeePositionTV.text = employee.position
    }

    // returning the size of array list.
    override fun getItemCount(): Int {
        return employeesArrayList.size
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        clickListener = listener
    }

    fun filterList(filterList: ArrayList<Employee>) {
        // below line is to add our filtered list in our course array list.
        employeesArrayList = filterList
        // below line is to notify our adapter as change in recycler view data.
        notifyDataSetChanged()
    }

    /* ViewHolder for Department, takes in the inflated view and the onClick behavior. */
    class EmployeeVHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val employeeFullNameTV: TextView = itemView.findViewById(R.id.employee_name)
        val employeePositionTV: TextView = itemView.findViewById(R.id.employee_position)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}