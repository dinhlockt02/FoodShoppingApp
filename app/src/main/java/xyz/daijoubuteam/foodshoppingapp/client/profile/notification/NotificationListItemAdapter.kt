package xyz.daijoubuteam.foodshoppingapp.client.profile.notification

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import xyz.daijoubuteam.foodshoppingapp.R
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemNotificationListBinding
import xyz.daijoubuteam.foodshoppingapp.model.Notification
import xyz.daijoubuteam.foodshoppingapp.model.Product


class NotificationListItemAdapter(private val onClickListener: OnClickListener): ListAdapter<Notification, NotificationListItemAdapter.NotificationViewHolder>(diffcallback) {
    class NotificationViewHolder(private val binding: ItemNotificationListBinding, private val onClickListener: OnClickListener) : RecyclerView.ViewHolder(binding.root){
        private var isExpanded: Boolean = false
        fun bind(notification: Notification){
            binding.notification = notification

            binding.markAsReadButton.setOnClickListener {
                onClickListener.onClick(notification.copy(notificationRead = true))
            }

            binding.notificationItem.setOnClickListener {
                if(!isExpanded) {
                    val animation = ObjectAnimator.ofInt(
                        binding.notificationBody,
                        "maxLines",
                        25
                    )
                    animation.duration = 300
                    animation.start()
                    isExpanded = !isExpanded
                    binding.expandedIcon.setImageResource(R.drawable.ic_outline_arrow_drop_up_24)
                }
                else {
                    val animation = ObjectAnimator.ofInt(
                        binding.notificationBody,
                        "maxLines",
                        2
                    )
                    animation.duration = 200
                    animation.start()
                    isExpanded = !isExpanded
                    binding.expandedIcon.setImageResource(R.drawable.ic_outline_arrow_drop_down_24)
                }
            }
            binding.executePendingBindings()
        }
    }

    object diffcallback: DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.notificationRead == newItem.notificationRead
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notificationItem = getItem(position)
        holder.bind(notificationItem)
    }

    class OnClickListener(val clickListener: (notification: Notification) -> Unit) {
        fun onClick(notification: Notification) = clickListener(notification)
    }
}