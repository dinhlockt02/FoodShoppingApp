package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemOrderItemBinding
import xyz.daijoubuteam.foodshoppingapp.model.OrderItem

class OrderCheckOutAdapter: ListAdapter<OrderItem, OrderCheckOutAdapter.OrderItemViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):OrderItemViewHolder{
        return OrderItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val oderItem = getItem(position)
        holder.bind(oderItem)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<OrderItem>(){
        override fun areItemsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: OrderItem, newItem: OrderItem): Boolean {
            return oldItem.productId == newItem.productId
        }
    }

    class OrderItemViewHolder private constructor(val binding: ItemOrderItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: OrderItem) {
            binding.orderItem = item
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): OrderItemViewHolder {
                val layoutInflate = LayoutInflater.from(parent.context)
                val binding = ItemOrderItemBinding.inflate(layoutInflate, parent, false)
                return OrderItemViewHolder(binding)
            }
        }
    }
}