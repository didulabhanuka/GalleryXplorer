package com.example.galleryxplorer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AllEventsAdapter (
    private val userAllEvents: ArrayList<Events>,
    private val context: Context,

    ) : RecyclerView.Adapter<AllEventsAdapter.UserEventsViewHolder>(){

    class UserEventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerId: TextView = itemView.findViewById(R.id.allEvents_singleEvent_sellerId)
        val randomId: TextView = itemView.findViewById(R.id.allEvents_singleEvent_randomId)
        val sellerName: TextView = itemView.findViewById(R.id.allEvents_singleEvent_sellerName)
        val eventCategory: TextView = itemView.findViewById(R.id.allEvents_singleEvent_category)
        val eventName: TextView = itemView.findViewById(R.id.allEvents_singleEvent_name)
        val eventVenue: TextView = itemView.findViewById(R.id.allEvents_singleEvent_venue)
        val eventTime: TextView = itemView.findViewById(R.id.allEvents_singleEvent_time)
        val eventDate: TextView = itemView.findViewById(R.id.allEvents_singleEvent_date)
        val eventOrganizers: TextView = itemView.findViewById(R.id.allEvents_singleEvent_organizers)
        val eventDescription: TextView = itemView.findViewById(R.id.allEvents_singleEvent_description)
        val eventBanner: ImageView = itemView.findViewById(R.id.allEvents_singleEvent_eventBanner)

        val viewEvent: TextView = itemView.findViewById(R.id.allEvents_singleEvent_textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEventsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_events_single_event,parent,false)
        return UserEventsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserEventsViewHolder, position: Int) {
        holder.sellerId.text = userAllEvents[position].sellerId
        holder.randomId.text = userAllEvents[position].randomId
        holder.sellerName.text = userAllEvents[position].sellerName
        holder.eventCategory.text = userAllEvents[position].eventCategory
        holder.eventName.text = userAllEvents[position].eventName
        holder.eventVenue.text = userAllEvents[position].eventVenue
        holder.eventTime.text = userAllEvents[position].eventTime
        holder.eventDate.text = userAllEvents[position].eventDate
        holder.eventOrganizers.text = userAllEvents[position].eventOrganizers
        holder.eventDescription.text = userAllEvents[position].eventDescription

        Glide.with(context).load(userAllEvents[position].urls).into(holder.eventBanner)

        holder.viewEvent.setOnClickListener(){
            val intent = Intent(context, User_SingleEventView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putExtra("sellerId", userAllEvents[position].sellerId)
            intent.putExtra("randomId", userAllEvents[position].randomId)
            intent.putExtra("sellerName", userAllEvents[position].sellerName)
            intent.putExtra("eventCategory", userAllEvents[position].eventCategory)
            intent.putExtra("eventName", userAllEvents[position].eventName)
            intent.putExtra("eventVenue", userAllEvents[position].eventVenue)
            intent.putExtra("eventTime", userAllEvents[position].eventTime)
            intent.putExtra("eventDate", userAllEvents[position].eventDate)
            intent.putExtra("eventOrganizers", userAllEvents[position].eventOrganizers)
            intent.putExtra("eventDescription", userAllEvents[position].eventDescription)
            intent.putExtra("urls", userAllEvents[position].urls)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userAllEvents.size

    }

}