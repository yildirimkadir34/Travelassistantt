package com.example.travelassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.travelassistant.databinding.ActivityEntertodolistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseStorage
private lateinit var dbRef: DatabaseReference
private lateinit var firebaseFirestore: FirebaseFirestore
private lateinit var binding: ActivityEntertodolistBinding

class Entertodolist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntertodolistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        db= FirebaseStorage.getInstance()
        auth= Firebase.auth
        firebaseFirestore= FirebaseFirestore.getInstance()

    }
    fun backtodo(view: View){
        val intent= Intent(this@Entertodolist, ToDoList::class.java)
        startActivity(intent)
        finish()
    }
    fun sendtask(view: View){
        val user= Firebase.auth.currentUser
        val uid=user?.uid
        val tasktext=binding.createTitle.text.toString()
        val senttask=binding.senttask.text.toString()
        val star = binding.seekBar.progress.toString()
        var Sira=1
        dbRef= FirebaseDatabase.getInstance().getReference("users")
        if (uid != null) {
            dbRef.child(uid).get().addOnSuccessListener {
                if (it.child("Adresler").child("tasks").exists()){

                    while (Sira<50){
                        var sira = it.child("Adresler").child("tasks").child("$Sira").child("sira").value
                        print("sira ="+ sira)

                        var siraa = sira.toString()

                        var siraaa=Sira.toString()

                        if (siraa != siraaa){

                            var coordinate = Taskinfo(tasktext,senttask,star,Sira)
                            dbRef= FirebaseDatabase.getInstance().getReference("users")
                            if (uid != null) {
                                dbRef.child(uid).get().addOnSuccessListener {
                                    dbRef.child(uid).child("Adresler").child("tasks").child("$Sira").setValue(coordinate)
                                    val intent = Intent (this@Entertodolist,ToDoList::class.java)
                                    startActivity(intent)
                                    finish()

                                }.addOnFailureListener {
                                    Toast.makeText(this@Entertodolist,"Failed", Toast.LENGTH_LONG).show()
                                }
                            }
                            break
                        }else{
                            Sira++
                            if (Sira==51){
                                Toast.makeText(this@Entertodolist,"You can't save more than 50 task", Toast.LENGTH_LONG).show()
                            }
                        }

                    }

                }else{
                    try {
                        var coordinate = Taskinfo(tasktext,senttask,star,Sira)
                        dbRef= FirebaseDatabase.getInstance().getReference("users")
                        if (uid != null) {
                            dbRef.child(uid).get().addOnSuccessListener {
                                dbRef.child(uid).child("Adresler").child("tasks").child("$Sira").setValue(coordinate)
                                val intent = Intent (this@Entertodolist,ToDoList::class.java)
                                Toast.makeText(this@Entertodolist,"Congratulations. Your first task has been added to your tasks", Toast.LENGTH_LONG).show()
                                startActivity(intent)
                                finish()

                            }.addOnFailureListener {
                                Toast.makeText(this@Entertodolist,"Failed", Toast.LENGTH_LONG).show()
                            }
                        }

                    }catch (e : Exception){
                        println(e.message)
                    }


                }

            }.addOnFailureListener {
                Toast.makeText(this@Entertodolist,"Failed", Toast.LENGTH_LONG).show()
            }

        }

    }


}