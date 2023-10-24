package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SpecificSellerItems : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var specificSellerItems: ArrayList<AllItems>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_seller_items)


        recyclerView = findViewById(R.id.your_items_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        specificSellerItems = arrayListOf()
        database = FirebaseFirestore.getInstance()

        val sellerId = intent.getStringExtra("sellerId").toString()

        database.collection("sellerItemsBySellerID").document("$sellerId").collection("sellerItems").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val yourItems:AllItems? = data.toObject(AllItems::class.java)
                    if(yourItems != null){
                        specificSellerItems.add(yourItems)
                    }
                }
                recyclerView.adapter = User_SpecificSellerItemsAdapater(specificSellerItems, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }
    }
}
