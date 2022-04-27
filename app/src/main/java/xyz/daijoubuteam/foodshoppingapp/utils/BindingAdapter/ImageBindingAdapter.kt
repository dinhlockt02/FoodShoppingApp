package xyz.daijoubuteam.foodshoppingapp.utils.BindingAdapter

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.model.Gender
import kotlin.random.Random


@BindingAdapter("imageUrl", "defaultUrl")
fun setImageUrlDefaultUri(view: ImageView, url: Uri?, defaultUrl: String?) {
    var imageUrl = url
    if (url == null) {
        imageUrl = defaultUrl?.toUri()
    }
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    Glide
        .with(view.context)
        .load(imageUrl)
        .centerCrop()
        .placeholder(circularProgressDrawable)
        .into(view);
}

@BindingAdapter("imageUrl", "defaultUrl")
fun setImageUrlDefaultString(view: ImageView, url: String?, defaultUrl: String?) {
    setImageUrlDefaultUri(view, url?.toUri(), defaultUrl)
}

@BindingAdapter("imageUrl", "gender")
fun setImageUrlAvatarUri(view: ImageView, url: Uri?, gender: Gender?) {
    if (url == null) {
        view.setImageResource(
            when (gender) {
                Gender.MALE -> R.drawable.default_avatar_male
                Gender.FEMALE -> R.drawable.default_avatar_female
                else -> if (Random.nextBoolean()) R.drawable.default_avatar_male else R.drawable.default_avatar_female
            }
        )
        view.setBackgroundResource(R.color.orange_50 )
        return
    }
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    Glide
        .with(view.context)
        .load(url)
        .centerCrop()
        .placeholder(circularProgressDrawable)
        .into(view);
}

@BindingAdapter("imageUrl", "gender")
fun setImageUrlAvatarString(view: ImageView, url: String?, gender: Gender?) {
    setImageUrlAvatarUri(view, url?.toUri(), gender)
}
