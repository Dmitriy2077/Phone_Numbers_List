package mingaz.lubinskiy.app.ui.adapters.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.databinding.DepartmentRvItemBinding
import mingaz.lubinskiy.app.entities.Department

class DepartmentsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Department, DepartmentsAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: DepartmentRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(department: Department, listener: OnItemClickListener) = with(binding) {
            departmentName.text = department.name
            itemView.setOnClickListener {
                listener.onClickDepartment(department)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    DepartmentRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<Department>() {
        override fun areItemsTheSame(oldItem: Department, newItem: Department): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Department, newItem: Department): Boolean {
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
        fun onClickDepartment(department: Department)
    }
}
