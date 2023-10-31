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
    private lateinit var btnSignOut: AppCompatButton
    private lateinit var allEvents : AppCompatButton
    private lateinit var allClasses : AppCompatButton
    private lateinit var allAuctions : AppCompatButton

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
        btnSignOut = findViewById(R.id.btn_signOut)
        allClasses = findViewById(R.id.btn_user_classes)
        allAuctions = findViewById(R.id.btn_user_auctions)

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

        allClasses.setOnClickListener {
            val intent = Intent(this, User_AllClasses::class.java)
            startActivity(intent)
        }
        allAuctions.setOnClickListener {
            val intent = Intent(this, User_AllAuctions::class.java)
            startActivity(intent)
        }

    }
}

