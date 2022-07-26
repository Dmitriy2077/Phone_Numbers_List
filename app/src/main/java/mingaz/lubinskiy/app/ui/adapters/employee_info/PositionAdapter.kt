package mingaz.lubinskiy.app.ui.adapters.employee_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.databinding.PositionRvItemBinding

class PositionAdapter(private val positionName: String): RecyclerView.Adapter<PositionAdapter.ItemHolder>() {

    class ItemHolder(private val binding: PositionRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(positionName: String) = with(binding) {
            employeePositionName.text = "Должность:"
            employeePosition.text = positionName
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    PositionRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(positionName)
    }

    override fun getItemCount(): Int {
        return 1
    }
}
