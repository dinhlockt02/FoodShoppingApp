package xyz.daijoubuteam.foodshoppingapp.client.home.adapter

import android.location.Location
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.MainApplication
import xyz.daijoubuteam.foodshoppingapp.databinding.FragmentVerticalListEateryBinding
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemCardEateryBinding
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemEateryBinding
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.utils.observeOnce

class VerticalCardEateryAdapter(private val onClickListener: VerticalCardEateryAdapter.OnClickListener): ListAdapter<Eatery, VerticalCardEateryAdapter.VerticalCardEateryViewHolder>(DiffCallBack) {
    class VerticalCardEateryViewHolder(private var binding: ItemCardEateryBinding): RecyclerView.ViewHolder(binding.root) {
        private var currentLocation: Location? = null
        private val mainApplication = binding.root.context.applicationContext as MainApplication

        fun bind(eatery: Eatery) {
            binding.eatery = eatery
            mainApplication.location.observeOnce { lct ->
                if(lct != null) {
                    currentLocation = lct
                    val location: Location = Location(LocationManager.GPS_PROVIDER)
                    eatery.addressEatery?.location?.let {
                        location.latitude = it.latitude
                        location.longitude = it.longitude
                        binding.distance = String.format("%.2f", currentLocation?.distanceTo(location)?.div(1000)) + " km"
                    }
                }
            }
            binding.executePendingBindings()
        }
    }
    companion object DiffCallBack: DiffUtil.ItemCallback<Eatery>() {
        override fun areItemsTheSame(oldItem: Eatery, newItem: Eatery): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Eatery, newItem: Eatery): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VerticalCardEateryViewHolder {
        val binding =  ItemCardEateryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VerticalCardEateryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VerticalCardEateryViewHolder, position: Int) {
        val eateryItem = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(eateryItem)
        }
        holder.bind(eateryItem)
    }

    class OnClickListener(val clickListener: (eatery:Eatery) -> Unit) {
        fun onClick(eatery:Eatery) = clickListener(eatery)
    }
}