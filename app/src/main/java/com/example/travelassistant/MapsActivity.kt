package com.example.travelassistant

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.travelassistant.databinding.ActivityMapsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseStorage
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var dbRef: DatabaseReference
    //private lateinit var user:FirebaseUser
    //private lateinit var reference:DatabaseReference
    val user= Firebase.auth.currentUser
    //private lateinit var home:LatLng
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db= FirebaseStorage.getInstance()
        auth=Firebase.auth
        val uid=user?.uid
        dbRef=Firebase.database.reference
        firebaseFirestore=FirebaseFirestore.getInstance()
        supportActionBar?.hide()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapp) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(dinleyici)
//latitude -> enlem
        //longitude -> boylam
        //41.129171, 29.031881



        //mMap.addMarker(MarkerOptions().position(home).title("Marker in home"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home,15f))
        //casting -> as
        locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener{
            override fun onLocationChanged(location: Location) {
                //lokasyon, konum değişince yapılacak işlemler

                mMap.clear()
                var home = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(home).title("Marker in home"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home,15f))
                val geocoder= Geocoder(this@MapsActivity, Locale.getDefault())

                try {
                    val adreslistesi=geocoder.getFromLocation(location.latitude,location.longitude,1)
                    if (adreslistesi != null){
                        println(adreslistesi.get(0).toString())

                    }
                }catch (e: Exception){

                }
            }

        }
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1 )

        }else{
            //izin zaten verilmiş

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val sonbilinenkonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (sonbilinenkonum != null){
                val sonbilinenlatlng= LatLng(sonbilinenkonum.latitude,sonbilinenkonum.longitude)
                mMap.addMarker(MarkerOptions().position(sonbilinenlatlng).title("son bilinen konum"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonbilinenlatlng,15f))


            }
        }




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1){
            if (grantResults.size> 0){
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    // İZİN VERİLDİ
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)

                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    val dinleyici = object : GoogleMap.OnMapLongClickListener {
        override fun onMapLongClick(p0: LatLng) {
            mMap.clear()
            val geocoder= Geocoder (this@MapsActivity, Locale.getDefault())
            if ( p0 != null){
                var adres = ""
                try {
                    val adreslistesi=geocoder.getFromLocation(p0.latitude,p0.longitude,1)
                    if (adreslistesi != null){
                        if (adreslistesi.get(0).thoroughfare != null){
                            adres += adreslistesi.get(0).thoroughfare
                            if (adreslistesi.get(0). subThoroughfare != null){
                                adres+= adreslistesi.get(0).subThoroughfare
                            }

                        }
                    }

                }catch (e : Exception){
                    e.printStackTrace()
                }
                mMap.addMarker(MarkerOptions().position(p0).title(adres))
            }
        }
    }





    fun golocation(view: View){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1 )

        }else{
            //izin zaten verilmiş

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val sonbilinenkonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (sonbilinenkonum != null){
                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

                val showadres= geocoder.getFromLocation(sonbilinenkonum.latitude,sonbilinenkonum.longitude,1)


                val sonbilinenlatlng= LatLng(sonbilinenkonum.latitude,sonbilinenkonum.longitude)
                mMap.addMarker(MarkerOptions().position(sonbilinenlatlng).title("Marker in home"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sonbilinenlatlng,15f))
            }}

    }
    fun gohome(view: View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()}
    /*fun enteradres(view: View){
        val locationSearch= binding.enteradres
        var location: String
        location = locationSearch.text.toString().trim()
        var addressList: List<Address>? = null

        if (location == null || location == ""){
            Toast.makeText(this, "provide location", Toast.LENGTH_SHORT).show()
        }else{
            val geoCoder = Geocoder(this)
            try {
                addressList = geoCoder.getFromLocationName(location, 1)
            }catch (e: IOException){
                e.printStackTrace()
            }

            val address = addressList!![0]
            val latLng = LatLng(address.latitude, address.longitude)
            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }*/

    fun savelocation(view: View){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {
            //izin verilmemiş
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1 )

        }else{
            //izin zaten verilmiş

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)
            val sonbilinenkonum = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (sonbilinenkonum != null){
                val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

                val showadres= geocoder.getFromLocation(sonbilinenkonum.latitude,sonbilinenkonum.longitude,1)


                val sonbilinenlatlng= LatLng(sonbilinenkonum.latitude,sonbilinenkonum.longitude)
                var Sira=1

                val not=binding.editTextText2.text.toString()
                val uid=user?.uid


                dbRef= FirebaseDatabase.getInstance().getReference("users")
                println(dbRef)
                if (uid != null) {

                    dbRef.child(uid).get().addOnSuccessListener {

                        if (it.child("Adresler").child("Coordinate").exists()){

                            println("exist" +it)
                            while (Sira<50){
                                var sira = it.child("Adresler").child("Coordinate").child("$Sira").child("sira").value

                                var siraa = sira.toString()

                                var siraaa=Sira.toString()


                                if (siraa != siraaa){

                                    var coordinate = com.example.travelassistant.Location(Sira, not, sonbilinenlatlng.latitude, sonbilinenlatlng.longitude)
                                    dbRef= FirebaseDatabase.getInstance().getReference("users")
                                    if (uid != null) {
                                        dbRef.child(uid).get().addOnSuccessListener {
                                            dbRef.child(uid).child("Adresler").child("Coordinate").child("$Sira").setValue(coordinate)
                                            val intent = Intent (this@MapsActivity, ListtActivity::class.java)
                                            startActivity(intent)
                                            finish()



                                        }.addOnFailureListener {
                                            Toast.makeText(this@MapsActivity,"Failed", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    break
                                }else{
                                    println("Else Çalıştı")
                                    Sira++
                                    if (Sira==51){
                                        Toast.makeText(this@MapsActivity,"You can't save more than 50 adress", Toast.LENGTH_LONG).show()
                                    }
                                }

                            }

                        }else{
                            try {
                                var coordinate = com.example.travelassistant.Location(Sira, not, sonbilinenlatlng.latitude, sonbilinenlatlng.longitude)
                                dbRef= FirebaseDatabase.getInstance().getReference("users")
                                if (uid != null) {
                                    dbRef.child(uid).get().addOnSuccessListener {
                                        dbRef.child(uid).child("Adresler").child("Coordinate").child("$Sira").setValue(coordinate)
                                        val intent = Intent (this@MapsActivity,ListtActivity::class.java)
                                        Toast.makeText(this@MapsActivity,"Congratulations. Your first location has been added to your list", Toast.LENGTH_LONG).show()
                                        startActivity(intent)



                                    }.addOnFailureListener {
                                        Toast.makeText(this@MapsActivity,"Failed", Toast.LENGTH_LONG).show()
                                    }
                                }

                            }catch (e : Exception){
                                println(e.message)
                            }


                        }

                    }.addOnFailureListener {
                        Toast.makeText(this@MapsActivity,"Failed", Toast.LENGTH_LONG).show()
                    }

                }

            }

        }



    }


}