package com.example.graduationproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.DataApplications.ApplicationsAdapter
import com.example.graduationproject.DataApplications.ApplicationsModel
import com.example.graduationproject.DataMessage.MessageModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ApplicationsList : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ApplicationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_applications_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_applications)
        adapter = ApplicationsAdapter()
        recyclerView.adapter = adapter

        val database = Firebase.database
        val refMessage = database.getReference("applications")
        onChangeListener(refMessage)
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listApplications = mutableListOf<ApplicationsModel>()
                for(s in snapshot.children){
                    val application = s.getValue(ApplicationsModel::class.java)
                    if((application != null) && (application.coach == null)) {
                        listApplications.add(application)
                    }
                }
                adapter.setListApplications(listApplications)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}