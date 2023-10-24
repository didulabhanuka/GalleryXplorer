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

class ProfileAllItemsAdapter(
    private val profileItemList: ArrayList<YourItems>,
    private val context: Context,

) : RecyclerView.Adapter<ProfileAllItemsAdapter.ProfileViewHolder>(){

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val itemName: TextView = itemView.findViewById(R.id.profile_singleItem_name)
        val itemPrice: TextView = itemView.findViewById(R.id.profile_singleItem_price)
        val itemMainImage: ImageView = itemView.findViewById(R.id.profile_singleItem_mainImage)
        val viewItem: TextView = itemView.findViewById(R.id.profile_singleItem_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_single_item,parent,false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        holder.itemName.text = profileItemList[position].itemName
        holder.itemPrice.text = profileItemList[position].itemPrice

        val imageUrls = profileItemList[position].urls
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(context).load(imageUrls[0]).into(holder.itemMainImage)
        }

        holder.viewItem.setOnClickListener() {

            val intent = Intent(context, Seller_SingleItemView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", profileItemList[position].sellerName)
            intent.putExtra("itemName", profileItemList[position].itemName)
            intent.putExtra("itemCategory", profileItemList[position].itemCategory)
            intent.putExtra("itemMedium", profileItemList[position].itemMedium)
            intent.putExtra("itemSubject", profileItemList[position].itemSubject)
            intent.putExtra("itemYear", profileItemList[position].itemYear)
            intent.putExtra("itemSize", profileItemList[position].itemSize)
            intent.putExtra("itemPrice", profileItemList[position].itemPrice)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return profileItemList.size
    }
}