package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_Dashboard : AppCompatActivity() {

    private lateinit var btnYourItems: AppCompatButton
    private lateinit var btnYourProfile: AppCompatButton
    private lateinit var sellerName: TextView

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)

        sellerName = findViewById(R.id.dashboard_seller_name)
        btnYourItems = findViewById(R.id.btn_your_items)
        btnYourProfile = findViewById(R.id.btn_your_profile)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        if (uId != null){
            database.collection("sellers").document(uId).get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val sellerData = it.data
                        val sellerNameValue = sellerData?.get("sellerName") as String
                        sellerName.text = sellerNameValue
                    }
                }
        }

        btnYourItems.setOnClickListener {
            val intent = Intent(this, Seller_YourItems::class.java)
            startActivity(intent)
        }

        btnYourProfile.setOnClickListener {
            val intent = Intent(this, Seller_SellerProfile::class.java)
            startActivity(intent)
        }

    }
}