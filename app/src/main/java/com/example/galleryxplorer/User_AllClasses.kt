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

class User_AllClasses : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAllClasses: ArrayList<AllClasses>
    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_all_classes)

        auth = FirebaseAuth.getInstance()

        recyclerView = findViewById(R.id.user_allClasses_recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        userAllClasses = arrayListOf()
        database = FirebaseFirestore.getInstance()

        database.collection("allClasses").get()
            .addOnSuccessListener {
                for (data in it.documents){
                    val allClasses:AllClasses? = data.toObject(AllClasses::class.java)
                    if(allClasses != null){
                        userAllClasses.add(allClasses)

                    }
                }
                recyclerView.adapter = AllClassesAdapter(userAllClasses, this)
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

            }
    }
}