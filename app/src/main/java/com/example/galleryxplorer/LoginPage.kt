package com.example.galleryxplorer

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

class LoginPage : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        auth = FirebaseAuth.getInstance()

        val loginEmail : EditText = findViewById(R.id.et_login_email)
        val loginPassword : EditText = findViewById(R.id.et_login_password)
        val loginPasswordLayout : TextInputLayout = findViewById(R.id.til_login_password)
        val btnLogin : AppCompatButton = findViewById(R.id.btn_login)
        val signUpText : TextView = findViewById(R.id.tv_havent_account_register)

        btnLogin.setOnClickListener {
            loginPasswordLayout.isPasswordVisibilityToggleEnabled = true
            val loginMail = loginEmail.text.toString()
            val loginPwd = loginPassword.text.toString()

            if(loginMail.isEmpty() || loginPwd.isEmpty()){
                if(loginMail.isEmpty()){
                    loginEmail.error = "Enter your email address"
                }
                if (loginPwd.isEmpty()){
                    loginPassword.error = "Enter your password"
                    loginPasswordLayout.isPasswordVisibilityToggleEnabled = false
                }
                Toast.makeText(this,"Enter valid details", Toast.LENGTH_SHORT).show()

            }else{
                auth.signInWithEmailAndPassword(loginMail, loginPwd).addOnCompleteListener {
                    if(it.isSuccessful){
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                        Toast.makeText(this, "Authentication Success!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, AddItem::class.java)
                        startActivity(intent)

                    }else{
                        Log.w(ContentValues.TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java)
            startActivity(intent)
            finish()
        }

    }
}