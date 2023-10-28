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

class AllClassesAdapter (
    private val userAllClasses: ArrayList<AllClasses>,
    private val context: Context,

    ) : RecyclerView.Adapter<AllClassesAdapter.UserClassesViewHolder>(){

    class UserClassesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sellerName: TextView = itemView.findViewById(R.id.allClasses_singleClass_sellerName)
        val sellerId: TextView = itemView.findViewById(R.id.allClasses_singleClass_sellerId)
        val itemName: TextView = itemView.findViewById(R.id.allClasses_singleClass_name)
        val itemCategory: TextView = itemView.findViewById(R.id.allClasses_singleClass_category)
        val itemMedium: TextView = itemView.findViewById(R.id.allClasses_singleClass_medium)
        val itemSubject: TextView = itemView.findViewById(R.id.allClasses_singleClass_subject)
        val itemYear: TextView = itemView.findViewById(R.id.allClasses_singleClass_year)
        val itemSize: TextView = itemView.findViewById(R.id.allClasses_singleClass_size)
        val itemPrice: TextView = itemView.findViewById(R.id.allClasses_singleClass_price)
        val itemMainImage: ImageView = itemView.findViewById(R.id.allClasses_singleClass_mainImage)
        val itemSecondImage: ImageView = itemView.findViewById(R.id.allClasses_singleClass_secondImage)
        val itemThirdImage: ImageView = itemView.findViewById(R.id.allClasses_singleClass_thirdImage)

        val viewClass: TextView = itemView.findViewById(R.id.allClasses_singleClass_textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserClassesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.all_classes_single_class,parent,false)
        return UserClassesViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: UserClassesViewHolder, position: Int) {
        holder.sellerName.text = userAllClasses[position].sellerName
        holder.sellerId.text = userAllClasses[position].sellerId
        holder.itemName.text = userAllClasses[position].itemName
        holder.itemCategory.text = userAllClasses[position].itemCategory
        holder.itemMedium.text = userAllClasses[position].itemMedium
        holder.itemSubject.text = userAllClasses[position].itemSubject
        holder.itemYear.text = userAllClasses[position].itemYear
        holder.itemSize.text = userAllClasses[position].itemSize
        holder.itemPrice.text = userAllClasses[position].itemPrice

        val imageUrls = userAllClasses[position].urls
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
            val intent = Intent(context, Buyer_SingleClassView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", userAllClasses[position].sellerName)
            intent.putExtra("sellerId", userAllClasses[position].sellerId)
            intent.putExtra("itemName", userAllClasses[position].itemName)
            intent.putExtra("itemCategory", userAllClasses[position].itemCategory)
            intent.putExtra("itemMedium", userAllClasses[position].itemMedium)
            intent.putExtra("itemSubject", userAllClasses[position].itemSubject)
            intent.putExtra("itemYear", userAllClasses[position].itemYear)
            intent.putExtra("itemSize", userAllClasses[position].itemSize)
            intent.putExtra("itemPrice", userAllClasses[position].itemPrice)

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userAllClasses.size

    }
}