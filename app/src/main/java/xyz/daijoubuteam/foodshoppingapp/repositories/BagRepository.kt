package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.*
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrder
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrderItem
import java.util.ArrayList

class BagRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getOrderListInBag(): Result<LiveData<List<BagOrder>>> {
        val orderList: MutableLiveData<List<BagOrder>> = MutableLiveData()
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("bag")
            docRef.addSnapshotListener { value, error ->
                orderList.value = value?.toObjects(BagOrder::class.java)
                orderList.value?.forEach { order ->
                    if (order.orderItems.isNullOrEmpty()){
                        order.id?.let { docRef.document(it).delete() }
                    }else {
                        order.eateryId?.addSnapshotListener { eateryValue, error ->
                            val eatery = eateryValue?.toObject(Eatery::class.java)
                            if (eatery == null) {
                                order.id?.let { docRef.document(it).delete() }
                            }else {
                                val newOrder = order.copy(
                                    eateryImage = eatery.photoUrl,
                                    eateryName = eatery.name
                                )
                                orderList.value = orderList.value?.map {
                                    if (it.id !== newOrder.id) it
                                    else newOrder
                                }
                            }
                        }
                    }
                }
            }
            Result.success(orderList)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getOrderItemList(orderId: String): Result<LiveData<List<BagOrderItem>>> {
        val orderItemList: MutableLiveData<List<BagOrderItem>> = MutableLiveData()
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("bag").document(orderId)
            docRef.addSnapshotListener { value, error ->
                val order = value?.toObject(BagOrder::class.java)
                orderItemList.value = order?.orderItems
                val newOrder = order?.copy()
                order?.orderItems?.forEach { orderItem ->
                    orderItem.productId?.addSnapshotListener { productValue, error ->
                        val product = productValue?.toObject(Product::class.java)
                        if (product == null || orderItem.quantity == 0) {
                            newOrder?.orderItems?.remove(orderItem)
                            if (newOrder != null) {
                                docRef.set(newOrder)
                            }
                        } else {
                            val newOrderItem = orderItem.copy(
                                productName = product.name,
                                productImg = product.img,
                                productPrice = product.price,
                                price = orderItem.quantity?.let { product.price?.times(it) }
                            )
                            orderItemList.value = orderItemList.value?.map {
                                if (it.productId !== newOrderItem.productId) it
                                else newOrderItem
                            }

                        }
                    }
                }
            }
            Result.success(orderItemList)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }


    suspend fun addNewOderItem(eateryId: String, productId: String, quantity: Int):Result<Boolean>{
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val eateryIdReference = db.document("eateries/$eateryId")
            val docRef = db.collection("users").document(uid).collection("bag").whereEqualTo("eateryId", eateryIdReference)
            val documentSnapShot = docRef.get().await()
            if (documentSnapShot.documents.isEmpty()) {
                val order = BagOrder(eateryIdReference)
                val documentRef = db.document("/eateries/$eateryId/products/$productId")
                val orderItem = BagOrderItem(documentRef,quantity)
                order.orderItems.add(orderItem)
                db.collection("users").document(uid).collection("bag").add(order)
            } else {
                val order = documentSnapShot.documents[0].toObject(BagOrder::class.java)
                val orderId = documentSnapShot.documents[0].id
                val documentRef = db.document("/eateries/$eateryId/products/$productId")
                val orderItem = order?.orderItems?.find{ orderItem -> orderItem.productId?.equals(documentRef)
                    ?: false }
                if( orderItem != null){
                    orderItem.quantity = orderItem.quantity?.plus(quantity)
                    db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                }else {
                    val newOrderItem = BagOrderItem(documentRef,quantity)
                    order?.orderItems?.add(newOrderItem)
                    if (order != null) {
                        db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                    }
                }
            }
            Result.success(true)
        }catch (exception: Exception){
            Result.failure(exception)
        }
    }

    fun getOrderAddress():Result<LiveData<ShippingAddress?>>{
        val shippingAddress: MutableLiveData<ShippingAddress?> = MutableLiveData()
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid)
            docRef.addSnapshotListener{value, error ->
                val userInfo = value?.toObject(User::class.java)
                if(userInfo?.shippingAddresses.isNullOrEmpty()){
                    shippingAddress.value = null
                }else{
                    shippingAddress.value = userInfo?.shippingAddresses?.find {shippingAddress -> shippingAddress.defaultAddress  }
                    Timber.i(shippingAddress.value.toString())
                }
            }
            Result.success(shippingAddress)
        }catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun placeOrder(orderItems: List<BagOrderItem>, orderId: String, shippingAddress: ShippingAddress):Result<Boolean>{
        return try {
            var totalPrice = 0.0
            for (orderItem in orderItems){
                totalPrice += orderItem.price!!
            }
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("bag").document(orderId)
            docRef.addSnapshotListener { value, error ->
                val bagOrder = value?.toObject(BagOrder::class.java)
                bagOrder?.eateryId?.addSnapshotListener{eateryValue, error->
                    val eatery = eateryValue?.toObject(Eatery::class.java)

                    val newOrder = Order(
                        eateryId = bagOrder.eateryId,
                        eateryName = eatery?.name,
                        eateryImage = eatery?.photoUrl,
                        orderItems = ArrayList(orderItems.map { orderItem -> orderItem.toOrderItem() }),
                        status = "Pending",
                        orderTime = Timestamp(System.currentTimeMillis()/1000, 0),
                        totalPrice = totalPrice,
                        shippingAddress = shippingAddress
                    )
                    val orderRef = db.collection("users").document(uid).collection("orders").document(orderId)
                    orderRef.set(newOrder)
                    db.collection("eateries").document("${eatery?.id}").collection("orders").add(hashMapOf("orderId" to orderRef))
                    docRef.delete()
                }

            }
            Result.success(true)
        }catch (exception:Exception){
            Result.failure(exception)
        }
    }

}