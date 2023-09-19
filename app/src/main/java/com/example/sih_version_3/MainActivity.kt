package com.example.sih_version_3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sih_version_3.authenticationAndChat.StudentWelcomeActivity
import com.example.sih_version_3.authenticationAndChat.mentorWelcome
import com.example.sih_version_3.authenticationAndChat.signUpActivity

import com.example.sih_version_3.dtaclasses.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignIn:Button
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.emailEditText)
        edtPassword = findViewById(R.id.passwordEditText)
        btnLogin = findViewById(R.id.loginButton)
        btnSignIn = findViewById(R.id.sigButtonnup)

        btnSignIn.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Log in user
                    val user = mAuth.currentUser

                    if (user != null) {
                        // Check the role of the logged-in user
                        checkUserRole(user.uid)
                    }
                } else {
                    Toast.makeText(this@MainActivity, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserRole(uid: String) {
        val roleRef = FirebaseDatabase.getInstance().getReference("user").child(uid).child("role")

        roleRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val role = snapshot.getValue(String::class.java)

                if (role == "STUDENT") {
                    val intent = Intent(this@MainActivity, StudentWelcomeActivity::class.java)
                    startActivity(intent)
                } else if(role =="MENTOR"){
                    val intent = Intent(this@MainActivity, mentorWelcome::class.java)
                    startActivity(intent)

                }

                else {
                    Toast.makeText(this@MainActivity, "abhi teri activity bani na", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@MainActivity, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }



}
