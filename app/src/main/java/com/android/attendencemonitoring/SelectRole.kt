package com.android.attendencemonitoring

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SelectRole : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select_role)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val student = findViewById<TextView>(R.id.student)
        val teacher= findViewById<TextView>(R.id.teacher)

        student.setOnClickListener {
            val intent = Intent(this, student_registration::class.java)
            startActivity(intent)
        }

        teacher.setOnClickListener {
            val intent = Intent(this, teacher_registration::class.java)
            startActivity(intent)
        }

    }


}