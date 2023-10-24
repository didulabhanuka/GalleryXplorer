package com.example.galleryxplorer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_AllEvents : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAllEvents: ArrayList<Events>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_all_events)

        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.user_allEvents_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,1)
        userAllEvents = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("allEvents").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val allEvents:Events? = data.toObject(Events::class.java)
                    if(allEvents != null){
                        userAllEvents.add(allEvents)

                    }
                }
                recyclerView.adapter = AllEventsAdapter(userAllEvents, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }

    }
}