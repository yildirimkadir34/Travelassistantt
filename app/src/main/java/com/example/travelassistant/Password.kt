package com.example.travelassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.example.travelassistant.databinding.ActivityPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

private lateinit var binding: ActivityPasswordBinding
private lateinit var auth: FirebaseAuth
private lateinit var dbRef: DatabaseReference
private lateinit var db: FirebaseStorage


class Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        db = FirebaseStorage.getInstance()
        dbRef= Firebase.database.reference
        var emailAddress : String = ""



        binding.resetpass.setOnClickListener {
            emailAddress= binding.resetmail.text.toString()
            if (emailAddress != ""){
                auth.sendPasswordResetEmail(emailAddress)
                    .addOnSuccessListener {
                        Toast.makeText(this,"Check Your E-Mail", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@Password, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,"This e-mail is not registered in the system", Toast.LENGTH_LONG).show()
                    }
            }else{
                Toast.makeText(this,"Please Enter E-Mail", Toast.LENGTH_LONG).show()
            }



        }


    }


}