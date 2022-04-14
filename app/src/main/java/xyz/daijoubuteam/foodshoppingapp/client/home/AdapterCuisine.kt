package xyz.daijoubuteam.foodshoppingapp.client.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.model.Cuisine
import java.util.ArrayList

class AdapterCuisine : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lstData: List<Cuisine> = ArrayList()
    public fun setData(lstData: List<Cuisine>)
    {
        this.lstData = lstData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdapterCuisine.AdapterCuisineHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cuisine, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterCuisine.AdapterCuisineHolder)
        {
            holder.bindingData(lstData.get(position))
        }
    }

    override fun getItemCount(): Int {
        return lstData.size
    }

    public class AdapterCuisineHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)
    {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)

        fun bindingData(cuisine: Cuisine) {
            tvName.text = cuisine.name
            imgAvatar.setImageResource(cuisine.imgAvatar)
        }
    }
}