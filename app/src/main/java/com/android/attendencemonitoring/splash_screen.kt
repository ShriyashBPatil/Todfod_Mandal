package com.android.attendencemonitoring

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // ✅ Delay for 3 seconds, then start MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // ✅ Close splash screen to prevent back button return
        }, 5000) // 3000ms = 3 seconds
    }
}
