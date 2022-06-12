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
        "Cancelled " -> ColorStateList.valueOf(view.context.getColor(R.color.deep_red))
        else -> ColorStateList.valueOf(view.context.getColor(R.color.green_500))
    })
}

@BindingAdapter("saveOrderChange")
fun setOrderText(view: TextView, quantity: Int?){
    if(quantity == 0){
        view.setBackgroundColor(view.context.getColor(R.color.deep_red))
        view.text = "Delete"
    }else{
        view.setBackgroundColor(view.context.getColor(R.color.deep_orange_A200))
        view.text = "Save"
    }
}

@BindingAdapter("dishesQuantity")
fun setDishesText(view: TextView, quantity: Int?){
    if(quantity == 1){
        view.text = "${quantity} dish"
    }else if (quantity != null) {
        if (quantity > 1){
            view.text = "${quantity} dishes"
        }
    }
}