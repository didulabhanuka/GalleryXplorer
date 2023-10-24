package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Seller_YourEvents : AppCompatActivity() {

    private lateinit var btnAddEvent: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var sellerEventList: ArrayList<Events>

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_your_events)

        btnAddEvent = findViewById(R.id.btn_your_events_addNewEvent)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        recyclerView = findViewById(R.id.your_events_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,1)
        sellerEventList = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("eventsBySellerID").document("$uId").collection("sellerEvents").get()
            .addOnSuccessListener {
                for(data in it.documents){
                    val yourEvents:Events? = data.toObject(Events::class.java)
                    if (yourEvents != null){
                        sellerEventList.add(yourEvents)

                    }
                }
                recyclerView.adapter = YourEventsAdapter(sellerEventList,this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }

        btnAddEvent.setOnClickListener {
            val intent = Intent(this, AddEvent::class.java)
            startActivity(intent)
        }
    }
}