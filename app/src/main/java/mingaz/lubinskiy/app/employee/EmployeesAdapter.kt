package mingaz.lubinskiy.app.employee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.databinding.EmployeeRvItemBinding

class EmployeesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Employee, EmployeesAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: EmployeeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: Employee, listener: OnItemClickListener) = with(binding) {
            employeeName.text = employee.name
            employeePosition.text = employee.info?.position
            itemView.setOnClickListener {
                listener.onClick(employee)
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
        holder.bind(getItem(position), listener)
    }

    interface OnItemClickListener {
        fun onClick(employee: Employee)
    }
}
