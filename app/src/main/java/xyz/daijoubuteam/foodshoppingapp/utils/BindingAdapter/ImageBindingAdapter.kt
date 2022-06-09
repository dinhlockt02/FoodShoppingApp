package xyz.daijoubuteam.foodshoppingapp.utils.BindingAdapter

import android.annotation.SuppressLint
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.model.Gender
import java.io.InputStream
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

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("imageUrl", "gender")
fun setImageUrlAvatarUri(view: ImageView, url: Uri?, gender: Gender?) {

    val defaultAvatarDrawable = view.context.applicationContext.getDrawable(when (gender) {
        Gender.MALE -> R.drawable.default_avatar_male
        Gender.FEMALE -> R.drawable.default_avatar_female
        else -> if (Random.nextBoolean()) R.drawable.default_avatar_male else R.drawable.default_avatar_female
    })

    if (url == null) {
        Timber.i(url.toString())
        view.setImageDrawable(defaultAvatarDrawable)
        view.setBackgroundResource(R.color.orange_50 )
        return
    } else {
        val circularProgressDrawable = CircularProgressDrawable(view.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide
            .with(view.context)
            .load(url)
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .error(defaultAvatarDrawable)
            .into(view);
    }


}

@BindingAdapter("imageUrl", "gender")
fun setImageUrlAvatarString(view: ImageView, url: String?, gender: Gender?) {
    val uri: Uri? = if(url.isNullOrBlank()) null else url.toUri()
    setImageUrlAvatarUri(view, uri, gender)
}

//@BindingAdapter("imageUrl")
//fun setImageUrlDrawable(view: ImageView, url: String?) {
//    val circularProgressDrawable = CircularProgressDrawable(view.context)
//    circularProgressDrawable.strokeWidth = 5f
//    circularProgressDrawable.centerRadius = 30f
//    circularProgressDrawable.start()
//    Glide
//        .with(view.context)
//        .load(url)
//        .centerCrop()
//        .placeholder(circularProgressDrawable)
//        .into(view);
//}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, url: String?) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val imageLoader = ImageLoader.Builder(view.context)
        .componentRegistry { add(SvgDecoder(view.context)) }
        .build()

    val request = ImageRequest.Builder(view.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.information_outline)
        .data(url)
        .target(view)
        .build()

    imageLoader.enqueue(request)
}

@BindingAdapter("imgVector")
fun setImageSVGDrawable(view: ImageView, url: String?) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val imageLoader = ImageLoader.Builder(view.context)
        .componentRegistry { add(SvgDecoder(view.context)) }
        .build()

    val request = ImageRequest.Builder(view.context)
        .crossfade(true)
        .crossfade(500)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.information_outline)
        .data(url)
        .target(view)
        .build()

    imageLoader.enqueue(request)
}

@BindingAdapter("statusString")
fun TextView.setStatusColor(status: String){
    setTextColor(when(status){
        "Pending" -> R.color.bluegray_500
        "Preparing" -> com.google.android.libraries.places.R.color.quantum_orange400
        "Shipping" -> com.google.android.libraries.places.R.color.quantum_googblue400
        else -> R.color.green_500
    })
}