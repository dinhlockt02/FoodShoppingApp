package xyz.daijoubuteam.foodshoppingapp.client.ordercheckout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemOrderItemBinding
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem

class OrderItemAdapter: ListAdapter<BagOrderItem, OrderItemAdapter.OrderItemViewHolder>(DiffCallBack) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):OrderItemViewHolder{
        return OrderItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val oderItem = getItem(position)
        holder.bind(oderItem)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<BagOrderItem>(){
        override fun areItemsTheSame(oldItem: BagOrderItem, newItem: BagOrderItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: BagOrderItem, newItem: BagOrderItem): Boolean {
            return oldItem == newItem
        }
    }

    class OrderItemViewHolder private constructor(val binding: ItemOrderItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: BagOrderItem) {
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