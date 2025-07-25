package com.example.neednet

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.neednet.databinding.ActivityOfferHelpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class OfferHelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOfferHelpBinding

    companion object {
        private const val LOCATION_PICKER_REQUEST = 101
        private const val LOCATION_PERMISSION_REQUEST = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Open LocationPickerActivity when location field is clicked
        binding.editLocation.setOnClickListener {
            val intent = Intent(this, LocationPickerActivity::class.java)
            startActivityForResult(intent, LOCATION_PICKER_REQUEST)
        }

        // Help type spinner setup
        val helpTypes = listOf("Borrow", "Sell", "Donate", "Rent", "Volunteer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, helpTypes)
        binding.spinnerHelpType.adapter = adapter

        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()


        // Submit button logic
        binding.btnSubmitOffer.setOnClickListener {
            val locationText = binding.editLocation.text.toString().trim()
            val message = binding.editMessage.text.toString().trim()
            val helpType = binding.spinnerHelpType.selectedItem.toString()
            val offerTitle = binding.editOfferTitle.text.toString().trim()
            val userId = auth.currentUser?.uid ?: ""
            val coordinates = binding.editLocation.tag as? Pair<*, *>
            val latitude = coordinates?.first as? Double ?: 0.0
            val longitude = coordinates?.second as? Double ?: 0.0

            if (locationText.isNotEmpty() && message.isNotEmpty() && offerTitle.isNotEmpty()) {
                val offerData = hashMapOf(
                    "userId" to userId,
                    "offerTitle" to offerTitle,
                    "helpType" to helpType,
                    "message" to message,
                    "location" to locationText,
                    "latitude" to latitude,
                    "longitude" to longitude,
                    "timestamp" to System.currentTimeMillis()
                )

                db.collection("help_offers")
                    .add(offerData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Offer posted successfully!", Toast.LENGTH_SHORT).show()
                        finish() // or navigate to Home/Listing screen
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to post: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_PICKER_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedAddress = data.getStringExtra("selected_address")
            val latitude = data.getDoubleExtra("latitude", 0.0)
            val longitude = data.getDoubleExtra("longitude", 0.0)

            if (!selectedAddress.isNullOrEmpty()) {
                binding.editLocation.setText(selectedAddress)

                // Save coordinates as tags or to a variable to include in Firestore
                binding.editLocation.tag = Pair(latitude, longitude)
            }
        }
    }

}
