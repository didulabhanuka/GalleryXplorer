package com.example.galleryxplorer

data class AllItems(
    val sellerName: String? = null,
    val sellerId: String? = null,
    val itemCategory: String? = null,
    val itemName: String? = null,
    val itemMedium: String? = null,
    val itemSubject: String? = null,
    val itemYear: String? = null,
    val itemSize: String? = null,
    val itemPrice: String? = null,
    val urls: List<String>? = null

)
