package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.OrderItem
import xyz.daijoubuteam.foodshoppingapp.model.Product

class BagRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getOrderListInBag(): Result<LiveData<List<Order>>> {
        val orderList: MutableLiveData<List<Order>> = MutableLiveData()
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("bag")
            docRef.addSnapshotListener { value, error ->
                orderList.value = value?.toObjects(Order::class.java)
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

    fun getOrderItemList(orderId: String): Result<LiveData<List<OrderItem>>> {
        val orderItemList: MutableLiveData<List<OrderItem>> = MutableLiveData()
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("bag").document(orderId)
            docRef.addSnapshotListener { value, error ->
                val order = value?.toObject(Order::class.java)
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
                                price = orderItem.quantity?.let { product.newPrice?.times(it) }
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
                val order = Order(eateryIdReference)
                val documentRef = db.document("/eateries/$eateryId/products/$productId")
                val orderItem = OrderItem(documentRef,quantity)
                order.orderItems.add(orderItem)
                db.collection("users").document(uid).collection("bag").add(order)
            } else {
                val order = documentSnapShot.documents[0].toObject(Order::class.java)
                val orderId = documentSnapShot.documents[0].id
                val documentRef = db.document("/eateries/$eateryId/products/$productId")
                val orderItem = order?.orderItems?.find{ orderItem -> orderItem.productId?.equals(documentRef)
                    ?: false }
                if( orderItem != null){
                    orderItem.quantity = orderItem.quantity?.plus(quantity)
                    db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                }else {
                    val newOrderItem = OrderItem(documentRef,quantity)
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

}