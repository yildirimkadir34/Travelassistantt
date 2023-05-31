package com.example.travelassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.travelassistant.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var db: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        db = FirebaseStorage.getInstance()
        dbRef= Firebase.database.reference
        val user= Firebase.auth.currentUser
        val uid=user?.uid







        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        binding.btnRegister.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val pass = binding.inputPassword.text.toString()
            val confirmPass = binding.inputConformPassword.text.toString()
            val username=binding.inputUsername.text.toString()
            val name=binding.name.text.toString()
            val srname=binding.srname.text.toString()

            //val username= binding.username.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty() && srname.isNotEmpty() && username.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val user= Firebase.auth.currentUser
                            val uid=user?.uid

                            var userinformation = Userinformation(username,name,srname)
                            dbRef= FirebaseDatabase.getInstance().getReference("users")
                            if (uid != null) {
                                dbRef.child(uid).get().addOnSuccessListener {
                                    dbRef.child(uid).child("Userinformation").setValue(userinformation)}
                                    .addOnFailureListener {
                                        Toast.makeText(this@Register,"Failed", Toast.LENGTH_LONG).show()
                                    }}





                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }


}