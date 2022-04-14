package xyz.daijoubuteam.foodshoppingapp.client.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import java.util.*

class AdapterEatery: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lstData: List<Eatery> = ArrayList()
    public fun setData(lstData: List<Eatery>)
    {
        this.lstData = lstData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdapterEateryHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_eatery, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterEateryHolder)
        {
            holder.bindingData(lstData.get(position))
        }
    }

    override fun getItemCount(): Int {
        return lstData.size
    }

    public class AdapterEateryHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)
    {
        private val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
        private val tvEateryName: TextView = itemView.findViewById(R.id.tvEateryName)
        private val tvEateryAddress: TextView = itemView.findViewById(R.id.tvEateryAddress)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        private val tvRatingCount: TextView = itemView.findViewById(R.id.tvRatingCount)

        fun bindingData(eatery: Eatery) {
            imgAvatar.setImageResource(eatery.imgRes)
            tvEateryName.text = eatery.name
            tvEateryAddress.text = eatery.address
            tvTime.text = "${eatery.time.toString()} mins"
            tvDistance.text = "${eatery.distance.toString()} km"
            tvRatingCount.text = eatery.ratingCount.toString()
        }
    }
}