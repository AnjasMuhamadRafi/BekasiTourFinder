package com.example.BekasiTourFinderAPP

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*

class TerdekatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLocation: Location? = null
    private lateinit var adapter: TerdekatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terdekat)

        recyclerView = findViewById(R.id.recyclerViewTerdekat)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TerdekatAdapter(this, listOf())
        recyclerView.adapter = adapter

        // Inisialisasi referensi Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("payload")

        fetchUserLocation()
    }

    private fun fetchUserLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                userLocation = location
                Log.d("TerdekatActivity", "User location: (${location.latitude}, ${location.longitude})")
                fetchDataFromFirebase()
            } else {
                Log.e("TerdekatActivity", "Failed to get user location")
            }
        }
    }

    private fun fetchDataFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val destinationList = dataSnapshot.children.mapNotNull { it.getValue(DataClass::class.java) }
                if (destinationList.isNotEmpty() && userLocation != null) {
                    val destinations = destinationList.mapNotNull { destination ->
                        if (destination.latitude != null && destination.longitude != null) {
                            Log.d("TerdekatActivity", "Destination: ${destination.nama_wisata}, longitude: ${destination.longitude}, latitude: ${destination.latitude}")

                            val distance = haversine(
                                userLocation!!.latitude,
                                userLocation!!.longitude,
                                destination.latitude!!,
                                destination.longitude!!
                            )
                            Log.d("TerdekatActivity", "Destination: ${destination.nama_wisata}, Distance: $distance km")

                            destination.apply {
                                this.distance = distance
                            }
                        } else {
                            null
                        }
                    }.sortedBy { it.distance }
                    adapter.updateDataList(destinations)
                } else {
                    Log.e("TerdekatActivity", "No valid destinations found or user location is null")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("TerdekatActivity", "Firebase error: ${databaseError.message}")
            }
        })
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        // Radius bumi dalam kilometer
        val R = 6372.8

        // Mengonversi perbedaan koordinat latitude dan longitude ke radian
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        // Menghitung sudut antara dua titik
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)

        // Menghitung sudut di atas dalam bentuk sudut sentral sudut
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        // Menghitung jarak menggunakan rumus haversine
        val distance = R * c

        // Log jarak yang dihitung
        Log.d("TerdekatActivity", "Distance calculated using Haversine formula: $distance km")

        return distance
    }

}
