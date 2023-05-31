package com.example.travelassistant

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.travelassistant.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityMainBinding
private lateinit var auth : FirebaseAuth
private lateinit var dbRef: DatabaseReference
private lateinit var locationManager: LocationManager
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()
        var namearea=binding.loginname
        val user= Firebase.auth.currentUser
        val uid=user?.uid

        dbRef= FirebaseDatabase.getInstance().getReference("users")
        if (uid != null) {
            dbRef.child(uid).get().addOnSuccessListener {
                if(it.child("Userinformation").exists()) {
                    val isim = it.child("Userinformation").child("name").value as String
                    val soyisim = it.child("Userinformation").child("surname").value as String
                    namearea.text="Welcome, $isim  $soyisim"



                }

            }
        }

        locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1 )

        }else{
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //GPS ON
            }
            else{
                //GPS OFF
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setMessage("GPS is disabled.GPS must be turned on to use the app. Would you turn on GPS?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }).setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)

                    })

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()


            }


        }

    }






    fun gobook(view: View){
        val intent = Intent(this@MainActivity,ListtActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun gomap(view: View){
        val intent = Intent(this@MainActivity,MapsActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun golist(view: View){
        val intent = Intent(this@MainActivity,ToDoList::class.java)
        startActivity(intent)
        finish()
    }
    fun logout(view: View){
        auth.signOut()
        val intent = Intent(this@MainActivity, Login::class.java)
        startActivity(intent)
        finish()
    }
}