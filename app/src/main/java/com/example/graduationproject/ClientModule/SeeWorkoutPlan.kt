package com.example.graduationproject.ClientModule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.graduationproject.DataPackage.WorkoutPlan.WorkoutPlanModel
import com.example.graduationproject.databinding.ActivitySeeWorkoutPlanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SeeWorkoutPlan : AppCompatActivity() {

    lateinit var binding: ActivitySeeWorkoutPlanBinding
    lateinit var workoutPlan: WorkoutPlanModel
    lateinit var authUser: FirebaseAuth
    lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeWorkoutPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authUser = Firebase.auth
        userName = authUser.currentUser?.email.toString().substringBefore('@')
        workoutPlan = WorkoutPlanModel()
        val refPlan = Firebase.database.getReference("workoutPlan").child(userName)
        getPlanInfo(refPlan)

    }

    private fun getPlanInfo(dRef: DatabaseReference) {
        dRef.get().addOnSuccessListener {
            dRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val plan = snapshot.getValue(WorkoutPlanModel::class.java)
                    if(plan != null) {
                        if(plan.firstDay != null && plan.firstDay != "") {
                            binding.textViewDay1.text = plan.firstDay
                        }
                        if(plan.secondDay != null && plan.secondDay != "") {
                            binding.textViewDay2.visibility = View.VISIBLE
                            binding.textViewDay2.text = plan.secondDay
                        }
                        if(plan.thirdDay != null && plan.thirdDay != "") {
                            binding.textViewDay3.visibility = View.VISIBLE
                            binding.textViewDay3.text = plan.thirdDay
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

}