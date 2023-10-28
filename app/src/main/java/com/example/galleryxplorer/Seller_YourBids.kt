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

class Seller_YourBids : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sellerBidList: ArrayList<YourBids>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_your_bids)


        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        recyclerView = findViewById(R.id.your_bids_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        sellerBidList = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("allBids").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val yourBids:YourBids? = data.toObject(YourBids::class.java)
                    if(yourBids != null){
                        sellerBidList.add(yourBids)
                        Log.d("Firestore", "Added item: ${yourBids.itemName}")

                    }
                }
                recyclerView.adapter = YourBidsAdapter(sellerBidList, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }
    }
}
