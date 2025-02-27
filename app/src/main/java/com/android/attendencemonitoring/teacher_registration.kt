package com.android.attendencemonitoring

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class teacher_registration : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_registration)

        auth = FirebaseAuth.getInstance()

        // UI Elements
        val techName = findViewById<EditText>(R.id.tech_name)
        val techId = findViewById<EditText>(R.id.teach_id)
        val techEmail = findViewById<EditText>(R.id.teach_email)
        val techPass = findViewById<EditText>(R.id.teach_pass)
        val techPassConf = findViewById<EditText>(R.id.teach_Cpass)
        val registerBtn = findViewById<TextView>(R.id.Registerbtn)


        registerBtn.setOnClickListener {
            val name = techName.text.toString().trim()
            val id = techId.text.toString().trim()
            val email = techEmail.text.toString().trim()
            val password = techPass.text.toString().trim()
            val confirmPassword = techPassConf.text.toString().trim()

            if (name.isEmpty() || id.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerTeacher(name, id, email, password)
        }
    }

    private fun registerTeacher(name: String, id: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val teacher = Teacher(name, id, email)

                    firestore.collection("Teachers").document(userId).set(teacher)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, Login::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Database Error: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}

data class Teacher(val name: String, val id: String, val email: String)
