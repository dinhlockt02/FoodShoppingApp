package xyz.daijoubuteam.foodshoppingapp.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Product
import xyz.daijoubuteam.foodshoppingapp.utils.VNCharacterUtils


class SearchRepository {

    private val db = Firebase.firestore

    suspend fun getEateryById(id: String): Eatery? {
        val eatery = db.collection("eateries").document(id).get().await().toObject(Eatery::class.java)
        val products = db.collection("eateries").document(id).collection("products").get().await().toObjects(Product::class.java)
        return eatery?.copy(products = ArrayList(products))
    }

    suspend fun searchEateries(searchText: String?): List<Eatery> {
        val removeAccentSearchText = VNCharacterUtils.removeAccent(searchText)
        if(searchText.isNullOrEmpty()) return listOf()
        val eateries = db.collection("eateries").get().await().toObjects(Eatery::class.java).map {
            eatery ->
            if (eatery.id != null) {
                val products = db.collection("eateries").document(eatery.id).collection("products").get().await().toObjects(Product::class.java).filter {
                    product ->
                    VNCharacterUtils.removeAccent(product.name).contains(removeAccentSearchText, ignoreCase = true)
                }.take(2)
                return@map eatery.copy(products = ArrayList(products))
            }
            else {
                return@map eatery;
            }
        }.filter {
                eatery ->
            if(eatery.products?.isNotEmpty() == true) return@filter true
            return@filter VNCharacterUtils.removeAccent(eatery.name).contains(removeAccentSearchText, ignoreCase = true)
        }.toList()
        eateries.forEach {
            Timber.i(it.name)
        }
        return eateries;
    }
}