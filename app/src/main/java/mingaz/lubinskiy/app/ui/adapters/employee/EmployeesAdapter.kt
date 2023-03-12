package mingaz.lubinskiy.app.ui.adapters.employee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.databinding.EmployeeRvItemBinding
import mingaz.lubinskiy.app.entities.Employee

class EmployeesAdapter(private val listener: OnItemClickListener,
                       private val longListener: OnItemLongClickListener
) :
    ListAdapter<Employee, EmployeesAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: EmployeeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee, listener: OnItemClickListener,
                 longListener: OnItemLongClickListener) = with(binding) {
            employeeName.text = employee.name
            employeePosition.text = employee.info?.position
            itemView.setOnClickListener {
                listener.onClickEmployee(employee)
            }
            itemView.setOnLongClickListener {
                longListener.onLongClickEmployee(employee, layoutPosition)
                return@setOnLongClickListener true
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    EmployeeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<Employee>() {
        override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), listener, longListener)
    }

    interface OnItemClickListener {
        fun onClickEmployee(employee: Employee)
    }

    interface OnItemLongClickListener {
        fun onLongClickEmployee(employee: Employee, position: Int)
    }
}
