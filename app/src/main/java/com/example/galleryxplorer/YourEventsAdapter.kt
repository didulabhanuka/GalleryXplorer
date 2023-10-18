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

class YourEventsAdapter (
    private val sellerEventList: ArrayList<Events>,
    private val context: Context,

        ) : RecyclerView.Adapter<YourEventsAdapter.SellerEventsViewHolder>() {

            class SellerEventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

                val sellerId: TextView = itemView.findViewById(R.id.events_singleEvent_sellerId)
                val randomId: TextView = itemView.findViewById(R.id.events_singleEvent_randomId)
                val sellerName: TextView = itemView.findViewById(R.id.events_singleEvent_sellerName)
                val eventCategory: TextView = itemView.findViewById(R.id.events_singleEvent_category)
                val eventName: TextView = itemView.findViewById(R.id.events_singleEvent_name)
                val eventVenue: TextView = itemView.findViewById(R.id.events_singleEvent_venue)
                val eventTime: TextView = itemView.findViewById(R.id.events_singleEvent_time)
                val eventDate: TextView = itemView.findViewById(R.id.events_singleEvent_date)
                val eventOrganizers: TextView = itemView.findViewById(R.id.events_singleEvent_organizers)
                val eventDescription: TextView = itemView.findViewById(R.id.events_singleEvent_description)
                val eventBanner: ImageView = itemView.findViewById(R.id.events_singleEvent_eventBanner)

                val viewEvent: TextView = itemView.findViewById(R.id.events_singleEvent_textView)

            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerEventsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.events_single_event,parent,false)
        return SellerEventsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: SellerEventsViewHolder, position: Int) {
        holder.sellerId.text = sellerEventList[position].sellerId
        holder.randomId.text = sellerEventList[position].randomId
        holder.sellerName.text = sellerEventList[position].sellerName
        holder.eventCategory.text = sellerEventList[position].eventCategory
        holder.eventName.text = sellerEventList[position].eventName
        holder.eventVenue.text = sellerEventList[position].eventVenue
        holder.eventTime.text = sellerEventList[position].eventTime
        holder.eventDate.text = sellerEventList[position].eventDate
        holder.eventOrganizers.text = sellerEventList[position].eventOrganizers
        holder.eventDescription.text = sellerEventList[position].eventDescription

        Glide.with(context).load(sellerEventList[position].urls).into(holder.eventBanner)
    }

    override fun getItemCount(): Int {
        return sellerEventList.size

    }
}