package com.example.sih_version_3.studentActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sih_version_3.R
import com.example.sih_version_3.adapters.mentorAdapter
import com.example.sih_version_3.dtaclasses.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class communicationMentorList : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var adapter: mentorAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var  mDbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communication_mentor_list)

        mAuth= FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().getReference()
        userList= ArrayList()
        adapter= mentorAdapter(this,userList)

        userRecyclerView=findViewById(R.id.mentorRecyclerView)
        userRecyclerView.layoutManager= LinearLayoutManager(this)
        userRecyclerView.adapter=adapter


        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if (currentUser != null && mAuth.currentUser?.email != currentUser.email) {
                        if (currentUser.role.toString() == "MENTOR") {
                            if(currentUser.skill.toString() =="communication"||currentUser.skill.toString() =="communication "||
                                currentUser.skill.toString() =="communication"  ) {
                                userList.add(currentUser)
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error here
            }
        })




    }
}