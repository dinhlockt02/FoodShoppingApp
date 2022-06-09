package xyz.daijoubuteam.foodshoppingapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import xyz.daijoubuteam.foodshoppingapp.model.Order

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
                orderList.value = value?.toObjects(Order::class.java)?.filter {order -> order.status == "Completed"}
            }
            Result.success(orderList)
        }catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}