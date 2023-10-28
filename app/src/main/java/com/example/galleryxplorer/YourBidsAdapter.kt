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

class YourBidsAdapter(
    private val sellerBidsList: ArrayList<YourBids>,
    private val context: Context,

    ) : RecyclerView.Adapter<YourBidsAdapter.SellerBidsViewHolder>(){

    class SellerBidsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.classes_singleClass_sellerName)
        val itemName: TextView = itemView.findViewById(R.id.classes_singleClass_name)
        val itemMedium: TextView = itemView.findViewById(R.id.classes_singleClass_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.classes_singleClass_subject)
        val sellerId: TextView = itemView.findViewById(R.id.classes_singleClass_size)
        val randomId: TextView = itemView.findViewById(R.id.classes_singleClass_randomId)
        val viewClass: TextView = itemView.findViewById(R.id.classes_singleClass_textView)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerBidsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bids_single_bid,parent,false)
        return SellerBidsViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: SellerBidsViewHolder, position: Int) {

        holder.sellerName.text = sellerBidsList[position].sellerName
        holder.itemName.text = sellerBidsList[position].itemName
        holder.itemMedium.text = sellerBidsList[position].itemMedium
        holder.itemSubject.text = sellerBidsList[position].itemSubject
        holder.sellerId.text = sellerBidsList[position].sellerId
        holder.randomId.text = sellerBidsList[position].randomId


        holder.viewClass.setOnClickListener() {
            val intent = Intent(context, Seller_SingleClassView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


            intent.putExtra("sellerName", sellerBidsList[position].sellerName)
            intent.putExtra("itemName", sellerBidsList[position].itemName)
            intent.putExtra("itemMedium", sellerBidsList[position].itemMedium)
            intent.putExtra("itemSubject", sellerBidsList[position].itemSubject)
            intent.putExtra("sellerId", sellerBidsList[position].sellerId)
            intent.putExtra("randomId", sellerBidsList[position].randomId)

            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return sellerBidsList.size

    }
}