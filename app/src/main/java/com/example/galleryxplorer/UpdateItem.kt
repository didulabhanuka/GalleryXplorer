package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class UpdateItem : AppCompatActivity() {

    private lateinit var itemName: TextInputEditText
    private lateinit var itemCategory: AutoCompleteTextView
    private lateinit var itemMedium: TextInputEditText
    private lateinit var itemSubject: TextInputEditText
    private lateinit var itemYear: TextInputEditText
    private lateinit var itemSize: TextInputEditText
    private lateinit var itemPrice: TextInputEditText
    private lateinit var sellerId: TextView
    private lateinit var randomId: TextView
    private lateinit var itemMainImage: ImageView
    private lateinit var itemSecondImage: ImageView
    private lateinit var itemThirdImage: ImageView
    private lateinit var btnUpdate: AppCompatButton

    private var selectedImageUri1: Uri? = null
    private var selectedImageUri2: Uri? = null
    private var selectedImageUri3: Uri? = null

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: FirebaseStorage

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_item)

        itemName = findViewById(R.id.et_update_item_name)
        itemCategory = findViewById(R.id.et_update_item_category)
        itemMedium = findViewById(R.id.et_update_item_medium)
        itemSubject = findViewById(R.id.et_update_item_subject)
        itemYear = findViewById(R.id.et_update_item_year)
        itemSize = findViewById(R.id.et_update_item_size)
        itemPrice = findViewById(R.id.et_update_item_price)
        sellerId = findViewById(R.id.et_update_item_sellerId)
        randomId = findViewById(R.id.et_update_item_randomId)
        itemMainImage = findViewById(R.id.item_update_image1)
        itemSecondImage = findViewById(R.id.item_update_image2)
        itemThirdImage = findViewById(R.id.item_update_image3)

        btnUpdate = findViewById(R.id.btn_update_item)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        randomId.text = intent.getStringExtra("randomId").toString()
        sellerId.text = intent.getStringExtra("sellerId").toString()
        itemName.setText(intent.getStringExtra("itemName").toString())
        itemCategory.setText(intent.getStringExtra("itemCategory").toString())
        itemMedium.setText(intent.getStringExtra("itemMedium").toString())
        itemSubject.setText(intent.getStringExtra("itemSubject").toString())
        itemYear.setText(intent.getStringExtra("itemYear").toString())
        itemSize.setText(intent.getStringExtra("itemSize").toString())
        itemPrice.setText(intent.getStringExtra("itemPrice").toString())

        val imageUrls = intent.getStringArrayListExtra("urls")
        if (imageUrls != null && imageUrls.isNotEmpty()) {
            Glide.with(this).load(imageUrls[0]).into(itemMainImage)
            if (imageUrls.size >= 2) {
                Glide.with(this).load(imageUrls[1]).into(itemSecondImage)
            }
            if (imageUrls.size >= 3) {
                Glide.with(this).load(imageUrls[2]).into(itemThirdImage)
            }
        } else {
            // Handle the case when image URLs are not available
            // You can hide or show placeholders, or display a message as needed.
        }

        setupCategoryDropdown()

        itemMainImage.setOnClickListener {
            openImagePicker(1)
        }

        itemSecondImage.setOnClickListener {
            openImagePicker(2)
        }

        itemThirdImage.setOnClickListener {
            openImagePicker(3)
        }

        btnUpdate.setOnClickListener {
            updateItem()
        }
    }

    private fun setupCategoryDropdown() {
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, CategoryData.categories)
        itemCategory.setAdapter(categoryAdapter)
    }

    private fun openImagePicker(requestCode: Int) {
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
                    loadSelectedImage(selectedImageUri1, itemMainImage)
                }
                2 -> {
                    selectedImageUri2 = selectedImageUri
                    loadSelectedImage(selectedImageUri2, itemSecondImage)
                }
                3 -> {
                    selectedImageUri3 = selectedImageUri
                    loadSelectedImage(selectedImageUri3, itemThirdImage)
                }
            }
        }
    }

    private fun loadSelectedImage(imageUri: Uri?, imageView: ImageView) {
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }
    }

    private fun updateItem() {
        val itemNameText = itemName.text.toString()
        val itemCategoryText = itemCategory.text.toString()
        val itemMediumText = itemMedium.text.toString()
        val itemSubjectText = itemSubject.text.toString()
        val itemYearText = itemYear.text.toString()
        val itemSizeText = itemSize.text.toString()
        val itemPriceText = itemPrice.text.toString()


        val updatedImageUrls = ArrayList<String>()  // Create a list to store the uploaded image URIs.
        val uploadTasks = mutableListOf<Task<UploadTask.TaskSnapshot>>()

        if (itemNameText.isEmpty() || itemCategoryText.isEmpty() || itemMediumText.isEmpty() || itemSubjectText.isEmpty() ||
            itemYearText.isEmpty() || itemSizeText.isEmpty() || itemPriceText.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val storageReference = FirebaseStorage.getInstance().reference

        for ((index, imageUri) in listOf( selectedImageUri1, selectedImageUri2, selectedImageUri3).withIndex()) {
            if (imageUri != null) {
                val imageName = "image${index + 1}_${System.currentTimeMillis()}"
                val imageRef = storageReference.child("Images/${intent.getStringExtra("sellerId").toString()}/$imageName")

                val uploadTask = imageRef.putFile(imageUri)
                uploadTasks.add(uploadTask)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                        // Store the downloaded URI in the list
                        updatedImageUrls.add(uri.toString())

                        if(updatedImageUrls.size >=1 ){
                            val updatedItemData = mapOf(
                                "itemName" to itemNameText,
                                "itemCategory" to itemCategoryText,
                                "itemMedium" to itemMediumText,
                                "itemSubject" to itemSubjectText,
                                "itemYear" to itemYearText,
                                "itemSize" to itemSizeText,
                                "itemPrice" to itemPriceText,
                                "urls" to updatedImageUrls
                            )

                            database.collection("allItems").document(intent.getStringExtra("randomId").toString())
                                .update(updatedItemData)
                            database.collection("categories").document("$itemCategoryText")
                                .collection("categoryItems").document(intent.getStringExtra("randomId").toString())
                                .set(updatedItemData)
                            database.collection("sellerItemsBySellerID")
                                .document(intent.getStringExtra("sellerId").toString())
                                .collection("sellerItems")
                                .document(intent.getStringExtra("randomId").toString()).update(updatedItemData)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show()

                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "Item updated fail", Toast.LENGTH_SHORT).show()

                                }
                        }
                    }
                }.addOnFailureListener { exception ->
                        Toast.makeText(this, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}