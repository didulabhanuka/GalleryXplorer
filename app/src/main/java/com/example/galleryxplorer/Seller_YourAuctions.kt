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

class Seller_YourAuctions : AppCompatActivity() {
    private lateinit var btnAddAuction: AppCompatButton
    private lateinit var btnBidAuction: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var sellerAuctionList: ArrayList<YourAuctions>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_your_auctions)

        btnAddAuction = findViewById(R.id.btn_your_auctions_addNewAuction)
        btnBidAuction = findViewById(R.id.btn_your_auctions_addNewAuction)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        recyclerView = findViewById(R.id.your_auctions_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        sellerAuctionList = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("sellerAuctionsBySellerID").document("$uId").collection("sellerAuctions").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val yourAuctions:YourAuctions? = data.toObject(YourAuctions::class.java)
                    if(yourAuctions != null){
                        sellerAuctionList.add(yourAuctions)
                        Log.d("Firestore", "Added item: ${yourAuctions.itemName}")

                    }
                }
                recyclerView.adapter = YourAuctionsAdapter(sellerAuctionList, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }

        btnAddAuction.setOnClickListener {
            val intent = Intent(this, AddAuction::class.java)
            startActivity(intent)
        }
        btnBidAuction.setOnClickListener {
            val intent = Intent(this, Seller_YourBids::class.java)
            startActivity(intent)
        }
    }
}
