package com.example.galleryxplorer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class User_Calendar : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventList: ArrayList<Events>
    private var database = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_calendar)

        calendarView = findViewById(R.id.eventsCalendar)
        eventsRecyclerView = findViewById(R.id.eventsCalendarRecyclerView)

        eventList = arrayListOf()
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)


        // Set up a listener for date changes on the CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
            fetchEvents(selectedDate)
        }
    }

    private fun fetchEvents(selectedDate: String) {
        // Query Firestore for events on the selected date
        database.collection("eventDates")
            .document(selectedDate)
            .collection("events")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val hasEvents = !querySnapshot.isEmpty()
                highlightDate(hasEvents)

                val eventList = ArrayList<Events>()
                for (document in querySnapshot.documents) {
                    val event = document.toObject(Events::class.java)
                    event?.let {
                        eventList.add(it)
                    }
                }

                // Update the RecyclerView with the events data
                val adapter = EventCalendarAdapter(eventList)
                eventsRecyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    private fun highlightDate(hasEvents: Boolean) {
        if (hasEvents) {
            // Set a background color or other visual indication for the selected date
            calendarView.setBackgroundColor(resources.getColor(R.color.elements_color))
        } else {
            // Reset the background color to the default
            calendarView.setBackgroundColor(resources.getColor(android.R.color.transparent))
        }
    }
}
