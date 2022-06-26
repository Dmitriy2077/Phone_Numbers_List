package mingaz.lubinskiy.app.department

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.R

class DepartmentsAdapter(private var departmentsArrayList: ArrayList<Department>) :
    RecyclerView.Adapter<DepartmentsAdapter.DepartmentVHolder?>() {

    private lateinit var mlistener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: onItemClickListener) {
        mlistener = listener
    }

    /*fun setOnClickListener(listener: Unit) {
        mlistener = listener
    }*/
    /* Creates and inflates view and return DepartmentVHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentVHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.department_rv_item, parent, false)
        return DepartmentVHolder(view, mlistener)
    }

    /* Gets current department and uses it to bind view. */
    override fun onBindViewHolder(holder: DepartmentVHolder, position: Int) {
        val department = departmentsArrayList[position]
        holder.departmentTextView.text = department.name
    }

    // returning the size of array list.
    override fun getItemCount(): Int {
        return departmentsArrayList.size
    }

    fun filterList(filterList: ArrayList<Department>) {
        // below line is to add our filtered list in our course array list.
        departmentsArrayList = filterList
        // below line is to notify our adapter as change in recycler view data.
        notifyDataSetChanged()
    }

    /* ViewHolder for Department, takes in the inflated view and the onClick behavior. */
    class DepartmentVHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val departmentTextView: TextView = itemView.findViewById(R.id.department_name)

        init {
            /*itemView.setOnClickListener {
                currentDepartment?.let {
                    onClick(it)
                }
            }*/
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}
