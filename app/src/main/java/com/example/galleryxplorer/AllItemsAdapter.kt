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

class AllItemsAdapter (
    private val userAllItems: ArrayList<AllItems>,
    private val context: Context,

) : RecyclerView.Adapter<AllItemsAdapter.UserViewHolder>(){

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
        holder.sellerName.text = userAllItems[position].sellerName
        holder.sellerId.text = userAllItems[position].sellerId
        holder.itemName.text = userAllItems[position].itemName
        holder.itemCategory.text = userAllItems[position].itemCategory
        holder.itemMedium.text = userAllItems[position].itemMedium
        holder.itemSubject.text = userAllItems[position].itemSubject
        holder.itemYear.text = userAllItems[position].itemYear
        holder.itemSize.text = userAllItems[position].itemSize
        holder.itemPrice.text = userAllItems[position].itemPrice

        val imageUrls = userAllItems[position].urls
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
            intent.putExtra("sellerName", userAllItems[position].sellerName)
            intent.putExtra("sellerId", userAllItems[position].sellerId)
            intent.putExtra("itemName", userAllItems[position].itemName)
            intent.putExtra("itemCategory", userAllItems[position].itemCategory)
            intent.putExtra("itemMedium", userAllItems[position].itemMedium)
            intent.putExtra("itemSubject", userAllItems[position].itemSubject)
            intent.putExtra("itemYear", userAllItems[position].itemYear)
            intent.putExtra("itemSize", userAllItems[position].itemSize)
            intent.putExtra("itemPrice", userAllItems[position].itemPrice)

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userAllItems.size

    }
}