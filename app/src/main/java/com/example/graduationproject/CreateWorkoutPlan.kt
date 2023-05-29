package com.example.graduationproject

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.example.graduationproject.databinding.ActivityCreateWorkoutPlanBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.childEvents
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import org.w3c.dom.Text


class CreateWorkoutPlan : AppCompatActivity() {

    private lateinit var binding: ActivityCreateWorkoutPlanBinding
    private lateinit var listExercise: MutableList<uprModel>
    private lateinit var listContent: MutableList<String>

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateWorkoutPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.database
        val refExercise = db.getReference("physicalExercise")

        val lvlClient = intent.extras?.getString("lvl")
        Toast.makeText(this, lvlClient, Toast.LENGTH_LONG).show()

        listExercise = mutableListOf()
        listContent = mutableListOf()

        for(i in 1..17) {
            getExerciseList(refExercise.child(i.toString()))
        }

        binding.buttonAddExercise.setOnClickListener {

            binding.textExercisesDay.text = "${binding.textExercisesDay.text}" +
                    "${listExercise[binding.spinnerExercise.selectedItemId.toInt()].content} | "

            when(lvlClient) {
                "Начальнай" -> binding.textExercisesDay.text = "${binding.textExercisesDay.text}"+"${listExercise[binding.spinnerExercise.selectedItemId.toInt()].qty_lvl1}\n"
                "Средний" -> binding.textExercisesDay.text = "${binding.textExercisesDay.text}"+"${listExercise[binding.spinnerExercise.selectedItemId.toInt()].qty_lvl2}\n"
                "Продвинутый" -> binding.textExercisesDay.text = "${binding.textExercisesDay.text}"+"${listExercise[binding.spinnerExercise.selectedItemId.toInt()].qty_lvl3}\n"
            }
        }

//        binding.buttonAddDay.setOnClickListener {
//            val myTextView = TextView(this)
//            myTextView.apply {
//                background = getDrawable(R.color.edit_text_background)
//                backgroundTintList = getColorStateList(R.color.hint_color)
//                backgroundTintMode = PorterDuff.Mode.ADD
//            }
//
//            val textLP = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//            textLP.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
//            textLP.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
//            textLP.topToBottom = R.id.spinnerExercise
//            textLP.marginStart = binding.textExercisesDay.marginStart
//            textLP.marginEnd = binding.textExercisesDay.marginEnd
//            textLP.topMargin = 100
//
//            binding.root.addView(myTextView, textLP)
//
//        }

    }

    private fun getExerciseList(dRef: DatabaseReference) {
        dRef.get().addOnSuccessListener {
            dRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(s in snapshot.children) {
                        val exercise = snapshot.getValue(uprModel::class.java)
                        if(exercise != null && !listExercise.contains(exercise)) {
                            listExercise.add(exercise)
                            listContent.add(exercise.content.toString())
                        }
                    }
                    val adapter = ArrayAdapter(this@CreateWorkoutPlan, android.R.layout.simple_spinner_item, listContent)
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

                    binding.spinnerExercise.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

}