package xyz.daijoubuteam.foodshoppingapp.repositories

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.model.*

class UserRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun getCurrentUser(): Result<User?> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            getOrderItemListByEateryId()
            AddNewOderItem()
            val docRef = db.collection("users").document(uid)
            val documentSnapShot = docRef.get().await()
            val users = documentSnapShot.toObject<User>()
            Result.success(users)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getCurrentUserLiveData(): Result<LiveData<User>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid)
            val user = MutableLiveData<User>()
            docRef.addSnapshotListener { value, error ->
                user.value = value?.toObject()
            }
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCurrentUserInfo(user: User): Result<Boolean> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            db.collection("users").document(uid).set(user).await()
            Result.success(true)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun uploadAvatar(uri: Uri): Result<Uri> {
        return try {
            val storageRef = storage.reference
            val avatarRef = storageRef.child("images/${uri.lastPathSegment}")
            avatarRef.putFile(uri).await()
            val downloadUrl = avatarRef.downloadUrl.await()
            Timber.i(downloadUrl.toString())
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserNotificationLiveData(): Result<LiveData<List<Notification>>> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            val notificationRef = db.collection("users").document(uid).collection("notifications")
            val notifications = MutableLiveData<List<Notification>>()
            notificationRef.addSnapshotListener { value, error ->
                val notificationsList = value?.toObjects(Notification::class.java)
                notifications.value = notificationsList ?: listOf()
                Timber.i(notifications.value?.size.toString())
            }
            Result.success(notifications)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotification(notification: Notification): Result<Boolean> {
        return try {
            val uid = auth.currentUser?.uid
                ?: throw Exception("Current user not found.")
            if (notification.id == null) throw Exception("Notification not found.")
            db.collection("users").document(uid).collection("notifications").document(
                notification.id
            ).set(notification).await()
            Result.success(true);
        }catch (exception: Exception){
            Result.failure(exception)
        }
    }
//    fun getListOrderItemByEateryId(): Result<LiveData<List<OrderItem>>>{
//        return try {
//            val uid = auth.currentUser?.uid
//                ?: throw Exception("Current user not found.")
//            val docRef = db.collection("users").document(uid)
//            val orderItemList: MutableLiveData<List<OrderItem>> = MutableLiveData()
//            val user = MutableLiveData<User>()
//            docRef.addSnapshotListener { value, error ->
//                user.value = value?.toObject()
//            }
//            orderItemList.value =  user.value?.bag
//            orderItemList.value?.find { orderItem -> orderItem.productId?.contains("c8vy6QVL2ZTLC0uOrdV7") ?: false }
//            Timber.i(orderItemList.toString())
//            Result.success(orderItemList)
//        } catch (exception: Exception){
//            Result.failure(exception)
//        }
//    }
    fun getOrderItemListByEateryId():Result<LiveData<List<Order>>>{
    val orderItemList: MutableLiveData<List<Order>> = MutableLiveData()
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("bag")
            docRef.addSnapshotListener{value,error ->
                orderItemList.value  = value?.toObjects(Order::class.java)
                if (value != null) {
                    for (document in value.documents){
                        val abc = document.toObject(Order::class.java)
                    }

                }
            }
            Result.success(orderItemList)
        } catch (exception: Exception){
            Result.failure(exception)
        }
    }

    suspend fun AddNewOderItem():Result<Boolean>{
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val documentRef1 = db.document("eateries/c8vy6QVL2ZTLC0uOrdV7")
            val docRef = db.collection("users").document(uid).collection("bag").whereEqualTo("eateryId", documentRef1)
            val documentSnapShot = docRef.get().await()
            if (documentSnapShot.documents.isEmpty()) {
                val order = Order(documentRef1)
                val documentRef = db.document("/eateries/c8vy6QVL2ZTLC0uOrdV7/products/IwzjrStZj9NR7EuknST9")
                documentRef.addSnapshotListener{value, error ->
                    val product = value?.toObject(Product::class.java)
                    val price = 4.0 * product?.newPrice!!
                    val orderItem = OrderItem(documentRef,4,price)
                    order.orderItems.add(orderItem)
                    order.totalPrice = price
                    db.collection("users").document(uid).collection("bag").add(order)
                }

            }else {
                val order = documentSnapShot.documents[0].toObject(Order::class.java)
                val orderId = documentSnapShot.documents[0].id
                val documentRef = db.document("/eateries/c8vy6QVL2ZTLC0uOrdV7/products/IwzjrStZj9NR7EuknST9")
                val orderItem = order?.orderItems?.find{ orderItem -> orderItem.productId?.equals(documentRef)
                    ?: false }
                if( orderItem != null){
                    documentRef.addSnapshotListener{value, error ->
                        orderItem.quantity = orderItem.quantity?.plus(2)
                        val product = value?.toObject(Product::class.java)
                        val bonusPrice = 2.toDouble() * product?.newPrice!!
                        orderItem.price = orderItem.price?.plus(bonusPrice)
                        order.totalPrice = order.totalPrice?.plus(bonusPrice)
                        db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                    }

                }else {
                    documentRef.addSnapshotListener{value, error ->
                        val product = value?.toObject(Product::class.java)
                        val price = 2.toDouble() * product?.newPrice!!
                        val newOrderItem = OrderItem(documentRef,2,price)
                        order?.orderItems?.add(newOrderItem)
                        if (order != null) {
                            order.totalPrice = order.totalPrice?.plus(newOrderItem.price!!)
                            db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                        }
                    }
                }
            }
            Result.success(true)
        }catch (exception: Exception){
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
                documentRef.addSnapshotListener{value, error ->
                    val product = value?.toObject(Product::class.java)
                    val price = quantity.toDouble() * product?.newPrice!!
                    val orderItem = OrderItem(documentRef,quantity,price)
                    order.orderItems.add(orderItem)
                    order.totalPrice = price
                    db.collection("users").document(uid).collection("bag").add(order)
                }
            } else {
                val order = documentSnapShot.documents[0].toObject(Order::class.java)
                val orderId = documentSnapShot.documents[0].id
                val documentRef = db.document("/eateries/$eateryId/products/$productId")
                val orderItem = order?.orderItems?.find{ orderItem -> orderItem.productId?.equals(documentRef)
                    ?: false }
                if( orderItem != null){
                    documentRef.addSnapshotListener{value, error ->
                        orderItem.quantity = orderItem.quantity?.plus(quantity)
                        val product = value?.toObject(Product::class.java)
                        val bonusPrice = quantity.toDouble() * product?.newPrice!!
                        orderItem.price = orderItem.price?.plus(bonusPrice)
                        order.totalPrice = order.totalPrice?.plus(bonusPrice)
                        db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                    }
                }else {
                    documentRef.addSnapshotListener{value, error ->
                        val product = value?.toObject(Product::class.java)
                        val price = quantity.toDouble() * product?.newPrice!!
                        val newOrderItem = OrderItem(documentRef,quantity,price)
                        order?.orderItems?.add(newOrderItem)
                        if (order != null) {
                            order.totalPrice = order.totalPrice?.plus(newOrderItem.price!!)
                            db.collection("users").document(uid).collection("bag").document(orderId).set(order)
                        }
                    }
                }
            }
            Result.success(true)
        }catch (exception: Exception){
            Result.failure(exception)
        }
    }
}