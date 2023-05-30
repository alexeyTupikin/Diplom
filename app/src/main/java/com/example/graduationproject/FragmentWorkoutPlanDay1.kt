package com.example.graduationproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FragmentWorkoutPlanDay1 : Fragment() {

    private lateinit var listExercise: MutableList<uprModel>
    private lateinit var listContent: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_plan_day1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textExerciseDay1 = view.findViewById<TextView>(R.id.textExercisesDay)
        setFragmentResult("day1", bundleOf("day1_key" to textExerciseDay1.text))

        val db = Firebase.database
        val refExercise = db.getReference("physicalExercise")

        listExercise = mutableListOf()
        listContent = mutableListOf()

        for(i in 1..17) {
            getExerciseList(view, refExercise.child(i.toString()))
        }

        val buttonAddExercise = view.findViewById<ImageButton>(R.id.buttonAddExercise)
        val textExercisesDay = view.findViewById<TextView>(R.id.textExercisesDay)
        val spinnerExercise = view.findViewById<Spinner>(R.id.spinnerExercise)

        buttonAddExercise.setOnClickListener {

            textExercisesDay.text = "${textExercisesDay.text}" +
                    "${listExercise[spinnerExercise.selectedItemId.toInt()].content} | "

//            when(lvlClient) {
//                "Начальный" -> textExercisesDay.text = "${textExercisesDay.text}"+"${listExercise[spinnerExercise.selectedItemId.toInt()].qty_lvl1}\n"
//                "Средний" -> textExercisesDay.text = "${textExercisesDay.text}"+"${listExercise[spinnerExercise.selectedItemId.toInt()].qty_lvl2}\n"
//                "Продвинутый" -> textExercisesDay.text = "${textExercisesDay.text}"+"${listExercise[spinnerExercise.selectedItemId.toInt()].qty_lvl3}\n"
//            }
        }
    }

    private fun getExerciseList(view:View ,dRef: DatabaseReference) {
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
                    val adapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_item, listContent)
                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    val spinner = view.findViewById<Spinner>(R.id.spinnerExercise)
                    spinner.adapter = adapter
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}