package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User_SellerProfileView : AppCompatActivity() {
    private lateinit var sellerDp : ShapeableImageView
    private lateinit var sellerPhone : ImageView
    private lateinit var sellerAddress : ImageView
    private lateinit var sellerName : TextView
    private lateinit var sellerBio : TextView

    private lateinit var btnMore : TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var specificSellerItems: ArrayList<AllItems>

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_seller_profile_view)

        sellerDp = findViewById(R.id.sellerProfile_sellerUserPhoto)
        sellerPhone = findViewById(R.id.sellerProfile_sellerPhone)
        sellerAddress = findViewById(R.id.sellerProfile_sellerAddress)
        sellerName = findViewById(R.id.sellerProfile_sellerName)
        sellerBio = findViewById(R.id.sellerProfile_sellerDesc)

        btnMore = findViewById(R.id.sellerProfile_yourItemsMoreText)
        recyclerView = findViewById(R.id.sellerProfile_yourItemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        specificSellerItems = arrayListOf()
        database = FirebaseFirestore.getInstance()

        val sellerId = intent.getStringExtra("sellerId").toString()

        if (sellerId != null){
            database.collection("sellers").document(sellerId).get()
                .addOnSuccessListener {
                    if (it.exists()){
                        val sellerData = it.data
                        val sellerNameValue = sellerData?.get("sellerName") as String
                        val sellerBioValue = sellerData?.get("sellerDescription") as String
                        val sellerPhoneValue = sellerData?.get("sellerNumber") as String
                        val sellerAddressValue = sellerData?.get("sellerAddress") as String
                        val sellerDpValue = sellerData?.get("url") as String

                        Glide.with(this).load(sellerDpValue).into(sellerDp)
                        sellerName.text = sellerNameValue
                        sellerBio.text = sellerBioValue

                        sellerPhone.setOnClickListener {
                            showPopupMessage(sellerPhone, sellerPhoneValue)
                        }
                        sellerAddress.setOnClickListener {
                            showPopupMessage(sellerAddress, sellerAddressValue)
                        }

                    }
                }

            database.collection("sellerItemsBySellerID").document("$sellerId").collection("sellerItems").get()
                .addOnSuccessListener {
                    for (data in it.documents){
                        val yourItems:AllItems? = data.toObject(AllItems::class.java)
                        if(yourItems != null){
                            specificSellerItems.add(yourItems)
                            Log.d("Firestore", "Added item: ${yourItems.itemName}")

                        }
                    }
                    recyclerView.adapter = User_SpecificSellerItemsAdapater(specificSellerItems, this)
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

                }

            btnMore.setOnClickListener {
                val intent = Intent(this, SpecificSellerItems::class.java)
                intent.putExtra("sellerId", sellerId)
                startActivity(intent)
            }
        }

    }
    private fun showPopupMessage(imageView: ImageView, message: String) {
        val popupView = LayoutInflater.from(this).inflate(R.layout.popup_message, null)
        val messageTextView = popupView.findViewById<TextView>(R.id.messageTextView)
        val copyIcon = popupView.findViewById<ImageView>(R.id.copyIcon)

        messageTextView.text = message

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        // Set outside touchable to dismiss the popup when tapped outside
        popupWindow.isOutsideTouchable = true

        // Handle the copy icon click event
        copyIcon.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("Message", message)
            clipboard.setPrimaryClip(clip)
            popupWindow.dismiss()
        }

        // Show the popup window at a specific location on the screen
        val location = IntArray(2)
        imageView.getLocationOnScreen(location)
        popupWindow.showAtLocation(imageView, Gravity.NO_GRAVITY, location[0] - popupWindow.width, location[1] + imageView.height)

    }
}