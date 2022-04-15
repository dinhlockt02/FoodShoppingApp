package xyz.daijoubuteam.foodshoppingapp.client.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemCuisineBinding
import xyz.daijoubuteam.foodshoppingapp.model.Cuisine
import xyz.daijoubuteam.foodshoppingapp.model.Orders
import java.util.ArrayList

class CuisineAdapter : ListAdapter<Cuisine, CuisineAdapter.CuisineViewHolder>(DiffCallBack) {
    class CuisineViewHolder(private var binding : ItemCuisineBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (cuisine: Cuisine) {
            binding.cuisine = cuisine
            binding.imgAvatar.setImageResource(cuisine.imgAvatar)
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Cuisine>(){
        override fun areItemsTheSame(oldItem: Cuisine, newItem: Cuisine): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cuisine, newItem: Cuisine): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuisineViewHolder {
        val binding = ItemCuisineBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return CuisineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CuisineViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)
    }
}