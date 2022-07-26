package mingaz.lubinskiy.app.ui.adapters.employee_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.databinding.CurrentDepRvItemBinding

class DepartmentNameAdapter(
    private val departmentName: String,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<DepartmentNameAdapter.ItemHolder>() {

    class ItemHolder(private val binding: CurrentDepRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(departmentName: String, listener: OnItemClickListener) = with(binding) {
            currentDepName.text = "Подразделение:"
            currentDepartment.text = departmentName
            itemView.setOnClickListener {
                listener.onClickDepartment()
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    CurrentDepRvItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(departmentName, listener)
    }

    override fun getItemCount(): Int {
        return 1
    }

    interface OnItemClickListener {
        fun onClickDepartment()
    }
}
