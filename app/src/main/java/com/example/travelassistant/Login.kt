package com.example.travelassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.travelassistant.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

private lateinit var auth: FirebaseAuth
private lateinit var binding : ActivityLoginBinding

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        supportActionBar?.hide()

        val currentUser=auth.currentUser

        if (currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.textViewSignUp.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this,Password::class.java)
            startActivity(intent)
            finish()
        }

    }



    fun signinclick(view: View){

        val email = binding.inputEmail.text.toString()
        val password= binding.inputPassword.text.toString()


        if(email.equals("") || password.equals("") ) {
            Toast.makeText(this, "Enter email and password!", Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent= Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

}