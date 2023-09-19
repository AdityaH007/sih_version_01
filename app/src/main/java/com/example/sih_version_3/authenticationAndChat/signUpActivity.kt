package com.example.sih_version_3.authenticationAndChat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.sih_version_3.MainActivity
import com.example.sih_version_3.R
import com.example.sih_version_3.dtaclasses.User
import com.example.sih_version_3.dtaclasses.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signUpActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtPhoneNumber: EditText
    private lateinit var edtLanguage: EditText
    private lateinit var radioGroupRole: RadioGroup
    private lateinit var radioButtonStudent: RadioButton
    private lateinit var radioButtonMentor: RadioButton
    private lateinit var edtSkill: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        edtName = findViewById(R.id.editTextName)
        edtEmail = findViewById(R.id.editTextEmail)
        edtPassword = findViewById(R.id.editTextPassword)
        edtPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        edtLanguage = findViewById(R.id.editTextLanguage)
        radioGroupRole = findViewById(R.id.radioGroupRole)
        radioButtonStudent = findViewById(R.id.radioButtonStudent)
        radioButtonMentor = findViewById(R.id.radioButtonMentor)
        edtSkill = findViewById(R.id.editTextSkill)
        btnSignUp = findViewById(R.id.buttonSignUp)

        radioGroupRole.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radioButtonMentor) {
                // If mentor role is selected, show the skill EditText
                edtSkill.visibility = View.VISIBLE
            } else {
                // If student role is selected, hide the skill EditText
                edtSkill.visibility = View.GONE
            }
        }

        btnSignUp.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val phoneNumber = edtPhoneNumber.text.toString()
            val language = edtLanguage.text.toString()
            val selectedRole = if (radioButtonMentor.isChecked) UserRole.MENTOR else UserRole.STUDENT
            val skill = edtSkill.text.toString().takeIf { radioButtonMentor.isChecked }

            signUp(name, email, password, phoneNumber, language, selectedRole, skill)
        }
    }

    private fun signUp(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        language: String,
        role: UserRole,
        skill: String?
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!, phoneNumber, language, role, skill)
                    val intent = Intent(this@signUpActivity, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@signUpActivity, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(
        name: String,
        email: String,
        uid: String,
        phoneNumber: String,
        language: String,
        role: UserRole,
        skill: String?
    ) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val user =
            User(name, email, "", phoneNumber, language, role, skill,uid)
        mDbRef.child("user").child(uid).setValue(user)
    }
}