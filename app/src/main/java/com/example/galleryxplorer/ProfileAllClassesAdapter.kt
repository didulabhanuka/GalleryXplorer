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

class ProfileAllClassesAdapter(
    private val profileClassList: ArrayList<YourClasses>,
    private val context: Context
) : RecyclerView.Adapter<ProfileAllClassesAdapter.ProfileViewHolder>() {

    class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName: TextView = itemView.findViewById(R.id.profile_singleClass_name)
        val itemPrice: TextView = itemView.findViewById(R.id.profile_singleClass_price)
        val itemMainImage: ImageView = itemView.findViewById(R.id.profile_singleClass_mainImage)
        val viewItem: TextView = itemView.findViewById(R.id.profile_singleClass_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.profile_single_class, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        holder.itemName.text = profileClassList[position].itemName
        holder.itemPrice.text = profileClassList[position].itemPrice

        val imageUrls = profileClassList[position].urls
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(context).load(imageUrls[0]).into(holder.itemMainImage)
        }

        holder.viewItem.setOnClickListener() {

            val intent = Intent(context, Seller_SingleClassView::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", profileClassList[position].sellerName)
            intent.putExtra("itemName", profileClassList[position].itemName)
            intent.putExtra("itemCategory", profileClassList[position].itemCategory)
            intent.putExtra("itemMedium", profileClassList[position].itemMedium)
            intent.putExtra("itemSubject", profileClassList[position].itemSubject)
            intent.putExtra("itemYear", profileClassList[position].itemYear)
            intent.putExtra("itemSize", profileClassList[position].itemSize)
            intent.putExtra("itemPrice", profileClassList[position].itemPrice)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return profileClassList.size
    }
}
