package xyz.daijoubuteam.foodshoppingapp.client.home.search

import android.location.Location
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.MainApplication
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemSearchedEateryBinding
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemSearchedProductBinding
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Product
import xyz.daijoubuteam.foodshoppingapp.utils.observeOnce

class SearchEateryProductAdapter(): ListAdapter<Product, SearchEateryProductAdapter.SearchEateryProductViewHolder>(DiffCallBack) {
    class SearchEateryProductViewHolder(private var binding: ItemSearchedProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.product = product
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchEateryProductViewHolder {
        val binding =  ItemSearchedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchEateryProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchEateryProductViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(productItem)
    }
}