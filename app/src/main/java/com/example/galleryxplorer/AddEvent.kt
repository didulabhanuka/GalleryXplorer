package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ComponentCallbacks
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.util.*

class AddEvent : AppCompatActivity() {

    private lateinit var eventCategory: AutoCompleteTextView
    private lateinit var eventName: EditText
    private lateinit var eventVenue: EditText
    private lateinit var eventTime: EditText
    private lateinit var eventDate: EditText
    private lateinit var eventOrganizers: EditText
    private lateinit var eventDescription: EditText
    private lateinit var eventBanner : ImageView
    private lateinit var btnAddEvent: AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: FirebaseStorage

    private var selectedImageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        eventCategory = findViewById(R.id.et_event_category)
        eventName = findViewById(R.id.et_event_name)
        eventVenue = findViewById(R.id.et_event_venue)
        eventTime = findViewById(R.id.et_event_time)
        eventDate = findViewById(R.id.et_event_date)
        eventOrganizers = findViewById(R.id.et_event_organizers)
        eventDescription = findViewById(R.id.et_event_description)
        eventBanner = findViewById(R.id.event_banner)
        btnAddEvent = findViewById(R.id.btn_add_event)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        setupCategoryDropdown()

        eventBanner.setOnClickListener {
            openImagePicker()
        }

        btnAddEvent.setOnClickListener {
            addNewEvent()
        }

    }

    private fun setupCategoryDropdown() {
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, CategoryData.categories)
        eventCategory.setAdapter(categoryAdapter)
    }

    fun showDatePickerDialog(view: View) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
                eventDate.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    fun showTimePickerDialog(view: View) {
        val editText = findViewById<EditText>(R.id.et_event_time)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime)
            },
            hour, minute, true
        )

        timePickerDialog.show()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUris = data.data
            selectedImageUri = selectedImageUris
            loadSelectedImage(selectedImageUri, eventBanner)
        }
    }

    private fun loadSelectedImage(imageUri: Uri?, imageView: ImageView) {
        if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(imageView)
        }
    }

    private fun addNewEvent(){
        val currentUser = auth.currentUser
        val uId = currentUser?.uid
        val randomId = UUID.randomUUID().toString()

        if (uId != null){
            val storageReference = FirebaseStorage.getInstance().reference

            val uploadTasks = mutableListOf<Task<UploadTask.TaskSnapshot>>()

            fun getSellerName(uId: String, callback: (String?) -> Unit){
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
                val selectedImage = selectedImageUri
                if (selectedImage != null) {
                    val imageName = "image_${System.currentTimeMillis()}"
                    val imageRef = storageReference.child("eventBanners/$uId/$imageName")

                    val uploadTask = imageRef.putFile(selectedImage)
                    uploadTasks.add(uploadTask)

                    uploadTask.addOnSuccessListener {
                        it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->

                            val uploadedImageUrl = uri.toString()

                            val eCategory = eventCategory.text.toString()
                            val eName = eventName.text.toString()
                            val eVenue = eventVenue.text.toString()
                            val eTime = eventTime.text.toString()
                            val eDate = eventDate.text.toString()
                            val eOrganizers = eventOrganizers.text.toString()
                            val eDescription = eventDescription.text.toString()

                            val eventMap = hashMapOf(
                                "sellerId" to uId,
                                "randomId" to randomId,
                                "sellerName" to sellerName,
                                "eventCategory" to eCategory,
                                "eventName" to eName,
                                "eventVenue" to eVenue,
                                "eventTime" to eTime,
                                "eventDate" to eDate,
                                "eventOrganizers" to eOrganizers,
                                "eventDescription" to eDescription,
                                "urls" to uploadedImageUrl
                            )

                            database.collection("allEvents").document(randomId).set(eventMap)
                            database.collection("eventDates").document("$eDate").collection("events").document(randomId).set(eventMap)
                            database.collection("eventsBySellerID").document(uId)
                                .collection("sellerEvents").document(randomId)
                                .set(eventMap)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {err ->
                                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}