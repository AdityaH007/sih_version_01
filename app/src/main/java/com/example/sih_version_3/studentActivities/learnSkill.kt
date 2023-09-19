package com.example.sih_version_3.studentActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sih_version_3.R
import com.example.sih_version_3.databinding.ActivityLearnSkillBinding

class learnSkill : AppCompatActivity() {
    private lateinit var binding:ActivityLearnSkillBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLearnSkillBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.communication.setOnClickListener {
            startActivity(Intent(this,communicationHome::class.java))

        }

    }
}