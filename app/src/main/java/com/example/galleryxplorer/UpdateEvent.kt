package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlin.math.log

class UpdateEvent : AppCompatActivity() {

    private lateinit var eventCategory: AutoCompleteTextView
    private lateinit var eventName: TextInputEditText
    private lateinit var eventVenue: TextInputEditText
    private lateinit var eventTime: TextInputEditText
    private lateinit var eventDate: TextInputEditText
    private lateinit var eventOrganizers: TextInputEditText
    private lateinit var eventDescription: TextView
    private lateinit var sellerName: TextView
    private lateinit var sellerId: TextView
    private lateinit var randomId: TextView
    private lateinit var eventBanner: ImageView
    private lateinit var btnUpdateEvent: AppCompatButton

    private var database = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: FirebaseStorage

    private var selectedImageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_event)

        eventCategory = findViewById(R.id.et_updateEvent_category)
        eventName = findViewById(R.id.et_updateEvent_name)
        eventVenue = findViewById(R.id.et_updateEvent_venue)
        eventTime = findViewById(R.id.et_updateEvent_time)
        eventDate = findViewById(R.id.et_updateEvent_date)
        eventOrganizers = findViewById(R.id.et_updateEvent_organizers)
        eventDescription = findViewById(R.id.et_updateEvent_description)
        sellerName = findViewById(R.id.et_updateEvent_sellerName)
        sellerId = findViewById(R.id.et_updateEvent_sellerId)
        randomId = findViewById(R.id.et_updateEvent_randomId)
        eventBanner = findViewById(R.id.updateEvent_banner)

        btnUpdateEvent = findViewById(R.id.btn_update_event)

        storageReference = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        sellerName.text = intent.getStringExtra("sellerName").toString()
        sellerId.text = intent.getStringExtra("sellerId").toString()
        randomId.text = intent.getStringExtra("randomId").toString()
        eventCategory.setText(intent.getStringExtra("eventCategory").toString())
        eventName.setText(intent.getStringExtra("eventName").toString())
        eventVenue.setText(intent.getStringExtra("eventVenue").toString())
        eventTime.setText(intent.getStringExtra("eventTime").toString())
        eventDate.setText(intent.getStringExtra("eventDate").toString())
        eventOrganizers.setText(intent.getStringExtra("eventOrganizers").toString())
        eventDescription.setText(intent.getStringExtra("eventDescription").toString())

        val imageUrl = intent.getStringExtra("urls")
        Glide.with(this).load(imageUrl).into(eventBanner)

        setupCategoryDropdown()

        eventBanner.setOnClickListener {
            openImagePicker()
        }

        btnUpdateEvent.setOnClickListener {
            updateEvent()
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
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                eventTime.setText(formattedTime)
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
        if (resultCode == RESULT_OK) {
            if (data != null && data.data != null) {
                val selectedImageUris = data.data
                selectedImageUri = selectedImageUris
                loadSelectedImage(selectedImageUri, eventBanner)
            } else {
                // No image was picked, set selectedImageUri to the value from the intent extra "urls"
                selectedImageUri = Uri.parse(intent.getStringExtra("urls"))
                loadSelectedImage(selectedImageUri, eventBanner)
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

    private fun updateEvent(){
        val eventCategoryText = eventCategory.text.toString()
        val eventNameText = eventName.text.toString()
        val eventVenueText = eventVenue.text.toString()
        val eventTimeText = eventTime.text.toString()
        val eventDateText = eventDate.text.toString()
        val eventOrganizersText = eventOrganizers.text.toString()
        val eventDescriptionText = eventDescription.text.toString()

        if (eventCategoryText.isEmpty() || eventNameText.isEmpty() || eventVenueText.isEmpty() || eventTimeText.isEmpty() ||
            eventDateText.isEmpty() || eventOrganizersText.isEmpty() || eventDescriptionText.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val uploadTasks = mutableListOf<Task<UploadTask.TaskSnapshot>>()
        val storageReference = FirebaseStorage.getInstance().reference

        val selectedImage = selectedImageUri
        if (selectedImage != null) {
            val imageName = "image_${System.currentTimeMillis()}"
            val imageRef = storageReference.child("eventBanners/${intent.getStringExtra("sellerId").toString()}/$imageName")

            val uploadTask = imageRef.putFile(selectedImage)
            uploadTasks.add(uploadTask)

            uploadTask.addOnSuccessListener {
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    val updatedImageUrl = uri.toString()

                    // Get the original eventDate from the intent data
                    val originalEventDate = intent.getStringExtra("eventDate").toString()


                    val updatedEventMap = mapOf(
                        "eventCategory" to eventCategoryText,
                        "eventName" to eventNameText,
                        "eventVenue" to eventVenueText,
                        "eventTime" to eventTimeText,
                        "eventDate" to eventDateText,
                        "eventOrganizers" to eventOrganizersText,
                        "eventDescription" to eventDescriptionText,
                        "urls" to updatedImageUrl
                    )

                    // Delete the old document with the original eventDate
                    database.collection("eventDates").document(originalEventDate)
                        .collection("events")
                        .document(intent.getStringExtra("randomId").toString())
                        .delete()

                    database.collection("allEvents").document(intent.getStringExtra("randomId").toString()).update(updatedEventMap)
                    database.collection("eventsBySellerID").document(intent.getStringExtra("sellerId").toString()).collection("sellerEvents").document(intent.getStringExtra("randomId").toString()).update(updatedEventMap)
                    database.collection("eventDates").document(eventDateText)
                        .collection("events")
                        .document(intent.getStringExtra("randomId").toString())
                        .set(updatedEventMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show()
                            finish()
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