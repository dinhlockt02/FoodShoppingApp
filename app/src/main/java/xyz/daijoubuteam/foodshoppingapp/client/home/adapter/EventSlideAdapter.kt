package xyz.daijoubuteam.foodshoppingapp.client.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.daijoubuteam.foodshoppingapp.databinding.ItemSlideBinding
import xyz.daijoubuteam.foodshoppingapp.model.Eatery

import xyz.daijoubuteam.foodshoppingapp.model.Event

class EventSlideAdapter(private val eventList: List<Event>, private val onClickListener: OnClickListener)
    :RecyclerView.Adapter<EventSlideAdapter.EventListViewHolder>(){
        class EventListViewHolder(private val binding: ItemSlideBinding)
            :RecyclerView.ViewHolder(binding.root) {
                fun bind(event: Event) {
                   binding.event = event
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventListViewHolder {
        val binding = ItemSlideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventListViewHolder, position: Int) {
        val eventItem = eventList[position]
        holder.itemView.setOnClickListener{
            onClickListener.onClick(eventItem)
        }
        holder.bind(eventItem)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class OnClickListener(val clickListener: (event: Event) -> Unit) {
        fun onClick(event: Event) = clickListener(event)
    }
}