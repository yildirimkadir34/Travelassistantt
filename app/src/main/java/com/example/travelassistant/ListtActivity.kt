package com.example.travelassistant

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelassistant.databinding.ActivityListtBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityListtBinding
private lateinit var postArrayList : ArrayList<Location>
private lateinit var recyclerView: RecyclerView
private lateinit var dbRef: DatabaseReference
private lateinit var feedAdapter: Adapter
private lateinit var auth : FirebaseAuth






class ListtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListtBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        recyclerView= binding.recyclerView
        auth= FirebaseAuth.getInstance()
        recyclerView.layoutManager = LinearLayoutManager(this)
        supportActionBar?.hide()
        postArrayList= arrayListOf<Location>()
        getLocationData()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter= Adapter(postArrayList)
        binding.recyclerView.adapter=feedAdapter


    }
    fun gohome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()}
    fun deletealladress(view: View){
        val user= Firebase.auth.currentUser
        val uid=user?.uid
        dbRef= FirebaseDatabase.getInstance().getReference("users")
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to delete all addresses?")
            .setCancelable(true)
            .setPositiveButton("Yes", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                if (uid != null){
                    dbRef.child(uid).child("Adresler").child("Coordinate").removeValue().addOnSuccessListener {
                        Toast.makeText(this@ListtActivity,"You has been deleted all addresses!",
                            Toast.LENGTH_LONG).show()
                        val intent = Intent(this@ListtActivity, ListtActivity::class.java)
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


        var i=1
        val user= Firebase.auth.currentUser
        val uid=user?.uid

        dbRef= FirebaseDatabase.getInstance().getReference("users")
        if (uid != null) {
            dbRef.child(uid).get().addOnSuccessListener {
                if (it.child("Adresler").child("Coordinate").exists()){


                    while (i<50){
                        if (it.child("Adresler").child("Coordinate").child("$i").exists()){

                            val not = it.child("Adresler").child("Coordinate").child("$i").child("not").value as String

                            val latitude = it.child("Adresler").child("Coordinate").child("$i").child("latitude").value

                            val longitude = it.child("Adresler").child("Coordinate").child("$i").child("longitude").value

                            val sira = it.child("Adresler").child("Coordinate").child("$i").child("sira").value

                            val post = Location(sira,not,latitude,longitude)

                            postArrayList.add(post)
                            i++

                        }else{
                            break
                        }

                    }
                    var adapter = Adapter(postArrayList)
                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object : Adapter.onItemClickListener{
                        override fun onItemclick(aa: CharSequence) {
                            //Toast.makeText(this@ListtActivity,"You Clicked on item no. $aa", Toast.LENGTH_LONG).show()
                            val navi =("$aa")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$navi&mode=d"))
                            //This is directly start navigation
                            intent.setPackage("com.google.android.apps.maps")
                            startActivity(intent)
                            finish()
                        }


                    })





                }

                feedAdapter.notifyDataSetChanged()



            }.addOnFailureListener {
                Toast.makeText(this@ListtActivity,"Failed", Toast.LENGTH_LONG).show()
            }
        }






    }
    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.exitmenu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            auth.signOut()
            val intent = Intent(this@ListtActivity, loginact::class.java)
            startActivity(intent)
            finish()

        }else if (item.itemId == R.id.Book){
            val intent = Intent(this@ListtActivity,ListtActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if (item.itemId == R.id.Map){
            val intent = Intent(this@ListtActivity,MapsActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if (item.itemId == R.id.todolistt){
            val intent = Intent(this@ListtActivity,ToDoList::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)

    }*/

}