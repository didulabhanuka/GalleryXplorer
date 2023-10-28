package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class Seller_SingleEventView : AppCompatActivity() {

    private lateinit var sellerId: TextView
    private lateinit var randomId: TextView
    private lateinit var sellerName: TextView
    private lateinit var eventCategory: TextView
    private lateinit var eventName: TextView
    private lateinit var eventVenue: TextView
    private lateinit var eventTime: TextView
    private lateinit var eventDate: TextView
    private lateinit var eventOrganizers: TextView
    private lateinit var eventDescription: TextView
    private lateinit var eventBanner: ImageView

    private lateinit var btnUpdate: AppCompatButton
    private lateinit var btnDelete: AppCompatButton

    private val database = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_single_event_view)

        sellerId = findViewById(R.id.event_view_sellerId)
        randomId = findViewById(R.id.event_view_randomId)
        sellerName = findViewById(R.id.event_view_seller_name)
        eventCategory = findViewById(R.id.event_view_category)
        eventName = findViewById(R.id.event_view_name)
        eventVenue = findViewById(R.id.event_view_venue)
        eventTime = findViewById(R.id.event_view_time)
        eventDate = findViewById(R.id.event_view_date)
        eventOrganizers = findViewById(R.id.event_view_guests)
        eventDescription = findViewById(R.id.event_view_description)
        eventBanner = findViewById(R.id.event_view_eventBanner)

        btnUpdate = findViewById(R.id.btn_event_update)
        btnDelete = findViewById(R.id.btn_event_delete)

        sellerId.text = intent.getStringExtra("sellerId").toString()
        randomId.text = intent.getStringExtra("randomId").toString()
        sellerName.text = intent.getStringExtra("sellerName").toString()
        eventCategory.text = intent.getStringExtra("eventCategory").toString()
        eventName.text = intent.getStringExtra("eventName").toString()
        eventVenue.text = intent.getStringExtra("eventVenue").toString()
        eventTime.text = intent.getStringExtra("eventTime").toString()
        eventDate.text = intent.getStringExtra("eventDate").toString()
        eventOrganizers.text = intent.getStringExtra("eventOrganizers").toString()
        eventDescription.text = intent.getStringExtra("eventDescription").toString()

        val imageUrl = intent.getStringExtra("urls")
        Glide.with(this).load(imageUrl).into(eventBanner)

        btnUpdate.setOnClickListener {
            val intent = Intent(this, UpdateEvent::class.java)

            intent.putExtra("sellerId", sellerId.text.toString())
            intent.putExtra("randomId", randomId.text.toString())
            intent.putExtra("sellerName", sellerName.text.toString())
            intent.putExtra("eventCategory", eventCategory.text.toString())
            intent.putExtra("eventName", eventName.text.toString())
            intent.putExtra("eventVenue", eventVenue.text.toString())
            intent.putExtra("eventTime", eventTime.text.toString())
            intent.putExtra("eventDate", eventDate.text.toString())
            intent.putExtra("eventOrganizers", eventOrganizers.text.toString())
            intent.putExtra("eventDescription", eventDescription.text.toString())

            if (imageUrl != null) {
                intent.putExtra("urls", imageUrl)
                Log.d("MyTag", "$imageUrl")
            } else {
                Log.d("MyTag", "Urls is null")
            }

            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { dialog, _ ->
                deleteItem()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun deleteItem() {
        val randomEventId = randomId.text.toString()

        // Use Firestore to delete the item with the specified randomId
        database.collection("eventsBySellerID").document(intent.getStringExtra("sellerId").toString()).collection("sellerEvents").document(intent.getStringExtra("randomId").toString()).delete()
        database.collection("allEvents").document(randomEventId).delete()
        database.collection("eventDates").document(intent.getStringExtra("eventDate").toString())
            .collection("events")
            .document(intent.getStringExtra("randomId").toString())
            .delete()
            .addOnSuccessListener {
                val intent = Intent(this, Seller_YourEvents::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                // Handle the error if the deletion fails
                Log.e("DeleteItem", "Error deleting item: $e")
            }
    }
}