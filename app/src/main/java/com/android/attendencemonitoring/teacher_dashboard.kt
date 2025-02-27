package com.android.attendencemonitoring

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class teacher_dashboard : AppCompatActivity() { // Fixed class name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_dashboard)

        val mainLayout = findViewById<View>(R.id.main) // Ensure R.id.main exists

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets



        }
        val studentListButton = findViewById<TextView>(R.id.Student_List)
        studentListButton.setOnClickListener {
            val intent = Intent(this, teacher_mainActivity::class.java)
            startActivity(intent)
        }


    }
}
