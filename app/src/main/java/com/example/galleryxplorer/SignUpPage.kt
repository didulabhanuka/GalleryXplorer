package com.example.galleryxplorer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var database = Firebase.firestore
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+"


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        auth = FirebaseAuth.getInstance()

        val fullName : EditText = findViewById(R.id.et_item_name)
        val email : EditText = findViewById(R.id.et_item_price)
        val contactNumber : EditText = findViewById(R.id.et_item_quantity)
        val password : EditText = findViewById(R.id.et_item_description)
        val confirmPassword : EditText = findViewById(R.id.et_signIn_confirm_password)
        val signUpBtn : AppCompatButton = findViewById(R.id.btn_register)
        val passwordLayout : TextInputLayout = findViewById(R.id.til_item_description)
        val confirmPasswordLayout : TextInputLayout = findViewById(R.id.til_signIn_confirm_password)
        val loginText : TextView = findViewById(R.id.tv_have_account_login)

        signUpBtn.setOnClickListener {
            val name = fullName.text.toString()
            val mail = email.text.toString()
            val contact = contactNumber.text.toString()
            val pwd = password.text.toString()
            val confirmPwd = confirmPassword.text.toString()

            passwordLayout.isPasswordVisibilityToggleEnabled = true
            confirmPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if (name.isEmpty() || mail.isEmpty() || contact.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()){
                if (name.isEmpty()){
                    fullName.error = "Enter your name"
                }
                if (mail.isEmpty()){
                    email.error = "Enter your email address"
                }
                if (contact.isEmpty()){
                    contactNumber.error = "Enter your username"
                }
                if (pwd.isEmpty()){
                    passwordLayout.isPasswordVisibilityToggleEnabled = false
                    password.error = "Enter your password"
                }
                if (confirmPwd.isEmpty()){
                    confirmPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    confirmPassword.error = "Re-enter your password"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()

            }else if (mail.matches(emailPattern.toRegex())){
                email.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()

            }else if(pwd.length < 6){
                password.error = "Password should be more than 6 characters"
                Toast.makeText(this, "Password should be more than 6 characters", Toast.LENGTH_SHORT).show()

            }else if (pwd != confirmPwd){
                confirmPassword.error = "Password not match, try again"
                Toast.makeText(this, "Password not match, try again", Toast.LENGTH_SHORT).show()

            }else{
                auth.createUserWithEmailAndPassword(mail,pwd).addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d(ContentValues.TAG, "createUserWithEmail:success")

                        val intent = Intent(this, LoginPage::class.java)
                        startActivity(intent)

                    }else{
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        loginText.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        }


    }
}