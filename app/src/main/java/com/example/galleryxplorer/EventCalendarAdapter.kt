package com.example.galleryxplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventCalendarAdapter(
    private val eventList: ArrayList<Events>

    ) : RecyclerView.Adapter<EventCalendarAdapter.EventViewHolder>() {
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val eventName: TextView = itemView.findViewById(R.id.calendar_event_name)
        val sellerName: TextView = itemView.findViewById(R.id.calendar_event_sellerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.calender_event_view, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.eventName.text = eventList[position].eventName
        holder.sellerName.text = eventList[position].sellerName
    }

    override fun getItemCount() = eventList.size
}
