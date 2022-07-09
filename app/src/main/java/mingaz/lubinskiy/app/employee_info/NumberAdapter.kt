package mingaz.lubinskiy.app.employee_info

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mingaz.lubinskiy.app.databinding.NumberRvItemBinding

class NumberAdapter(private val listener: OnItemClickListener) :
    ListAdapter<HashMap<String, Long>, NumberAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: NumberRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            numbers: HashMap<String, Long>? = HashMap(),
            listener: OnItemClickListener
        ) = with(binding) {
            employeeNumberName.text = formatString(numbers?.keys.toString())
            employeeNumber.text = formatString(numbers!!.values.toString())
            itemView.setOnClickListener {
                listener.onClick(employeeNumber)
            }
        }

        private fun formatString(s: String): String {
            return if (s.contains(Regex("[0-9]{9,12}")))
                "+${s.substring(1, s.length - 1)}"
            else
                "${s.substring(1, s.length - 1)}:"
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    NumberRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    class ItemComparator : DiffUtil.ItemCallback<HashMap<String, Long>>() {
        override fun areItemsTheSame(
            oldItem: HashMap<String, Long>,
            newItem: HashMap<String, Long>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HashMap<String, Long>,
            newItem: HashMap<String, Long>
        ): Boolean {
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
        fun onClick(view: TextView)
    }
}
