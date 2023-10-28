package com.example.galleryxplorer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class CategoriesPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_page)

        val paintingLayout : LinearLayout = findViewById(R.id.cat_painting)
        val paintingText : TextView = findViewById(R.id.cat_painting_text)
        val sculptingLayout : LinearLayout = findViewById(R.id.cat_sculpting)
        val sculptingText : TextView = findViewById(R.id.cat_sculpting_text)

        val paintingClickListener = View.OnClickListener {
            navigateToCategoryItemsPage("Painting")
        }

        val sculptingClickListener = View.OnClickListener {
            navigateToCategoryItemsPage("Sculpting")
        }

        paintingLayout.setOnClickListener(paintingClickListener)
        paintingText.setOnClickListener(paintingClickListener)
        sculptingLayout.setOnClickListener(sculptingClickListener)
        sculptingText.setOnClickListener(sculptingClickListener)

    }

    private fun navigateToCategoryItemsPage(categoryName: String) {
        val intent = Intent(this, CategoryItems::class.java)
        intent.putExtra("category_name", categoryName)
        startActivity(intent)
    }

}