package xyz.daijoubuteam.foodshoppingapp.utils.BindingAdapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import xyz.daijoubuteam.foodshoppingapp.R

@BindingAdapter("statusString")
fun setStatusColor(view: TextView, status: String?){
    view.setTextColor(when(status){
        "Pending" -> R.color.bluegray_500
        "Preparing" -> com.google.android.libraries.places.R.color.quantum_orange400
        "Shipping" -> com.google.android.libraries.places.R.color.quantum_googblue400
        else -> R.color.green_500
    })
}