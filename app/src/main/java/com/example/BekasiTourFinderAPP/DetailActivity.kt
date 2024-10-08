package com.example.BekasiTourFinderAPP

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.BekasiTourFinderAPP.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        if (bundle != null) {
            binding.textDescription.text = bundle.getString("Description")
            binding.textName.text = bundle.getString("Title")
            binding.textLocation.text = bundle.getString("Priority")
            binding.textOperationalHours.text = bundle.getString("Time")
            binding.textPrice.text = bundle.getString("Price")
            binding.textPhone.text = bundle.getString("Telepone")
            imageUrl = bundle.getString("Image") ?: ""
            Glide.with(this).load(imageUrl).into(binding.imageDetail)

            val latitude = bundle.getString("Lattitude")
            val longitude = bundle.getString("Longtitude")
            val locationName = bundle.getString("Title")

            binding.buttonOpenMap.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($locationName)")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }
}
