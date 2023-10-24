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

class User_SpecificSellerItemsAdapater (
    private val specificSellerItems: ArrayList<AllItems>,
    private val context: Context,

    ) : RecyclerView.Adapter<User_SpecificSellerItemsAdapater.UserViewHolder>(){

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.allItems_singleItem_sellerName)
        val sellerId: TextView = itemView.findViewById(R.id.allItems_singleItem_sellerId)
        val itemName: TextView = itemView.findViewById(R.id.allItems_singleItem_name)
        val itemCategory: TextView = itemView.findViewById(R.id.allItems_singleItem_category)
        val itemMedium: TextView = itemView.findViewById(R.id.allItems_singleItem_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.allItems_singleItem_subject)
        val itemYear: TextView = itemView.findViewById(R.id.allItems_singleItem_year)
        val itemSize: TextView = itemView.findViewById(R.id.allItems_singleItem_size)
        val itemPrice: TextView = itemView.findViewById(R.id.allItems_singleItem_price)
        val itemMainImage: ImageView = itemView.findViewById(R.id.allItems_singleItem_mainImage)
        val itemSecondImage: ImageView = itemView.findViewById(R.id.allItems_singleItem_secondImage)
        val itemThirdImage: ImageView = itemView.findViewById(R.id.allItems_singleItem_thirdImage)

        val viewItem: TextView = itemView.findViewById(R.id.allItems_singleItem_textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_items_single_item,parent,false)
        return UserViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.sellerName.text = specificSellerItems[position].sellerName
        holder.sellerId.text = specificSellerItems[position].sellerId
        holder.itemName.text = specificSellerItems[position].itemName
        holder.itemCategory.text = specificSellerItems[position].itemCategory
        holder.itemMedium.text = specificSellerItems[position].itemMedium
        holder.itemSubject.text = specificSellerItems[position].itemSubject
        holder.itemYear.text = specificSellerItems[position].itemYear
        holder.itemSize.text = specificSellerItems[position].itemSize
        holder.itemPrice.text = specificSellerItems[position].itemPrice

        val imageUrls = specificSellerItems[position].urls
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(context).load(imageUrls[0]).into(holder.itemMainImage)
            if (imageUrls.size >= 2) {
                Glide.with(context).load(imageUrls[1]).into(holder.itemSecondImage)
            }
            if (imageUrls.size >= 3) {
                Glide.with(context).load(imageUrls[2]).into(holder.itemThirdImage)
            }
        }

        holder.viewItem.setOnClickListener() {
            val intent = Intent(context, Buyer_SingleItemView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", specificSellerItems[position].sellerName)
            intent.putExtra("sellerId", specificSellerItems[position].sellerId)
            intent.putExtra("itemName", specificSellerItems[position].itemName)
            intent.putExtra("itemCategory", specificSellerItems[position].itemCategory)
            intent.putExtra("itemMedium", specificSellerItems[position].itemMedium)
            intent.putExtra("itemSubject", specificSellerItems[position].itemSubject)
            intent.putExtra("itemYear", specificSellerItems[position].itemYear)
            intent.putExtra("itemSize", specificSellerItems[position].itemSize)
            intent.putExtra("itemPrice", specificSellerItems[position].itemPrice)

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return specificSellerItems.size

    }
}