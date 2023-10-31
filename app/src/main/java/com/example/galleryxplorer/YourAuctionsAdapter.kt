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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class YourAuctionsAdapter(
    private val sellerAuctionList: ArrayList<YourAuctions>,
    private val context: Context,

    ) : RecyclerView.Adapter<YourAuctionsAdapter.SellerAuctionsViewHolder>(){

    class SellerAuctionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.auctions_singleAuction_sellerName)
        val itemName: TextView = itemView.findViewById(R.id.auctions_singleAuction_name)
        val itemCategory: TextView = itemView.findViewById(R.id.auctions_singleAuction_category)
        val itemMedium: TextView = itemView.findViewById(R.id.auctions_singleAuction_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.auctions_singleAuction_subject)
        val itemYear: TextView = itemView.findViewById(R.id.auctions_singleAuction_year)
        val itemSize: TextView = itemView.findViewById(R.id.auctions_singleAuction_size)
        val itemPrice: TextView = itemView.findViewById(R.id.auctions_singleAuction_price)
        val sellerId: TextView = itemView.findViewById(R.id.auctions_singleAuction_size)
        val randomId: TextView = itemView.findViewById(R.id.auctions_singleAuction_randomId)
        val itemMainImage: ImageView = itemView.findViewById(R.id.auctions_singleAuction_mainImage)
        val itemSecondImage: ImageView = itemView.findViewById(R.id.auctions_singleAuction_secondImage)
        val itemThirdImage: ImageView = itemView.findViewById(R.id.auctions_singleAuction_thirdImage)

        val viewAuction: TextView = itemView.findViewById(R.id.auctions_singleAuction_textView)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerAuctionsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.auctions_single_auction,parent,false)
        return SellerAuctionsViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: SellerAuctionsViewHolder, position: Int) {

        holder.sellerName.text = sellerAuctionList[position].sellerName
        holder.itemName.text = sellerAuctionList[position].itemName
        holder.itemCategory.text = sellerAuctionList[position].itemCategory
        holder.itemMedium.text = sellerAuctionList[position].itemMedium
        holder.itemSubject.text = sellerAuctionList[position].itemSubject
        holder.itemYear.text = sellerAuctionList[position].itemYear
        holder.itemSize.text = sellerAuctionList[position].itemSize
        holder.itemPrice.text = sellerAuctionList[position].itemPrice
        holder.sellerId.text = sellerAuctionList[position].sellerId
        holder.randomId.text = sellerAuctionList[position].randomId

        val imageUrls = sellerAuctionList[position].urls
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(context).load(imageUrls[0]).into(holder.itemMainImage)
            if (imageUrls.size >= 2) {
                Glide.with(context).load(imageUrls[1]).into(holder.itemSecondImage)
            }
            if (imageUrls.size >= 3) {
                Glide.with(context).load(imageUrls[2]).into(holder.itemThirdImage)
            }
        }


        holder.viewAuction.setOnClickListener() {
            val intent = Intent(context, Seller_SingleAuctionView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", sellerAuctionList[position].sellerName)
            intent.putExtra("itemName", sellerAuctionList[position].itemName)
            intent.putExtra("itemCategory", sellerAuctionList[position].itemCategory)
            intent.putExtra("itemMedium", sellerAuctionList[position].itemMedium)
            intent.putExtra("itemSubject", sellerAuctionList[position].itemSubject)
            intent.putExtra("itemYear", sellerAuctionList[position].itemYear)
            intent.putExtra("itemSize", sellerAuctionList[position].itemSize)
            intent.putExtra("itemPrice", sellerAuctionList[position].itemPrice)
            intent.putExtra("sellerId", sellerAuctionList[position].sellerId)
            intent.putExtra("randomId", sellerAuctionList[position].randomId)

            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return sellerAuctionList.size

    }
}