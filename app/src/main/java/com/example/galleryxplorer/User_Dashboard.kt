package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_Dashboard : AppCompatActivity() {

    private lateinit var becomeSeller : AppCompatButton
    private lateinit var allItems : AppCompatButton
    private lateinit var categories : AppCompatButton
    private lateinit var btnCalendar: AppCompatButton
    private lateinit var allEvents : AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth : FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        becomeSeller = findViewById(R.id.btn_become_seller)
        allItems = findViewById(R.id.btn_user_all_items)
        categories = findViewById(R.id.btn_user_categories)
        allEvents = findViewById(R.id.btn_user_events)
        btnCalendar = findViewById(R.id.btn_user_calender)

        auth = FirebaseAuth.getInstance()

        becomeSeller.setOnClickListener {
            val intent = Intent(this, SellerRegistration::class.java)
            startActivity(intent)
        }

        allItems.setOnClickListener {
            val intent = Intent(this, User_AllItems::class.java)
            startActivity(intent)
        }
        categories.setOnClickListener {
            val intent = Intent(this, CategoriesPage::class.java)
            startActivity(intent)
        }

        allEvents.setOnClickListener {
            val intent = Intent(this, User_AllEvents::class.java)
            startActivity(intent)
        }

        btnCalendar.setOnClickListener {
            val intent = Intent(this, User_Calendar::class.java)
            startActivity(intent)
        }

    }
}

