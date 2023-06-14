package com.example.graduationproject.CommonWindows

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.graduationproject.TrainerModule.CoachArea
import com.example.graduationproject.ClientModule.PersonalAreaWindow
import com.example.graduationproject.databinding.ActivityLoginWindowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginWindow : AppCompatActivity() {

    lateinit var binding: ActivityLoginWindowBinding
    lateinit var email: EditText
    lateinit var password: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        email = binding.editTextTextEmailAddress
        password = binding.editTextTextPassword

        binding.buttonRegistration.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.buttonAuth.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        if(user?.email.toString().contains(Regex("""@scvibor\.ru"""))) {
                            val goCoachAreaIntent = Intent(this, CoachArea::class.java)
                            startActivity(goCoachAreaIntent)
                        } else {
                            val goChatWindowIntent = Intent(this, PersonalAreaWindow::class.java)
                            startActivity(goChatWindowIntent)
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

    companion object {
        private const val TAG = "EmailPassword"
        const val EMAIL = "Email"
        const val PASS = "Password"
    }

}