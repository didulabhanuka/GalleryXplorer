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

class YourEnrollsAdapter(
    private val sellerEnrollsList: ArrayList<YourEnrolls>,
    private val context: Context,

    ) : RecyclerView.Adapter<YourEnrollsAdapter.SellerEnrollsViewHolder>(){

    class SellerEnrollsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.classes_singleClass_sellerName)
        val itemName: TextView = itemView.findViewById(R.id.classes_singleClass_name)
        val itemMedium: TextView = itemView.findViewById(R.id.classes_singleClass_medium)
        val itemYear: TextView = itemView.findViewById(R.id.classes_singleClass_year)
        val itemSize: TextView = itemView.findViewById(R.id.classes_singleClass_size)
        val sellerId: TextView = itemView.findViewById(R.id.classes_singleClass_size)
        val randomId: TextView = itemView.findViewById(R.id.classes_singleClass_randomId)
        val viewClass: TextView = itemView.findViewById(R.id.classes_singleClass_textView)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerEnrollsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.enrolls_single_enroll,parent,false)
        return SellerEnrollsViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: SellerEnrollsViewHolder, position: Int) {

        holder.sellerName.text = sellerEnrollsList[position].sellerName
        holder.itemName.text = sellerEnrollsList[position].itemName
        holder.itemMedium.text = sellerEnrollsList[position].itemMedium
        holder.itemYear.text = sellerEnrollsList[position].itemYear
        holder.itemSize.text = sellerEnrollsList[position].itemSize
        holder.sellerId.text = sellerEnrollsList[position].sellerId
        holder.randomId.text = sellerEnrollsList[position].randomId


        holder.viewClass.setOnClickListener() {
            val intent = Intent(context, Seller_SingleClassView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)


            intent.putExtra("sellerName", sellerEnrollsList[position].sellerName)
            intent.putExtra("itemName", sellerEnrollsList[position].itemName)
            intent.putExtra("itemMedium", sellerEnrollsList[position].itemMedium)
            intent.putExtra("itemYear", sellerEnrollsList[position].itemYear)
            intent.putExtra("itemSize", sellerEnrollsList[position].itemSize)
            intent.putExtra("sellerId", sellerEnrollsList[position].sellerId)
            intent.putExtra("randomId", sellerEnrollsList[position].randomId)

            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return sellerEnrollsList.size

    }
}
