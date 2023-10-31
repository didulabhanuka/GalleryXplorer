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

class YourClassesAdapter(
    private val sellerClassList: ArrayList<YourClasses>,
    private val context: Context,

    ) : RecyclerView.Adapter<YourClassesAdapter.SellerClassesViewHolder>(){

    class SellerClassesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.classes_singleClass_sellerName)
        val itemName: TextView = itemView.findViewById(R.id.classes_singleClass_name)
        val itemCategory: TextView = itemView.findViewById(R.id.classes_singleClass_category)
        val itemMedium: TextView = itemView.findViewById(R.id.classes_singleClass_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.classes_singleClass_subject)
        val itemYear: TextView = itemView.findViewById(R.id.classes_singleClass_year)
        val itemSize: TextView = itemView.findViewById(R.id.classes_singleClass_size)
        val itemPrice: TextView = itemView.findViewById(R.id.classes_singleClass_price)
        val sellerId: TextView = itemView.findViewById(R.id.classes_singleClass_size)
        val randomId: TextView = itemView.findViewById(R.id.classes_singleClass_randomId)
        val itemMainImage: ImageView = itemView.findViewById(R.id.classes_singleClass_mainImage)
        val itemSecondImage: ImageView = itemView.findViewById(R.id.classes_singleClass_secondImage)
        val itemThirdImage: ImageView = itemView.findViewById(R.id.classes_singleClass_thirdImage)

        val viewClass: TextView = itemView.findViewById(R.id.classes_singleClass_textView)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellerClassesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.classes_single_class,parent,false)
        return SellerClassesViewHolder(itemView)

    }
    override fun onBindViewHolder(holder: SellerClassesViewHolder, position: Int) {

        holder.sellerName.text = sellerClassList[position].sellerName
        holder.itemName.text = sellerClassList[position].itemName
        holder.itemCategory.text = sellerClassList[position].itemCategory
        holder.itemMedium.text = sellerClassList[position].itemMedium
        holder.itemSubject.text = sellerClassList[position].itemSubject
        holder.itemYear.text = sellerClassList[position].itemYear
        holder.itemSize.text = sellerClassList[position].itemSize
        holder.itemPrice.text = sellerClassList[position].itemPrice
        holder.sellerId.text = sellerClassList[position].sellerId
        holder.randomId.text = sellerClassList[position].randomId

        val imageUrls = sellerClassList[position].urls
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
            val intent = Intent(context, Seller_SingleClassView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", sellerClassList[position].sellerName)
            intent.putExtra("itemName", sellerClassList[position].itemName)
            intent.putExtra("itemCategory", sellerClassList[position].itemCategory)
            intent.putExtra("itemMedium", sellerClassList[position].itemMedium)
            intent.putExtra("itemSubject", sellerClassList[position].itemSubject)
            intent.putExtra("itemYear", sellerClassList[position].itemYear)
            intent.putExtra("itemSize", sellerClassList[position].itemSize)
            intent.putExtra("itemPrice", sellerClassList[position].itemPrice)
            intent.putExtra("sellerId", sellerClassList[position].sellerId)
            intent.putExtra("randomId", sellerClassList[position].randomId)

            context.startActivity(intent)

        }
    }
    override fun getItemCount(): Int {
        return sellerClassList.size

    }
}