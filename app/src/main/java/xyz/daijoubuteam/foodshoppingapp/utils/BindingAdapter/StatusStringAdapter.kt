package xyz.daijoubuteam.foodshoppingapp.utils.BindingAdapter

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.databinding.BindingAdapter
import xyz.daijoubuteam.foodshoppingapp.R

@BindingAdapter("statusString")
fun setStatusColor(view: TextView, status: String?){
    view.setTextColor(when(status){
        "Pending" -> ColorStateList.valueOf(view.context.getColor(R.color.bluegray_500))
        "Preparing" -> ColorStateList.valueOf(view.context.getColor(R.color.orange_400))
        "Shipping" -> ColorStateList.valueOf(view.context.getColor(R.color.blue_A200))
        else -> ColorStateList.valueOf(view.context.getColor(R.color.green_500))
    })
}