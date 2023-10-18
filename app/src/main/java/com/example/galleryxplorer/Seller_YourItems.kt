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

class Seller_YourItems : AppCompatActivity() {
    private lateinit var btnAddItem: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var sellerItemList: ArrayList<YourItems>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_your_items)

        btnAddItem = findViewById(R.id.btn_your_items_addNewItem)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        recyclerView = findViewById(R.id.your_items_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        sellerItemList = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("sellerItemsBySellerID").document("$uId").collection("sellerItems").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val yourItems:YourItems? = data.toObject(YourItems::class.java)
                    if(yourItems != null){
                        sellerItemList.add(yourItems)
                    }
                }
                recyclerView.adapter = YourItemsAdapter(sellerItemList, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }

        btnAddItem.setOnClickListener {
            val intent = Intent(this, AddItem::class.java)
            startActivity(intent)
        }
    }
}
