package com.example.galleryxplorer

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.checkerframework.checker.units.qual.A

class CategoryItems : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryItems: ArrayList<AllItems>
    private lateinit var categoryName: String
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_items)

        auth = FirebaseAuth.getInstance()

        // Retrieve the category name from the intent
        categoryName = intent.getStringExtra("category_name") ?: ""

        recyclerView = findViewById(R.id.categoryItems_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        categoryItems = arrayListOf()
        database = FirebaseFirestore.getInstance()

        // Fetch category-specific items from Firestore
        fetchCategoryItems(categoryName)
    }

    private fun fetchCategoryItems(categoryName: String) {

        database.collection("categories").document(categoryName).collection("categoryItems")
            .get()
            .addOnSuccessListener {
                for (data in it.documents) {
                    val allItems:AllItems? = data.toObject(AllItems::class.java)
                    if(allItems != null){
                        categoryItems.add(allItems)
                    }
                }

                // Initialize and set up your RecyclerView adapter with categoryItems
                recyclerView.adapter = AllItemsAdapter(categoryItems, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}