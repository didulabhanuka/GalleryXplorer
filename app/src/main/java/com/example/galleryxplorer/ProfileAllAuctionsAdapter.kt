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

class ProfileAllAuctionsAdapter(
    private val profileAuctionList: ArrayList<YourAuctions>,
    private val context: Context
) : RecyclerView.Adapter<ProfileAllAuctionsAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName: TextView = itemView.findViewById(R.id.profile_singleAuction_name)
        val itemPrice: TextView = itemView.findViewById(R.id.profile_singleAuction_price)
        val itemMainImage: ImageView = itemView.findViewById(R.id.profile_singleAuction_mainImage)
        val viewItem: TextView = itemView.findViewById(R.id.profile_singleAuction_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_single_auction, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        holder.itemName.text = profileAuctionList[position].itemName
        holder.itemPrice.text = profileAuctionList[position].itemPrice

        val imageUrls = profileAuctionList[position].urls
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(context).load(imageUrls[0]).into(holder.itemMainImage)
        }

        holder.viewItem.setOnClickListener() {

            val intent = Intent(context, Seller_SingleClassView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", profileAuctionList[position].sellerName)
            intent.putExtra("itemName", profileAuctionList[position].itemName)
            intent.putExtra("itemCategory", profileAuctionList[position].itemCategory)
            intent.putExtra("itemMedium", profileAuctionList[position].itemMedium)
            intent.putExtra("itemSubject", profileAuctionList[position].itemSubject)
            intent.putExtra("itemYear", profileAuctionList[position].itemYear)
            intent.putExtra("itemSize", profileAuctionList[position].itemSize)
            intent.putExtra("itemPrice", profileAuctionList[position].itemPrice)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return profileAuctionList.size
    }
}
