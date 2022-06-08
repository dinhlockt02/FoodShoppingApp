package xyz.daijoubuteam.foodshoppingapp.client.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemOrderInfoBinding
import xyz.daijoubuteam.foodshoppingapp.model.Order

class OrderAdapter: ListAdapter<Order, OrderAdapter.OrderViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val oderItem = getItem(position)
        holder.bind(oderItem)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    class OrderViewHolder private constructor(val binding: ItemOrderInfoBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Order) {
            binding.order = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): OrderViewHolder {
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = ItemOrderInfoBinding.inflate(layoutInflate, parent, false)
                return OrderViewHolder(binding)
            }
        }
    }
}