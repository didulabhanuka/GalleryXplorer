package com.example.galleryxplorer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_Dashboard : AppCompatActivity() {

    private lateinit var becomeSeller : AppCompatButton
    private lateinit var allItems : AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth : FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)

        becomeSeller = findViewById(R.id.btn_become_seller)
        allItems = findViewById(R.id.btn_all_items)

        auth = FirebaseAuth.getInstance()

    }
}

