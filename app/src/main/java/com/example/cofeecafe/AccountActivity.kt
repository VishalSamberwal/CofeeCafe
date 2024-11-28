package com.example.cofeecafe

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        firebaseAuth = FirebaseAuth.getInstance()

        val logoutbtn = findViewById<TextView>(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            logoutUser()
        }

        val profilebtn = findViewById<TextView>(R.id.accprofile)
        profilebtn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val aboutbtn = findViewById<TextView>(R.id.accinfo)
        aboutbtn.setOnClickListener {
            val intent = Intent(this, AboutAppActivity::class.java)
            startActivity(intent)
        }

        val trackorderbtn = findViewById<TextView>(R.id.accorder)
        trackorderbtn.setOnClickListener {
            Toast.makeText(this, "Sorry, this feature is not available right now", Toast.LENGTH_SHORT).show()
        }
    }

    private fun logoutUser() {
        firebaseAuth.signOut()
        Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LogInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
