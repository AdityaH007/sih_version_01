package com.example.sih_version_3.studentActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sih_version_3.R
import com.example.sih_version_3.databinding.ActivityCommunicationHomeBinding

class communicationHome : AppCompatActivity() {
    private lateinit var binding: ActivityCommunicationHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCommunicationHomeBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

     binding.speechRecognition.setOnClickListener {
         startActivity(Intent(this,improveYourSpeech::class.java))
     }

        binding.mentorEnglish.setOnClickListener {
            startActivity(Intent(this,communicationMentorList::class.java))


        }

        binding.quizTaker.setOnClickListener {
            startActivity(Intent(this,engllishQuiz::class.java))
        }


    }
}