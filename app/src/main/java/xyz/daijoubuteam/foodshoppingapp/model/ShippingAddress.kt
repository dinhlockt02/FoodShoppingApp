package xyz.daijoubuteam.foodshoppingapp.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.encoders.annotations.Encodable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.GeoPoint

data class ShippingAddress(
    var name: String = "",
    var gender: Gender = Gender.MALE,
    var phoneNumber: String = "",
    var address: String = "",
    var geoPointLocation: GeoPoint? = null
) {
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