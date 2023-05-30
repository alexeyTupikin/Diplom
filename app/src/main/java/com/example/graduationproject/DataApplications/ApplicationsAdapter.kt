package com.example.graduationproject.DataApplications

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.ApplicationInfo
import com.example.graduationproject.CreateWorkoutPlan
import com.example.graduationproject.TrenerArea
import com.example.graduationproject.databinding.ApplicationsItemModelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ApplicationsAdapter(context: Context?): RecyclerView.Adapter<ApplicationsAdapter.ApplicationsViewHolder>() {

    private var listApplications = emptyList<ApplicationsModel>()
    private lateinit var auth: FirebaseAuth
    private var _context = context

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
                application.status = "В обработке"
                val db = Firebase.database
                val ref = db.getReference("applications").child(application.userName.toString())
                ref.setValue(application)
                createWorkoutPlan(application.userName.toString() ,application.lvl.toString())
            }
            itemList.setOnClickListener {
                applicationInfo(application.userName.toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListApplications(list: MutableList<ApplicationsModel>) {
        listApplications = list
        notifyDataSetChanged()
    }

    fun applicationInfo(client: String) {
        val appInfo = Intent(_context, ApplicationInfo::class.java)
        appInfo.putExtra("client", client)
        startActivity(_context!!, appInfo, null)
    }

    fun createWorkoutPlan(client: String, lvl: String) {
        val createWorkoutPlanIntent = Intent(_context, TrenerArea::class.java)
        createWorkoutPlanIntent.putExtra("lvl", lvl)
        createWorkoutPlanIntent.putExtra("client", client)
        startActivity(_context!!, createWorkoutPlanIntent, null)
    }

}