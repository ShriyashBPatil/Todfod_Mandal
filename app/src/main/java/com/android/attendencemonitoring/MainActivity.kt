package com.android.attendencemonitoring

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get UI Elements
        val nameTextView = findViewById<TextView>(R.id.dash_name)
        val emailTextView = findViewById<TextView>(R.id.dash_email)
        val rollNoTextView = findViewById<TextView>(R.id.dash_roll)
        val classTextView = findViewById<TextView>(R.id.dash_class)

        // Fetch and display user data
        fetchUserData { userData ->
            if (userData != null) {
                nameTextView.text = "${userData.name}"
                emailTextView.text = "${userData.email}"
                rollNoTextView.text = "${userData.rollNo}"
                classTextView.text = "${userData.Class}"
            } else {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchUserData(callback: (Student?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("Firestore", "User is not logged in")
            callback(null)
            return
        }

        firestore.collection("Students").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userData = document.toObject(Student::class.java)
                    callback(userData)
                } else {
                    Log.e("Firestore", "No such document")
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data: ${exception.message}")
                callback(null)
            }
    }
}

// Data Model
data class Student(
    val name: String = "",
    val email: String = "",
    val rollNo: String = "",
    val Class: String = ""
)
