package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.lang.ref.Reference
import java.util.UUID

class AddItem : AppCompatActivity() {

    private lateinit var itemCategory: EditText
    private lateinit var itemName: EditText
    private lateinit var itemPrice: EditText
    private lateinit var itemQty: EditText
    private lateinit var itemDesc: EditText
    private lateinit var itemPhoto1 : ImageView
    private lateinit var itemPhoto2 : ImageView
    private lateinit var itemPhoto3 : ImageView
    private lateinit var btnAddItem: AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: FirebaseStorage

//    private val PICK_IMAGE_REQUEST_CODE = 123
    private var selectedImageUri1: Uri? = null
    private var selectedImageUri2: Uri? = null
    private var selectedImageUri3: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemCategory = findViewById(R.id.et_item_category)
        itemName = findViewById(R.id.et_item_name)
        itemPrice = findViewById(R.id.et_item_price)
        itemQty = findViewById(R.id.et_item_quantity)
        itemDesc = findViewById(R.id.et_item_description)
        itemPhoto1 = findViewById(R.id.item_image1)
        itemPhoto2 = findViewById(R.id.item_image2)
        itemPhoto3 = findViewById(R.id.item_image3)
        btnAddItem = findViewById(R.id.btn_add_item)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        itemPhoto1.setOnClickListener {
            openImagePicker(1)
        }

        itemPhoto2.setOnClickListener {
            openImagePicker(2)
        }

        itemPhoto3.setOnClickListener {
            openImagePicker(3)
        }

        btnAddItem.setOnClickListener {
            addNewItem()
        }

    }

    private fun openImagePicker(requestCode: Int){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data
            when (requestCode) {
                1 -> {
                    selectedImageUri1 = selectedImageUri
                    loadSelectedImage(selectedImageUri1, itemPhoto1)
                }
                2 -> {
                    selectedImageUri2 = selectedImageUri
                    loadSelectedImage(selectedImageUri2, itemPhoto2)
                }
                3 -> {
                    selectedImageUri3 = selectedImageUri
                    loadSelectedImage(selectedImageUri3, itemPhoto3)
                }
            }
        }
    }

    private fun loadSelectedImage(imageUri: Uri?, imageView: ImageView) {
        if (imageUri != null) {
            // Use a suitable library or method to load and display the image in the ImageView.
            // For example, you can use Glide or Picasso for efficient image loading.
            // Here's an example using Glide (make sure to add the Glide dependency to your app):

            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }
    }

    private fun addNewItem(){
        val currentUser = auth.currentUser
        val uId = currentUser?.uid
        val randomId = UUID.randomUUID().toString()

        if (uId != null){
            val storageReference = FirebaseStorage.getInstance().reference
            val uploadedImageUrls = ArrayList<String>()  // Create a list to store the uploaded image URIs.
            val uploadTasks = mutableListOf<Task<UploadTask.TaskSnapshot>>()

            // Upload the three images
            for ((index, imageUri) in listOf(selectedImageUri1, selectedImageUri2, selectedImageUri3).withIndex()) {
                if (imageUri != null) {
                    val imageName = "image${index + 1}_${System.currentTimeMillis()}"
                    val imageRef = storageReference.child("Images/$uId/$imageName")

                    val uploadTask = imageRef.putFile(imageUri)
                    uploadTasks.add(uploadTask)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                            // Store the downloaded URI in the list
                            uploadedImageUrls.add(uri.toString())

                            // If all images are uploaded, proceed to store data in Firestore
                            if (uploadedImageUrls.size == 3) {
                                //val iCategory = itemCategory.text.toString()
                                val iName = itemName.text.toString()
                                val iPrice = itemPrice.text.toString()
                                val iQty = itemQty.text.toString()
                                val iDesc = itemDesc.text.toString()

                                val sellerMap = hashMapOf(
                                    "seller ID" to uId,
                                    "random ID" to randomId,
                                    //"item Category" to iCategory,
                                    "item Name" to iName,
                                    "item Price" to iPrice,
                                    "item Qty" to iQty,
                                    "item Description" to iDesc,
                                    "urls" to uploadedImageUrls // Store all image URIs
                                )

                                // Store data in Firestore
                                database.collection("sellerItems").document(randomId).set(sellerMap)
                                database.collection("sellerItemsBySellerID").document(uId)
                                    .collection("singleSellerItems").document(randomId)
                                    .set(sellerMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {err ->
                                        Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
    }
}