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
    private lateinit var btnYourEvents: AppCompatButton
    private lateinit var btnCalendar: AppCompatButton
    private lateinit var btnSignOut: AppCompatButton
    private lateinit var sellerName: TextView

    private lateinit var btnYourAuctionItems: AppCompatButton
    private lateinit var btnYourClassItems: AppCompatButton
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
        btnYourEvents = findViewById(R.id.btn_your_events)
        btnCalendar = findViewById(R.id.btn_calender)
        btnSignOut = findViewById(R.id.btn_signOut)
        btnYourAuctionItems = findViewById(R.id.btn_your_auction_items)
        btnYourClassItems = findViewById(R.id.btn_your_class_items)


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

        btnYourEvents.setOnClickListener {
            val intent = Intent(this, Seller_YourEvents::class.java)
            startActivity(intent)
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(this, User_Calendar::class.java)
            startActivity(intent)
        }

        btnSignOut.setOnClickListener {
            // Sign out the current user
            auth.signOut()

            // Redirect the user to the login or sign-in activity
            val intent = Intent(this, LoginPage::class.java)
            // Make sure the user cannot navigate back to the dashboard using the back button
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnYourAuctionItems.setOnClickListener {
            val intent = Intent(this, Seller_YourAuctions::class.java)
            startActivity(intent)
        }

        btnYourClassItems.setOnClickListener {
            val intent = Intent(this, Seller_YourClasses::class.java)
            startActivity(intent)
        }

    }
}