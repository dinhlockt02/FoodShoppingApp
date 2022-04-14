package xyz.daijoubuteam.foodshoppingapp.client.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.model.Category
import java.util.ArrayList

class AdapterCategory : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lstData: List<Category> = ArrayList()
    public fun setData(lstData: List<Category>)
    {
        this.lstData = lstData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdapterCategory.AdapterCategoryHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AdapterCategory.AdapterCategoryHolder)
        {
            holder.bindingData(lstData.get(position))
        }
    }

    override fun getItemCount(): Int {
        return lstData.size
    }

    public class AdapterCategoryHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView)
    {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)

        fun bindingData(category: Category) {
            tvName.text = category.name
            imgIcon.setImageResource(category.icon)
        }
    }
}