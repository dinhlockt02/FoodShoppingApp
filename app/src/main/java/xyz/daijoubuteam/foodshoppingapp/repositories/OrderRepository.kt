package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import xyz.daijoubuteam.foodshoppingapp.model.Eatery
import xyz.daijoubuteam.foodshoppingapp.model.Order
import xyz.daijoubuteam.foodshoppingapp.model.bagmodel.BagOrder

class OrderRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun getOrderUpComing():Result<LiveData<List<Order>>>{
        val orderList: MutableLiveData<List<Order>> = MutableLiveData()
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("orders")
            docRef.addSnapshotListener { value, error ->
                orderList.value = value?.toObjects(Order::class.java)?.filter {order -> order.status == "Pending"}
                orderList.value = (orderList.value as MutableList<Order>).sortedByDescending { it.orderTime }
            }
            Result.success(orderList)
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getOrderOnGoing():Result<LiveData<List<Order>>>{
        val orderList: MutableLiveData<List<Order>> = MutableLiveData()
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("orders")
            docRef.addSnapshotListener { value, error ->
                orderList.value = value?.toObjects(Order::class.java)?.filter {order -> order.status == "Preparing" || order.status == "Shipping"}
                orderList.value = (orderList.value as MutableList<Order>).sortedByDescending { it.orderTime }
            }
            Result.success(orderList)
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getOrderCompleted():Result<LiveData<List<Order>>>{
        val orderList: MutableLiveData<List<Order>> = MutableLiveData()
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("orders")
            docRef.addSnapshotListener { value, error ->
                orderList.value = value?.toObjects(Order::class.java)?.filter {order -> order.status == "Completed" || order.status == "Cancelled"}
                orderList.value = (orderList.value as MutableList<Order>).sortedByDescending { it.orderTime }
            }
            Result.success(orderList)
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    fun getOrderById(orderId: String):Result<LiveData<Order>>{
        val order: MutableLiveData<Order> = MutableLiveData()
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("orders").document(orderId)
            docRef.addSnapshotListener { value, error ->
                order.value = value?.toObject(Order::class.java)
            }
            Result.success(order)
        }catch (exception: Exception) {
        Result.failure(exception)
        }
    }

    suspend fun getEateryByOrderId(orderId: String):Result<LiveData<Eatery>>{
        val eatery: MutableLiveData<Eatery> = MutableLiveData()
        return try{
            val uid = auth.currentUser?.uid ?: throw Exception("Current user not found.")
            val docRef = db.collection("users").document(uid).collection("orders").document(orderId)
            val documentSnapShot = docRef.get().await()
            val order = documentSnapShot.toObject<Order>()
            order?.eateryId?.addSnapshotListener { value, error ->
                eatery.value = value?.toObject(Eatery::class.java)
            }
            Result.success(eatery)
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}