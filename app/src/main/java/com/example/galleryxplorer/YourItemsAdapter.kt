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

class YourItemsAdapter(
    private val sellerItemList: ArrayList<YourItems>,
    private val context: Context,

) : RecyclerView.Adapter<YourItemsAdapter.SellerViewHolder>(){

    class SellerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.items_singleItem_sellerName)
        val itemName: TextView = itemView.findViewById(R.id.items_singleItem_name)
        val itemCategory: TextView = itemView.findViewById(R.id.items_singleItem_category)
        val itemMedium: TextView = itemView.findViewById(R.id.items_singleItem_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.items_singleItem_subject)
        val itemYear: TextView = itemView.findViewById(R.id.items_singleItem_year)
        val itemSize: TextView = itemView.findViewById(R.id.items_singleItem_size)
        val itemPrice: TextView = itemView.findViewById(R.id.items_singleItem_price)
        val sellerId: TextView = itemView.findViewById(R.id.items_singleItem_size)
        val randomId: TextView = itemView.findViewById(R.id.items_singleItem_randomId)
        val itemMainImage: ImageView = itemView.findViewById(R.id.items_singleItem_mainImage)
        val itemSecondImage: ImageView = itemView.findViewById(R.id.items_singleItem_secondImage)
        val itemThirdImage: ImageView = itemView.findViewById(R.id.items_singleItem_thirdImage)

        val viewItem: TextView = itemView.findViewById(R.id.items_singleItem_textView)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.items_single_item,parent,false)
        return SellerViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: SellerViewHolder, position: Int) {

        holder.sellerName.text = sellerItemList[position].sellerName
        holder.itemName.text = sellerItemList[position].itemName
        holder.itemCategory.text = sellerItemList[position].itemCategory
        holder.itemMedium.text = sellerItemList[position].itemMedium
        holder.itemSubject.text = sellerItemList[position].itemSubject
        holder.itemYear.text = sellerItemList[position].itemYear
        holder.itemSize.text = sellerItemList[position].itemSize
        holder.itemPrice.text = sellerItemList[position].itemPrice
        holder.sellerId.text = sellerItemList[position].sellerId
        holder.randomId.text = sellerItemList[position].randomId

        val imageUrls = sellerItemList[position].urls
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
            val intent = Intent(context, Seller_SingleItemView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", sellerItemList[position].sellerName)
            intent.putExtra("itemName", sellerItemList[position].itemName)
            intent.putExtra("itemCategory", sellerItemList[position].itemCategory)
            intent.putExtra("itemMedium", sellerItemList[position].itemMedium)
            intent.putExtra("itemSubject", sellerItemList[position].itemSubject)
            intent.putExtra("itemYear", sellerItemList[position].itemYear)
            intent.putExtra("itemSize", sellerItemList[position].itemSize)
            intent.putExtra("itemPrice", sellerItemList[position].itemPrice)
            intent.putExtra("sellerId", sellerItemList[position].sellerId)
            intent.putExtra("randomId", sellerItemList[position].randomId)

            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return sellerItemList.size

    }
}