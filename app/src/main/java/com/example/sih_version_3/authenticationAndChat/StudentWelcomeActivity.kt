package com.example.sih_version_3.authenticationAndChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.sih_version_3.R
import com.example.sih_version_3.studentActivities.learnSkill

class StudentWelcomeActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var button2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_welcome)

        button=findViewById(R.id.btnChatWithMentor)
        button.setOnClickListener {
            startActivity(Intent(this,mentorList::class.java))
        }

        button2 = findViewById(R.id.learnSkill)
        button2.setOnClickListener {
            startActivity(Intent(this,learnSkill::class.java))
        }



    }
}