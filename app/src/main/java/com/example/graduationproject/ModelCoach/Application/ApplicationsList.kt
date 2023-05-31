package com.example.graduationproject.ModelCoach.Application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.DataPackage.DataApplications.ApplicationsAdapter
import com.example.graduationproject.DataPackage.DataApplications.ApplicationsModel
import com.example.graduationproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ApplicationsList : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ApplicationsAdapter
    lateinit var checkBox: CheckBox
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_applications_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        auth = Firebase.auth
        checkBox = view.findViewById(R.id.checkBoxList)
        recyclerView = view.findViewById(R.id.rv_applications)
        adapter = ApplicationsAdapter(this.context)
        recyclerView.adapter = adapter

        val database = Firebase.database
        val refMessage = database.getReference("applications")
        onChangeListener(refMessage, coach = null)

        checkBox.setOnClickListener {
            if(checkBox.isChecked) {
                onChangeListener(refMessage, auth.currentUser?.email.toString().substringBefore('@'))
            } else {
                onChangeListener(refMessage, coach = null)
            }
        }


//        val toastMessage = MessageModel("client", "message")
//        setFragmentResult("toast", bundleOf("toast_key" to toastMessage))
    }

    private fun onChangeListener(dRef: DatabaseReference, coach: String?) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listApplications = mutableListOf<ApplicationsModel>()
                for(s in snapshot.children){
                    val application = s.getValue(ApplicationsModel::class.java)
                    if((application != null) && (application.coach == coach)) {
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