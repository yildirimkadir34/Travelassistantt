package com.example.travelassistant

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelassistant.databinding.ActivityToDoListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityToDoListBinding
private lateinit var dataArrayList : ArrayList<Taskinfo>
private lateinit var recyclerView: RecyclerView
private lateinit var dbRef: DatabaseReference
private lateinit var feedAdapter: Todolistadapter
private lateinit var auth : FirebaseAuth


class ToDoList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityToDoListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val user= Firebase.auth.currentUser
        recyclerView= binding.recyclertodolist
        auth= FirebaseAuth.getInstance()
        recyclerView.layoutManager = LinearLayoutManager(this)

        dataArrayList= arrayListOf<Taskinfo>()
        getLocationData()
        binding.recyclertodolist.layoutManager = LinearLayoutManager(this)
        feedAdapter= Todolistadapter(dataArrayList)
        binding.recyclertodolist.adapter=feedAdapter
    }
    fun gohome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()}

    fun savetask(view: View){
        val intent = Intent(this@ToDoList,Entertodolist::class.java)
        startActivity(intent)
        finish()
    }
    fun deletealltasks(view: View){
        val user= Firebase.auth.currentUser
        val uid=user?.uid
        dbRef= FirebaseDatabase.getInstance().getReference("users")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to delete all tasks?")
            .setCancelable(true)
            .setPositiveButton("Yes", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                if (uid != null){
                    dbRef.child(uid).child("Adresler").child("tasks").removeValue().addOnSuccessListener {
                        Toast.makeText(this@ToDoList,"You has been deleted all tasks!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ToDoList, ToDoList::class.java)
                        startActivity(intent)
                        finish()
                    }}
            }).setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->

                dialogInterface.cancel()
            })

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()



    }
    private fun getLocationData(){

        //recyclerView.visibility= View.GONE
        var i=1
        val user= Firebase.auth.currentUser
        val uid=user?.uid

        dbRef= FirebaseDatabase.getInstance().getReference("users")
        if (uid != null) {
            dbRef.child(uid).get().addOnSuccessListener {
                if (it.child("Adresler").child("tasks").exists()){


                    while (i<50){
                        if (it.child("Adresler").child("tasks").child("$i").exists()){

                            val tasktext = it.child("Adresler").child("tasks").child("$i").child("tasktext").value as String

                            val siraa = it.child("Adresler").child("tasks").child("$i").child("sira").value

                            val important = it.child("Adresler").child("tasks").child("$i").child("important").value as String

                            val time = it.child("Adresler").child("tasks").child("$i").child("time").value

                            val post = Taskinfo(tasktext,time,important,siraa)

                            dataArrayList.add(post)
                            println(i)
                            i++
                        }else{
                            break
                        }
                    }
                    var adapter = Todolistadapter(dataArrayList)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListenerr(object : Todolistadapter.onItemClickListenerr{
                        override fun onItemclickk(aa: CharSequence) {
                            //Toast.makeText(this@ToDoList,"You Clicked on item no. $aa", Toast.LENGTH_LONG).show()
                        }
                    })

                }


            }.addOnFailureListener {
                Toast.makeText(this@ToDoList,"Failed", Toast.LENGTH_LONG).show()
            }
        }

    }

}