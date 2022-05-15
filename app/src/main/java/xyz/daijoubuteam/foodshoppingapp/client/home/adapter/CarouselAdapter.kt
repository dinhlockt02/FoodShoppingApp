package xyz.daijoubuteam.foodshoppingapp.client.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemSlideBinding
import xyz.daijoubuteam.foodshoppingapp.model.Carousel
import xyz.daijoubuteam.foodshoppingapp.model.Category

class CarouselAdapter: ListAdapter<Carousel, CarouselAdapter.CarouselViewHolder>(DiffCallBack) {
    class CarouselViewHolder(private var binding: ItemSlideBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(carousel: Carousel) {
            binding.carousel = carousel
            //binding.imgIcon.setImageResource(category.icon)
            binding.executePendingBindings()
        }
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Carousel>() {
        override fun areItemsTheSame(oldItem: Carousel, newItem: Carousel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Carousel, newItem: Carousel): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val carouselItem = getItem(position)
//        holder.itemView.setOnClickListener{
//            onClickListener.onClick(eateryItem)
//        }
        holder.bind(carouselItem)
    }
}
