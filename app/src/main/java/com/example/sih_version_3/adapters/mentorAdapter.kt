package com.example.sih_version_3.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sih_version_3.R
import com.example.sih_version_3.authenticationAndChat.student_mentor_chat
import com.example.sih_version_3.dtaclasses.User

class mentorAdapter (val context: Context,val userlist:ArrayList<User>):
    RecyclerView.Adapter<mentorAdapter.userViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): userViewHolder {
        val view:View= LayoutInflater.from(context).inflate(R.layout.userlayout,parent,false)
        return userViewHolder(view)
    }

    override fun onBindViewHolder(holder: userViewHolder, position: Int) {
        val currentUser=userlist[position]
        holder.textName.text=currentUser.name

        holder.itemView.setOnClickListener{
            val intent =Intent(context, student_mentor_chat::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return userlist.size
    }

    class userViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName=itemView.findViewById<TextView>(R.id.txt_name)

    }
}
