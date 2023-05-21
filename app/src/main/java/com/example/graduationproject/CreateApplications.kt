package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.example.graduationproject.DataApplications.ApplicationsModel
import com.example.graduationproject.databinding.ActivityCreateApplicationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateApplications : AppCompatActivity() {

    lateinit var binding: ActivityCreateApplicationsBinding
    lateinit var targetText: EditText
    lateinit var place: Spinner
    lateinit var lvlTraining: Spinner
    lateinit var trainingTime: EditText
    lateinit var dopInfo: EditText
    lateinit var buttonSave: Button

    lateinit var authUser: FirebaseAuth
    lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateApplicationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        targetText = binding.editTextTarget
        place = binding.spinnerPlace
        lvlTraining = binding.spinnerLvLTraining
        trainingTime = binding.editTextTrainingTime
        dopInfo = binding.editTextDopInfo
        buttonSave = binding.buttonSaveApplications


        authUser = Firebase.auth
        userName = authUser.currentUser?.email.toString().substringBefore('@')

        buttonSave.setOnClickListener {
            val db = Firebase.database
            val ref = db.getReference("applications").child(userName)
            ref.setValue(ApplicationsModel(
                userName,
                targetText.text.toString(),
                place.selectedItem.toString(),
                lvlTraining.selectedItem.toString(),
                trainingTime.text.toString(),
                dopInfo.text.toString(),
                status = "Создана",
                date = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm"))
                    .toString(),
                coach = null
            ))
            finish()
        }
    }
}