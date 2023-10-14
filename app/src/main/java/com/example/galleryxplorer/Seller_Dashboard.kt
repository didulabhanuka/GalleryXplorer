package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_Dashboard : AppCompatActivity() {

    private lateinit var btnYourItems: AppCompatButton
    private lateinit var btnYourProfile: AppCompatButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_dashboard)

        btnYourItems = findViewById(R.id.btn_your_items)
        btnYourProfile = findViewById(R.id.btn_your_profile)

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