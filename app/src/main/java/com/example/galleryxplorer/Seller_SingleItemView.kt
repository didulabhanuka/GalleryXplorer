package com.example.galleryxplorer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_SingleItemView : AppCompatActivity() {

    private lateinit var sellerName: TextView
    private lateinit var itemName: TextView
    private lateinit var itemCategory: TextView
    private lateinit var itemMedium: TextView
    private lateinit var itemSubject: TextView
    private lateinit var itemYear: TextView
    private lateinit var itemSize: TextView
    private lateinit var itemPrice: TextView
    private lateinit var itemMainImage: ImageView
    private lateinit var itemSecondImage: ImageView
    private lateinit var itemThirdImage: ImageView

    private val database = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_single_item_view)

        sellerName = findViewById(R.id.item_view_seller_name)
        itemName = findViewById(R.id.item_view_name)
        itemCategory = findViewById(R.id.item_view_category)
        itemMedium = findViewById(R.id.item_view_medium)
        itemSubject = findViewById(R.id.item_view_image_subject)
        itemYear = findViewById(R.id.item_view_image_year)
        itemSize = findViewById(R.id.item_view_image_size)
        itemPrice = findViewById(R.id.item_view_price)
        itemMainImage = findViewById(R.id.item_view_main_image)
        itemSecondImage = findViewById(R.id.item_view_second_image)
        itemThirdImage = findViewById(R.id.item_view_third_image)

        sellerName.text = intent.getStringExtra("sellerName").toString()
        itemName.text = intent.getStringExtra("itemName").toString()
        itemCategory.text = intent.getStringExtra("itemCategory").toString()
        itemMedium.text = intent.getStringExtra("itemMedium").toString()
        itemSubject.text = intent.getStringExtra("itemSubject").toString()
        itemYear.text = intent.getStringExtra("itemYear").toString()
        itemSize.text = intent.getStringExtra("itemSize").toString()
        itemPrice.text = intent.getStringExtra("itemPrice").toString()

        val imageUrls = intent.getStringArrayListExtra("urls")
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(this).load(imageUrls[0]).into(itemMainImage)

            if (imageUrls.size >= 2) {
                Glide.with(this).load(imageUrls[1]).into(itemSecondImage)
            }

            if (imageUrls.size >= 3) {
                Glide.with(this).load(imageUrls[2]).into(itemThirdImage)
            }
        } else {
            // Handle the case when image URLs are not available
            // You can hide or show placeholders, or display a message as needed.
        }

    }
}