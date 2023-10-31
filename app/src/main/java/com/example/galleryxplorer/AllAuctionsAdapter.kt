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
import java.security.PrivateKey

class AllAuctionsAdapter (
    private val userAllAuctions: ArrayList<AllAuctions>,
    private val context: Context,

    ) : RecyclerView.Adapter<AllAuctionsAdapter.UserAuctionsViewHolder>(){

    class UserAuctionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_sellerName)
        val sellerId: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_sellerId)
        val itemName: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_name)
        val itemCategory: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_category)
        val itemMedium: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_subject)
        val itemYear: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_year)
        val itemSize: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_size)
        val itemPrice: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_price)
        val itemMainImage: ImageView = itemView.findViewById(R.id.allAuctions_singleAuction_mainImage)
        val itemSecondImage: ImageView = itemView.findViewById(R.id.allAuctions_singleAuction_secondImage)
        val itemThirdImage: ImageView = itemView.findViewById(R.id.allAuctions_singleAuction_thirdImage)

        val viewClass: TextView = itemView.findViewById(R.id.allAuctions_singleAuction_textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAuctionsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_auctions_single_auction,parent,false)
        return UserAuctionsViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserAuctionsViewHolder, position: Int) {
        holder.sellerName.text = userAllAuctions[position].sellerName
        holder.sellerId.text = userAllAuctions[position].sellerId
        holder.itemName.text = userAllAuctions[position].itemName
        holder.itemCategory.text = userAllAuctions[position].itemCategory
        holder.itemMedium.text = userAllAuctions[position].itemMedium
        holder.itemSubject.text = userAllAuctions[position].itemSubject
        holder.itemYear.text = userAllAuctions[position].itemYear
        holder.itemSize.text = userAllAuctions[position].itemSize
        holder.itemPrice.text = userAllAuctions[position].itemPrice

        val imageUrls = userAllAuctions[position].urls
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(context).load(imageUrls[0]).into(holder.itemMainImage)
            if (imageUrls.size >= 2) {
                Glide.with(context).load(imageUrls[1]).into(holder.itemSecondImage)
            }
            if (imageUrls.size >= 3) {
                Glide.with(context).load(imageUrls[2]).into(holder.itemThirdImage)
            }
        }

        holder.viewClass.setOnClickListener() {
            val intent = Intent(context, Buyer_SingleAuctionView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", userAllAuctions[position].sellerName)
            intent.putExtra("sellerId", userAllAuctions[position].sellerId)
            intent.putExtra("itemName", userAllAuctions[position].itemName)
            intent.putExtra("itemCategory", userAllAuctions[position].itemCategory)
            intent.putExtra("itemMedium", userAllAuctions[position].itemMedium)
            intent.putExtra("itemSubject", userAllAuctions[position].itemSubject)
            intent.putExtra("itemYear", userAllAuctions[position].itemYear)
            intent.putExtra("itemSize", userAllAuctions[position].itemSize)
            intent.putExtra("itemPrice", userAllAuctions[position].itemPrice)

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userAllAuctions.size

    }
}