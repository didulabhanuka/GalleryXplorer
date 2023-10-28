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

class Seller_YourClasses : AppCompatActivity() {
    private lateinit var btnAddClass: AppCompatButton
    private lateinit var btnEnrollClass: AppCompatButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var sellerClassList: ArrayList<YourClasses>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_your_classes)

        btnAddClass = findViewById(R.id.btn_your_classes_addNewClass)
        btnEnrollClass = findViewById(R.id.btn_your_classes_viewenroll)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        recyclerView = findViewById(R.id.your_classes_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        sellerClassList = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("sellerClassesBySellerID").document("$uId").collection("sellerClasses").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val yourClasses:YourClasses? = data.toObject(YourClasses::class.java)
                    if(yourClasses != null){
                        sellerClassList.add(yourClasses)
                        Log.d("Firestore", "Added item: ${yourClasses.itemName}")

                    }
                }
                recyclerView.adapter = YourClassesAdapter(sellerClassList, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }

        btnAddClass.setOnClickListener {
            val intent = Intent(this, AddClass::class.java)
            startActivity(intent)
        }
        btnEnrollClass.setOnClickListener {
            val intent = Intent(this, Seller_YourEnrolls::class.java)
            startActivity(intent)
        }
    }
}
