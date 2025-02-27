package com.android.attendencemonitoring

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class student_registration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.student_registartion)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.studentemail)
        val password = findViewById<EditText>(R.id.std_reg_pass)
        val confirmPassword = findViewById<EditText>(R.id.std_reg_pass_conf)
        val name = findViewById<EditText>(R.id.std_reg_email)
        val rollno = findViewById<EditText>(R.id.stdRollno)
        val stdclass = findViewById<EditText>(R.id.stdclass)
        val registerBtn = findViewById<TextView>(R.id.std_reg)

        // Register Button Click Listener
        registerBtn.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()
            val userConfirmPassword = confirmPassword.text.toString().trim()
            val userName = name.text.toString().trim()
            val userRollNo = rollno.text.toString().trim()
            val userClass = stdclass.text.toString().trim()

            // Validate Inputs
            if (userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty() ||
                userName.isEmpty() || userRollNo.isEmpty() || userClass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (userPassword != userConfirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(userEmail, userPassword, userName, userRollNo, userClass)
        }
    }

    // Register User and Save Data to Firestore
    private fun registerUser(email: String, password: String, name: String, rollNo: String, stdClass: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        Toast.makeText(this, "User ID is null", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }

                    val student = hashMapOf(
                        "name" to name,
                        "rollNo" to rollNo,
                        "class" to stdClass,
                        "email" to email
                    )

                    // Store in Firestore instead of Realtime Database
                    firestore.collection("Students").document(userId)
                        .set(student)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Login::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Firestore Error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
