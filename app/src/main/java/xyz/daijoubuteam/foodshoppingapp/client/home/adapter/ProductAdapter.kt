package xyz.daijoubuteam.foodshoppingapp.client.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemProductBinding
import xyz.daijoubuteam.foodshoppingapp.model.Product

class ProductAdapter: ListAdapter<Product, ProductAdapter.ProductViewHolder>(DiffCallBack) {
    class ProductViewHolder(private var binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root) {
       fun bind(product: Product) {
           binding.product = product
       }
    }
    companion object DiffCallBack: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(productItem)
    }
}