package xyz.daijoubuteam.foodshoppingapp.client.bag

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemOrderBinding
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrder

class BagOrderAdapter(private val onClickListener: OnClickListener) : ListAdapter<BagOrder, BagOrderAdapter.OrderViewHolder>(
    DiffCallBack
) {
    class OrderViewHolder(private var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: BagOrder) {
            binding.order = order
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<BagOrder>() {
        override fun areItemsTheSame(oldItem: BagOrder, newItem: BagOrder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BagOrder, newItem: BagOrder): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val orderItem = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(orderItem)
        }
        holder.bind(orderItem)
    }

    class OnClickListener(val clickListener: (order: BagOrder) -> Unit) {
        fun onClick(order: BagOrder) = clickListener(order)
    }
}