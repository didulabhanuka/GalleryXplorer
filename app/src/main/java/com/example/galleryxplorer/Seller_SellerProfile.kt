package com.example.galleryxplorer

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

class Seller_SellerProfile : AppCompatActivity() {
    private lateinit var sellerDp : ShapeableImageView
    private lateinit var sellerPhone : ImageView
    private lateinit var sellerAddress : ImageView
    private lateinit var sellerName : TextView
    private lateinit var sellerBio : TextView

    private lateinit var btnMoreItems : TextView
    private lateinit var btnMoreClasses : TextView
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var recyclerViewClasses: RecyclerView
    private lateinit var profileItemList: ArrayList<YourItems>
    private lateinit var profileClassList: ArrayList<YourClasses>

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_seller_profile)

        sellerDp = findViewById(R.id.sellerProfile_sellerUserPhoto)
        sellerPhone = findViewById(R.id.sellerProfile_sellerPhone)
        sellerAddress = findViewById(R.id.sellerProfile_sellerAddress)
        sellerName = findViewById(R.id.sellerProfile_sellerName)
        sellerBio = findViewById(R.id.sellerProfile_sellerDesc)

        btnMoreItems = findViewById(R.id.sellerProfile_yourItemsMoreText)
        btnMoreClasses = findViewById(R.id.sellerProfile_yourClassesMoreText)
        recyclerViewItems = findViewById(R.id.sellerProfile_yourItemsRecyclerView)
        recyclerViewItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        profileItemList = arrayListOf()

        recyclerViewClasses = findViewById(R.id.sellerProfile_yourClassesRecyclerView)
        recyclerViewClasses.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        profileClassList = arrayListOf()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uId = currentUser?.uid

        if (uId != null){
            database.collection("sellers").document(uId).get()
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

            database.collection("sellerItemsBySellerID").document("$uId").collection("sellerItems").get()
                .addOnSuccessListener {
                    for (data in it.documents){
                        val yourItems:YourItems? = data.toObject(YourItems::class.java)
                        if(yourItems != null){
                            profileItemList.add(yourItems)
                            Log.d("Firestore", "Added item: ${yourItems.itemName}")

                        }
                    }
                    recyclerView.adapter = ProfileAllItemsAdapter(profileItemList, this)
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()

                }

            btnMore.setOnClickListener {
                val intent = Intent(this, Seller_YourItems::class.java)
                startActivity(intent)
            }

            // Load Classes
            database.collection("sellerClassesBySellerID").document(uId).collection("sellerClasses").get()
                .addOnSuccessListener {
                    for (data in it.documents){
                        val yourClasses: YourClasses? = data.toObject(YourClasses::class.java)
                        if(yourClasses != null){
                            profileClassList.add(yourClasses)
                            Log.d("Firestore", "Added class: ${yourClasses.itemName}")
                        }
                    }
                    recyclerViewClasses.adapter = ProfileAllClassesAdapter(profileClassList, this)
                    Toast.makeText(this, "Classes Loaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }

            btnMoreItems.setOnClickListener {
                val intent = Intent(this, Seller_YourItems::class.java)
                startActivity(intent)
            }

            btnMoreClasses.setOnClickListener {
                val intent = Intent(this, Seller_YourClasses::class.java)
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
