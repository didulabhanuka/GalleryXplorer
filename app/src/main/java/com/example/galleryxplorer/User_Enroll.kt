package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class User_Enroll : AppCompatActivity() {

    private lateinit var itemName: EditText
    private lateinit var itemMedium: EditText
    private lateinit var itemYear: EditText
    private lateinit var itemSize: EditText
    private lateinit var btnAddItem: AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: FirebaseStorage

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_enroll)

        itemName = findViewById(R.id.et_item_name)
        itemMedium = findViewById(R.id.et_item_medium)
        itemYear = findViewById(R.id.et_item_year)
        itemSize = findViewById(R.id.et_item_size)
        btnAddItem = findViewById(R.id.btn_add_item)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        btnAddItem.setOnClickListener {
            addNewItem()
        }
    }

    private fun addNewItem() {
        val currentUser = auth.currentUser
        val uId = currentUser?.uid
        val randomId = UUID.randomUUID().toString()

        if (uId != null) {
            val uploadedImageUrls = ArrayList<String>()  // Create a list to store the uploaded image URIs.

            fun getSellerName(uId: String, callback: (String?) -> Unit) {
                val sellerRef = database.collection("sellers").document("$uId")
                sellerRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val sellerName = documentSnapshot.getString("sellerName")
                            callback(sellerName)
                        } else {
                            callback(null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        callback(null)
                        Toast.makeText(this, "Error getting seller name: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }

            getSellerName(uId) { sellerName ->
                val iName = itemName.text.toString()
                val iMedium = itemMedium.text.toString()
                val iYear = itemYear.text.toString()
                val iSize = itemSize.text.toString()

                if (iName.isNotEmpty() && iMedium.isNotEmpty() && iYear.isNotEmpty() && iSize.isNotEmpty()) {
                    val sellerMap = hashMapOf(
                        "sellerId" to uId,
                        "randomId" to randomId,
                        "sellerName" to sellerName,
                        "itemName" to iName,
                        "itemMedium" to iMedium,
                        "itemYear" to iYear,
                        "itemSize" to iSize,
                        "urls" to uploadedImageUrls
                    )

                    database.collection("allEnrolls").document(randomId).set(sellerMap)
                    database.collection("enrollcategory").document("Category").collection("categoryEnrolls").document(randomId).set(sellerMap)
                    database.collection("userEnrollsByUserID").document(uId)
                        .collection("userEnrolls").document(randomId)
                        .set(sellerMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { err ->
                            Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
