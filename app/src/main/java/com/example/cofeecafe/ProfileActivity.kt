package com.example.cofeecafe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    private lateinit var profileImg: ImageView
    private lateinit var saveButton: Button
    private lateinit var editButton: ImageView
    private lateinit var nameField: EditText
    private lateinit var mobileField: EditText
    private lateinit var emailField: EditText
    private lateinit var addressField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseAuth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        profileImg = findViewById(R.id.profile_img)
        saveButton = findViewById(R.id.profilesavebtn)
        editButton = findViewById(R.id.editprofile)
        nameField = findViewById(R.id.profilename)
        mobileField = findViewById(R.id.profilemobieno)
        emailField = findViewById(R.id.profileemail)
        addressField = findViewById(R.id.profileaddres)

        enableEditing(false)
        loadUserData()

        profileImg.setOnClickListener {
            openImagePicker()
        }

        saveButton.setOnClickListener {
            saveProfileInfo()
        }

        editButton.setOnClickListener {
            enableEditing(true)
        }
    }

    private fun loadUserData() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        database.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    val mobile = snapshot.child("mobile").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)
                    val address = snapshot.child("address").getValue(String::class.java)
                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                    nameField.setText(name)
                    mobileField.setText(mobile)
                    emailField.setText(email)
                    addressField.setText(address)

                    profileImageUrl?.let {
                        Glide.with(this@ProfileActivity)
                            .load(it)
                            .into(profileImg)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Error loading user data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            profileImg.setImageURI(selectedImageUri)
        }
    }

    private fun enableEditing(enable: Boolean) {
        nameField.isEnabled = enable
        mobileField.isEnabled = enable
        emailField.isEnabled = enable
        addressField.isEnabled = enable
        saveButton.isEnabled = enable
        profileImg.isEnabled = enable

        saveButton.alpha = if (enable) 1f else 0.5f
    }

    private fun saveProfileInfo() {
        val name = nameField.text.toString().trim()
        val mobile = mobileField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val address = addressField.text.toString().trim()

        if (name.isEmpty() || mobile.isEmpty() || email.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = firebaseAuth.currentUser?.uid ?: return

        val userMap = mapOf(
            "name" to name,
            "mobile" to mobile,
            "email" to email,
            "address" to address
        )

        database.child(userId).setValue(userMap).addOnSuccessListener {
            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show()
            enableEditing(false)

            selectedImageUri?.let {
                uploadProfileImage(userId, it)
            }

        }.addOnFailureListener {
            Toast.makeText(this, "Error saving profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadProfileImage(userId: String, uri: Uri) {
        val storageRef = storage.reference.child("profileImages/$userId.jpg")

        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                database.child(userId).child("profileImageUrl").setValue(downloadUrl.toString())
                    .addOnSuccessListener {
                        Toast.makeText(this, "Profile image uploaded", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error saving profile image URL", Toast.LENGTH_SHORT).show()
                    }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
