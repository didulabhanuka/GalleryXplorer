package com.example.galleryxplorer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.galleryxplorer.databinding.ActivityHelloUserBinding

class HelloUser : AppCompatActivity() {

    private lateinit var binding: ActivityHelloUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelloUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent())
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent())
        }
    }
}