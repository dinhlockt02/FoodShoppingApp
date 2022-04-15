package xyz.daijoubuteam.foodshoppingapp.client.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemOrdersBinding

import xyz.daijoubuteam.foodshoppingapp.model.Orders

class OrdersApdater: ListAdapter<Orders, OrdersApdater.OrdersViewHolder>(DiffCallBack) {
    class OrdersViewHolder(private var binding: ItemOrdersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Orders) {
            binding.order = order
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Orders>() {
        override fun areItemsTheSame(oldItem: Orders, newItem: Orders): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Orders, newItem: Orders): Boolean {
            return oldItem.titleOrders == newItem.titleOrders
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val binding = ItemOrdersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        var orderItems = getItem(position)
        holder.bind(orderItems)
    }
}