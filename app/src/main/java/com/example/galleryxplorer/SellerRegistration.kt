package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class SellerRegistration : AppCompatActivity() {

    private lateinit var sellerName: EditText
    private lateinit var sellerNumber: EditText
    private lateinit var sellerAddress: EditText
    private lateinit var sellerDescription: EditText
    private lateinit var btnCreateSeller: AppCompatButton
    private lateinit var btnUploadSellerImage: AppCompatButton

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.firestore
    private lateinit var storageReference: FirebaseStorage
    private lateinit var uri: Uri

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_registration)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance() // Initialize storageReference

        sellerName = findViewById(R.id.et_seller_name)
        sellerNumber = findViewById(R.id.et_seller_phone)
        sellerAddress = findViewById(R.id.et_seller_address)
        sellerDescription = findViewById(R.id.et_seller_description)
        btnCreateSeller = findViewById(R.id.btnCreateNewStore)
        btnUploadSellerImage = findViewById(R.id.btnSellerUploadPhoto)

        btnUploadSellerImage.setOnClickListener {
            selectImage()
        }

        btnCreateSeller.setOnClickListener {
            saveSellerData()
        }
    }

    private fun saveSellerData() {
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        if(uId != null){
            storageReference.getReference("Image/$uId").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->

                            val sName = sellerName.text.toString()
                            val sNumber = sellerNumber.text.toString()
                            val sAddress = sellerAddress.text.toString()
                            val sDescription = sellerDescription.text.toString()

                            if(sName.isEmpty()){
                                sellerName.error = "Please enter shop or seller name"
                            }
                            if (sNumber.isEmpty()){
                                sellerNumber.error = "Please enter shop address"
                            }
                            if(sAddress.isEmpty()){
                                sellerAddress.error = "Please enter contact number"
                            }
                            if(sDescription.isEmpty()){
                                sellerDescription.error = "Please enter shop or seller description"
                            }

                            // Create a data class or a map to represent the store information
                            val storeData = hashMapOf(
                                "sellerId" to uId,
                                "sellerName" to sName,
                                "sellerNumber" to sNumber,
                                "sellerAddress" to sAddress,
                                "sellerDescription" to sDescription,
                                "url" to uri
                            )

                            // Save the store data under the user's UID as a Firestore document
                            database.collection("sellers").document(uId.toString())
                                .set(storeData, SetOptions.merge())
                                .addOnSuccessListener {

                                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()

                                }.addOnFailureListener{err ->
                                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        uri = imageUri
                        btnUploadSellerImage.text = "Image Selected: {$uri}"

                    }
                }
            }
        }
    }
}
