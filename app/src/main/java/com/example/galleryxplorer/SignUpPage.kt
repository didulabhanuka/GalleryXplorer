package com.example.galleryxplorer

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        auth = FirebaseAuth.getInstance()

        val fullName : EditText = findViewById(R.id.et_fullname)
        val email : EditText = findViewById(R.id.et_email)
        val contactNumber : EditText = findViewById(R.id.et_contact)
        val password : EditText = findViewById(R.id.et_password)
        val confirmPassword : EditText = findViewById(R.id.et_confirm_password)
        val signUpBtn : AppCompatButton = findViewById(R.id.btn_register)
        val passwordLayout : TextInputLayout = findViewById(R.id.til_password)
        val confirmPasswordLayout : TextInputLayout = findViewById(R.id.til_confirm_password)

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
                        Log.d(ContentValues.TAG, "createuserWithEmail:success")
                        val user = auth.currentUser

                    }else{
                        Log.w(ContentValues.TAG, "createUserWithEmail:failure", it.exception)
                    }
                }
            }

        }

    }
}