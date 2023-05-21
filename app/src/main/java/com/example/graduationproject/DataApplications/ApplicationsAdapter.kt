package com.example.graduationproject.DataApplications

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.DataMessage.MessageAdapter
import com.example.graduationproject.DataMessage.MessageModel
import com.example.graduationproject.databinding.ApplicationsItemModelBinding
import com.example.graduationproject.databinding.MessageItemModelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ApplicationsAdapter: RecyclerView.Adapter<ApplicationsAdapter.ApplicationsViewHolder>() {

    private var listApplications = emptyList<ApplicationsModel>()
    private lateinit var auth: FirebaseAuth

    class ApplicationsViewHolder(val binding: ApplicationsItemModelBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ApplicationsItemModelBinding.inflate(inflater, parent, false)
        return ApplicationsViewHolder(binding)
    }

    override fun getItemCount(): Int = listApplications.size

    override fun onBindViewHolder(holder: ApplicationsViewHolder, position: Int) {
        val application = listApplications[position]
        auth = Firebase.auth
        with(holder.binding) {
            targetText.text = application.target
            placeText.text = application.place
            dateText.text = application.date
            acceptButton.setOnClickListener {
                application.coach = auth.currentUser?.email.toString().substringBefore('@')
                val db = Firebase.database
                val ref = db.getReference("applications").child(application.userName.toString())
                ref.setValue(application)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListApplications(list: MutableList<ApplicationsModel>) {
        listApplications = list
        notifyDataSetChanged()
    }

}