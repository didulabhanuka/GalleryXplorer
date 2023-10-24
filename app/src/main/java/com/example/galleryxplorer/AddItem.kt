package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import java.util.UUID

class AddItem : AppCompatActivity() {

    private lateinit var itemCategory: AutoCompleteTextView
    private lateinit var itemName: EditText
    private lateinit var itemMedium: EditText
    private lateinit var itemSubject: EditText
    private lateinit var itemYear: EditText
    private lateinit var itemSize: EditText
    private lateinit var itemPrice: EditText
    private lateinit var itemPhoto1 : ImageView
    private lateinit var itemPhoto2 : ImageView
    private lateinit var itemPhoto3 : ImageView
    private lateinit var btnAddItem: AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: FirebaseStorage

    private var selectedImageUri1: Uri? = null
    private var selectedImageUri2: Uri? = null
    private var selectedImageUri3: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemCategory = findViewById(R.id.et_item_category)
        itemName = findViewById(R.id.et_item_name)
        itemMedium = findViewById(R.id.et_item_medium)
        itemSubject = findViewById(R.id.et_item_subject)
        itemYear = findViewById(R.id.et_item_year)
        itemSize = findViewById(R.id.et_item_size)
        itemPrice = findViewById(R.id.et_item_price)
        itemPhoto1 = findViewById(R.id.item_image1)
        itemPhoto2 = findViewById(R.id.item_image2)
        itemPhoto3 = findViewById(R.id.item_image3)
        btnAddItem = findViewById(R.id.btn_add_item)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        setupCategoryDropdown()

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

    private fun setupCategoryDropdown() {
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, CategoryData.categories)
        itemCategory.setAdapter(categoryAdapter)
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

            getSellerName(uId) {sellerName ->
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
                                if (uploadedImageUrls.size >= 1) {
                                    val iCategory = itemCategory.text.toString()
                                    val iName = itemName.text.toString()
                                    val iMedium = itemMedium.text.toString()
                                    val iSubject = itemSubject.text.toString()
                                    val iYear = itemYear.text.toString()
                                    val iSize = itemSize.text.toString()
                                    val iPrice = itemPrice.text.toString()

                                    val sellerMap = hashMapOf(
                                        "sellerId" to uId,
                                        "randomId" to randomId,
                                        "sellerName" to sellerName,
                                        "itemCategory" to iCategory,
                                        "itemName" to iName,
                                        "itemMedium" to iMedium,
                                        "itemSubject" to iSubject,
                                        "itemYear" to iYear,
                                        "itemSize" to iSize,
                                        "itemPrice" to iPrice,
                                        "urls" to uploadedImageUrls
                                    )

                                    database.collection("allItems").document(randomId).set(sellerMap)
                                    database.collection("categories").document("$iCategory").collection("categoryItems").document(randomId).set(sellerMap)
                                    database.collection("sellerItemsBySellerID").document(uId)
                                        .collection("sellerItems").document(randomId)
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
}