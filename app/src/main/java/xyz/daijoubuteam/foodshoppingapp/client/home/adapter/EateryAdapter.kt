package xyz.daijoubuteam.foodshoppingapp.client.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemEateryBinding
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

class EateryAdapter(private val onClickListener:OnClickListener) : ListAdapter<Eatery, EateryAdapter.EateryViewHolder>(DiffCallBack) {
    class EateryViewHolder(private var binding: ItemEateryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(eatery: Eatery) {
            binding.eatery = eatery
            // cach tam thoi
            //binding.imgAvatar.setImageResource(eatery.imgRes)
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<Eatery>(){
        override fun areItemsTheSame(oldItem: Eatery, newItem: Eatery): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Eatery, newItem: Eatery): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EateryViewHolder {
        val binding = ItemEateryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EateryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EateryViewHolder, position: Int) {
        val eateryItem = getItem(position)
        holder.itemView.setOnClickListener{
            onClickListener.onClick(eateryItem)
        }
        holder.bind(eateryItem)
    }

    class OnClickListener(val clickListener: (eatery:Eatery) -> Unit) {
        fun onClick(eatery:Eatery) = clickListener(eatery)
    }
}
