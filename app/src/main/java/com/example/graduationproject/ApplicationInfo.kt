package com.example.graduationproject

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.example.graduationproject.DataApplications.ApplicationsModel
import com.example.graduationproject.DataUserPersonal.UserModel
import com.example.graduationproject.databinding.ActivityApplicationInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class ApplicationInfo : AppCompatActivity() {

    lateinit var binding: ActivityApplicationInfoBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clientName = intent.extras?.getString("client")
        val db = Firebase.database
        val refApplication = db.getReference("applications").child(clientName!!)
        getApplicationInfo(refApplication)
        val refClient = db.getReference("accounts").child(clientName)
        getClientInfo(refClient)

    }

    private fun getClientInfo(dRef: DatabaseReference) {
        dRef.get().addOnSuccessListener {
            dRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val clientInfo = snapshot.getValue(UserModel::class.java)
                    if(clientInfo != null) {
                        binding.textClient.text = "${clientInfo.name} ${clientInfo.lastName} | Возраст: ${clientInfo.years}\n" +
                                "Рост: ${clientInfo.height} | Вес: ${clientInfo.weight} кг."
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    private fun getApplicationInfo(dRef: DatabaseReference) {
        dRef.get().addOnSuccessListener {
            dRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val appInfo = snapshot.getValue(ApplicationsModel::class.java)
                    if(appInfo != null) {
                        binding.textTarget.text = "Цель тренировок: " + appInfo.target
                        binding.textPlace.text = "Место тренировок: " + appInfo.place
                        binding.textLvl.text = "Уровень физ. подготовки: " + appInfo.lvl
                        binding.textTime.text = "Время тренировок: " + appInfo.time
                        if(appInfo.dopInfo != null && appInfo.dopInfo != "") {
                            binding.textDopInfo.text = "Доп. информация: " + appInfo.dopInfo
                        } else {
                            binding.textDopInfo.text = "Дополнительная информация отсутствует"
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}