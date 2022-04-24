package xyz.daijoubuteam.foodshoppingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.LayoutAddressItemBinding
import xyz.daijoubuteam.foodshoppingapp.model.ShippingAddress

class AddressAdapter: ListAdapter<ShippingAddress, AddressAdapter.AddressViewHolder>(AddressAdapter.DiffCallback) {
    class AddressViewHolder(private var binding: LayoutAddressItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(shippingAddress: ShippingAddress){
            binding.shippingAddress = shippingAddress
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ShippingAddress>() {
        override fun areItemsTheSame(oldItem: ShippingAddress, newItem: ShippingAddress): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ShippingAddress,
            newItem: ShippingAddress
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val binding = LayoutAddressItemBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return AddressAdapter.AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)
    }
}