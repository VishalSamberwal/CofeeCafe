package com.example.cofeecafe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Locale

class CheckOutActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnGetCurrentLocation: Button
    private lateinit var fullNameEditText: EditText
    private lateinit var cityEditText: EditText
    private lateinit var districtEditText: EditText
    private lateinit var landmarkEditText: EditText
    private lateinit var postalCodeEditText: EditText
    private lateinit var mobileNumberEditText: EditText
    private lateinit var btnSaveAddress: Button
    private lateinit var btnConfirmOrder: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var databaseReference: DatabaseReference

    private lateinit var selectedPaymentMethod: String

    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_check_out)

        databaseReference = FirebaseDatabase.getInstance().getReference("Orders")

        btnGetCurrentLocation = findViewById(R.id.btn_get_current_location)
        fullNameEditText = findViewById(R.id.full_name)
        cityEditText = findViewById(R.id.city)
        districtEditText = findViewById(R.id.district)
        landmarkEditText = findViewById(R.id.landmark)
        postalCodeEditText = findViewById(R.id.postal_code)
        mobileNumberEditText = findViewById(R.id.mobile_number)
        btnSaveAddress = findViewById(R.id.btn_save_address)
        btnConfirmOrder = findViewById(R.id.btn_confirm_order)
        radioGroup = findViewById(R.id.radio_group)

        val cartItems: List<CartItem>? = intent.getParcelableArrayListExtra("cart_items")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        btnGetCurrentLocation.setOnClickListener {
            getCurrentLocation()
        }

        btnSaveAddress.setOnClickListener {
            saveAddress()
        }

        btnConfirmOrder.setOnClickListener {
            confirmOrder(cartItems)
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    if (location != null) {
                        updateLocationUI(location)
                    } else {
                        Toast.makeText(this@CheckOutActivity, "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
        }
    }

    private fun updateLocationUI(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                cityEditText.setText(address.locality)
                districtEditText.setText(address.subAdminArea)
            } else {
                Toast.makeText(this, "No address found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Geocoder failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAddress() {
        val fullName = fullNameEditText.text.toString()
        val city = cityEditText.text.toString()
        val district = districtEditText.text.toString()
        val landmark = landmarkEditText.text.toString()
        val postalCode = postalCodeEditText.text.toString()
        val mobileNumber = mobileNumberEditText.text.toString()

        if (fullName.isEmpty() || city.isEmpty() || district.isEmpty() || landmark.isEmpty() || postalCode.isEmpty() || mobileNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val address = Address(
            fullName,
            city,
            district,
            landmark,
            postalCode,
            mobileNumber
        )

        databaseReference.push().setValue(address)
            .addOnSuccessListener {
                Toast.makeText(this, "Address saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save address: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun confirmOrder(cartItems: List<CartItem>?) {
        val selectedPaymentMethodId = radioGroup.checkedRadioButtonId
        selectedPaymentMethod = when (selectedPaymentMethodId) {
            R.id.radio_card -> {
                Toast.makeText(this, "We are not accepting payments with cards.", Toast.LENGTH_SHORT).show()
                return
            }
            R.id.radio_upi -> {
                openUpiPayment(cartItems)
                return
            }
            R.id.radio_cod -> "Cash on Delivery"
            else -> {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show()
                return
            }
        }

        saveOrder(cartItems)
    }

    private fun openUpiPayment(cartItems: List<CartItem>?) {
        val paymentApps = arrayOf("Paytm", "Google Pay", "PhonePe")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Payment App")
            .setItems(paymentApps) { _, which ->
                val selectedApp = paymentApps[which]
                val amount = calculateTotalAmount(cartItems)
                val upiId = when (selectedApp) {
                    "Paytm" -> "9728395491@paytm"
                    "Google Pay" -> "webvishal@oksbi"
                    "PhonePe" -> "9728395491@ybl"
                    else -> return@setItems
                }
                initiateUpiPayment(upiId, amount)
            }
        builder.create().show()
    }

    private fun initiateUpiPayment(upiId: String, amount: String) {
        val uri = Uri.parse("upi://pay?pa=$upiId&pn=YourName&mc=YourMerchantCode&tid=YourTransactionId&tt=TransactionTitle&am=$amount&cu=INR&url=YourUrl")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivityForResult(intent, UPI_PAYMENT_REQUEST_CODE)
    }

    private fun calculateTotalAmount(cartItems: List<CartItem>?): String {
        var totalAmount = 0.0

        cartItems?.forEach { item ->
            totalAmount += item.price * item.quantity
        }

        return String.format("%.2f", totalAmount)
    }

    private fun saveOrder(cartItems: List<CartItem>?) {
        val fullName = fullNameEditText.text.toString()
        val city = cityEditText.text.toString()
        val district = districtEditText.text.toString()
        val landmark = landmarkEditText.text.toString()
        val postalCode = postalCodeEditText.text.toString()
        val mobileNumber = mobileNumberEditText.text.toString()

        val order = Order(
            fullName,
            city,
            district,
            landmark,
            postalCode,
            mobileNumber,
            cartItems,
            selectedPaymentMethod
        )

        databaseReference.push().setValue(order)
            .addOnSuccessListener {
                Toast.makeText(this, "Order confirmed successfully", Toast.LENGTH_SHORT).show()
                navigateToHomeScreen()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to confirm order: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val UPI_PAYMENT_REQUEST_CODE = 2
    }
}
