package com.example.graduationproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.graduationproject.DataUserPersonal.UserModel
import com.example.graduationproject.WorkoutPlan.WorkoutPlanModel
import com.example.graduationproject.databinding.ActivityTrenerAreaBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TrenerArea : AppCompatActivity() {

    private lateinit var binding: ActivityTrenerAreaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTrenerAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_trener_area)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragmentWorkoutPlanDay1, R.id.fragmentWorkoutPlanDay2, R.id.fragmentWorkoutPlanDay3
            )
        )

        val clientName = intent.extras?.getString("client")
        val lvl = intent.extras?.getString("lvl")

        val viewModelForDay =
            ViewModelProvider(this)[ViewModelForDay::class.java]
        viewModelForDay._client.value = clientName.toString()
        viewModelForDay._lvl.value = lvl.toString()

        val secondDay: BottomNavigationItemView = findViewById(R.id.fragmentWorkoutPlanDay2)
        secondDay.visibility = View.INVISIBLE
        val threeDay: BottomNavigationItemView = findViewById(R.id.fragmentWorkoutPlanDay3)
        threeDay.visibility = View.INVISIBLE

        val database = Firebase.database
        val refAuth = database.getReference("workoutPlan").child(clientName!!)
        refAuth.get().addOnSuccessListener {
            refAuth.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val plan = snapshot.getValue(WorkoutPlanModel::class.java)
                    if(plan != null) {
                        viewModelForDay._exerciseDay1.value = plan.firstDay
                        viewModelForDay._exerciseDay2.value = plan.secondDay
                        viewModelForDay._exerciseDay3.value = plan.thirdDay
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        binding.buttonAddDay.setOnClickListener {
            when(navView.selectedItemId) {
                R.id.fragmentWorkoutPlanDay1 -> secondDay.visibility = View.VISIBLE
                R.id.fragmentWorkoutPlanDay2 -> threeDay.visibility = View.VISIBLE
            }
        }

        binding.buttonSavePlan.setOnClickListener {
            var exerciseDay1: String? = null
            var exerciseDay2: String? = null
            var exerciseDay3: String? = null

            val viewModelForDay =
                ViewModelProvider(this)[ViewModelForDay::class.java]

            viewModelForDay.exerciseDay1.observe(this) {
                if(it != null && it != "") {
                    exerciseDay1 = it
                }
            }

            viewModelForDay.exerciseDay2.observe(this) {
                if(it != null && it != "") {
                    exerciseDay2 = it
                }
            }

            viewModelForDay.exerciseDay3.observe(this) {
                if(it != null && it != "") {
                    exerciseDay3 = it
                }
            }

            val db = Firebase.database
            val ref = db.getReference("workoutPlan").child(clientName!!)
            ref.setValue(WorkoutPlanModel(exerciseDay1, exerciseDay2, exerciseDay3))

        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}