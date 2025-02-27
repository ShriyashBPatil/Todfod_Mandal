package com.android.attendencemonitoring

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class teacher_login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views

        val loginEmail = findViewById<EditText>(R.id.teach_login_email)
        val loginPass = findViewById<EditText>(R.id.teach_login_pass)
        val loginBtn = findViewById<TextView>(R.id.tech_log_btn)

        loginBtn.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val password = loginPass.text.toString().trim()

            if (email.isEmpty()) {
                loginEmail.error = "Enter email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                loginPass.error = "Enter password"
                return@setOnClickListener
            }

            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, teacher_dashboard::class.java) // Change to your dashboard activity
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
