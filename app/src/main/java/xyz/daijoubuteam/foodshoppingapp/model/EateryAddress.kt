package xyz.daijoubuteam.foodshoppingapp.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable
import java.util.*

data class EateryAddress(
    //var id: String? = UUID.randomUUID().toString().replace("-", "").uppercase(),
    var address: String? = "",
    var geoPointLocation: GeoPoint? = null
): Serializable {
    @get:Exclude
    @set:Exclude
    var location: LatLng?
        get() = geoPointLocation?.let {
            LatLng(it.latitude, it.longitude)
        }
        set(value) = setGeoPointLocation(value)

    @Exclude
    private fun setGeoPointLocation(location: LatLng?){
        location?.let {
            geoPointLocation = GeoPoint(location.latitude, location.longitude)
        }
    }
}