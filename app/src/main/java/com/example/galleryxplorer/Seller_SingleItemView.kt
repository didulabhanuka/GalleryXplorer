package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
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
    private lateinit var sellerId: TextView
    private lateinit var randomId: TextView
    private lateinit var itemMainImage: ImageView
    private lateinit var itemSecondImage: ImageView
    private lateinit var itemThirdImage: ImageView

    private lateinit var btnUpdate: AppCompatButton
    private lateinit var btnDelete: AppCompatButton

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
        sellerId = findViewById(R.id.item_view_sellerId)
        randomId = findViewById(R.id.item_view_randomId)
        itemMainImage = findViewById(R.id.item_view_main_image)
        itemSecondImage = findViewById(R.id.item_view_second_image)
        itemThirdImage = findViewById(R.id.item_view_third_image)

        btnUpdate = findViewById(R.id.btn_item_update)
        btnDelete = findViewById(R.id.btn_item_delete)

        sellerName.text = intent.getStringExtra("sellerName").toString()
        itemName.text = intent.getStringExtra("itemName").toString()
        itemCategory.text = intent.getStringExtra("itemCategory").toString()
        itemMedium.text = intent.getStringExtra("itemMedium").toString()
        itemSubject.text = intent.getStringExtra("itemSubject").toString()
        itemYear.text = intent.getStringExtra("itemYear").toString()
        itemSize.text = intent.getStringExtra("itemSize").toString()
        itemPrice.text = intent.getStringExtra("itemPrice").toString()
        sellerId.text = intent.getStringExtra("sellerId").toString()
        randomId.text = intent.getStringExtra("randomId").toString()

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

        btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateItem::class.java)

            intent.putStringArrayListExtra("urls", ArrayList(imageUrls ?: emptyList()))
            intent.putExtra("sellerName", sellerName.text.toString())
            intent.putExtra("itemName", itemName.text.toString())
            intent.putExtra("itemCategory", itemCategory.text.toString())
            intent.putExtra("itemMedium", itemMedium.text.toString())
            intent.putExtra("itemSubject", itemSubject.text.toString())
            intent.putExtra("itemYear", itemYear.text.toString())
            intent.putExtra("itemSize", itemSize.text.toString())
            intent.putExtra("itemPrice", itemPrice.text.toString())
            intent.putExtra("sellerId", sellerId.text.toString())
            intent.putExtra("randomId", randomId.text.toString())

            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    private fun showDeleteConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { dialog, _ ->
                // User clicked "Delete," delete the item here
                deleteItem()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // User clicked "Cancel," return to the item page
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun deleteItem() {
        val randomItemId = randomId.text.toString()

        // Use Firestore to delete the item with the specified randomId
        database.collection("sellerItemsBySellerID").document(intent.getStringExtra("sellerId").toString()).collection("sellerItems").document(intent.getStringExtra("randomId").toString()).delete()
        database.collection("categories").document(intent.getStringExtra("itemCategory").toString()).collection("categoryItems").document(intent.getStringExtra("randomId").toString()).delete()
        database.collection("allItems")
            .document(randomItemId)
            .delete()
            .addOnSuccessListener {
                finish()
            }
            .addOnFailureListener { e ->
                // Handle the error if the deletion fails
                Log.e("DeleteItem", "Error deleting item: $e")
            }
    }


}