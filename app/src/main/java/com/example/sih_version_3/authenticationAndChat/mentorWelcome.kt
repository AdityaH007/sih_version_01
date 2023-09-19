package com.example.sih_version_3.authenticationAndChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sih_version_3.R

class mentorWelcome : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_welcome)

        button = findViewById(R.id.btnChatWithStudent)
        button.setOnClickListener {
            startActivity(Intent(this@mentorWelcome,studentActivity::class.java))

        }

    }
}