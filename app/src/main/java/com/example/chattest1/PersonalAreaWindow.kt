package com.example.chattest1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.chattest1.DataApplications.ApplicationsModel
import com.example.chattest1.DataUserPersonal.UserModel
import com.example.chattest1.databinding.ActivityPersonalAreaWindowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream

class PersonalAreaWindow : AppCompatActivity() {

    lateinit var binding: ActivityPersonalAreaWindowBinding

    lateinit var authUser: FirebaseAuth
    lateinit var userName: String

    lateinit var photoView: ImageView
    lateinit var setPhotoButton: ImageButton
    lateinit var nameTextView: TextView
    lateinit var lastNameTextView: TextView
    lateinit var heightTextView: TextView
    lateinit var weightTextView: TextView
    lateinit var yearsTextView: TextView
    lateinit var buttonSave: Button
    lateinit var buttonSingOut: Button
    lateinit var buttonAddApplications: Button
    lateinit var applicationsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalAreaWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoView = binding.userPhoto
        setPhotoButton = binding.buttonSetPhoto
        nameTextView = binding.nameTextView
        lastNameTextView = binding.lastNameTextView
        heightTextView = binding.heightTextView
        weightTextView = binding.weightTextView
        yearsTextView = binding.yearsTextView
        buttonSave = binding.buttonSaveInfo
        buttonSingOut = binding.buttonSingOut
        buttonAddApplications = binding.buttonAddApplications
        applicationsText = binding.textViewApplications

        authUser = Firebase.auth
        userName = authUser.currentUser?.email.toString().substringBefore('@')

        val database = Firebase.database
        Firebase.storage.reference.child(authUser.currentUser?.email.toString()).getBytes(1024*1024).addOnSuccessListener { data ->
            photoView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.size))
        }

        val refAuth = database.getReference("accounts").child(userName)
        refAuth.get().addOnSuccessListener {
            refAuth.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    if(user != null) {
                        nameTextView.text = user.name
                        lastNameTextView.text = user.lastName
                        heightTextView.text = user.height
                        weightTextView.text = user.weight
                        yearsTextView.text = user.years
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        val refApplications = database.getReference("applications").child(userName)
        refApplications.get().addOnSuccessListener {
            refApplications.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val applications = snapshot.getValue(ApplicationsModel::class.java)
                    if(applications != null) {
                        applicationsText.text = "Дата создания: ${applications.date}\nСтатус: ${applications.status}"
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        setPhotoButton.setOnClickListener {
            val intentPhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intentPhoto, CAMERA_REQ_CODE)
        }



        buttonSave.setOnClickListener {
            uploadImage()
            val db = Firebase.database
            val ref = db.getReference("accounts").child(userName)
            ref.setValue(UserModel(
                nameTextView.text.toString(),
                lastNameTextView.text.toString(),
                heightTextView.text.toString(),
                weightTextView.text.toString(),
                yearsTextView.text.toString()
            ))
            uploadImage()
        }

        buttonAddApplications.setOnClickListener {
            val createApplicationsWindow = Intent(this, CreateApplications::class.java)
            startActivity(createApplicationsWindow)
        }

        buttonSingOut.setOnClickListener {
            Firebase.auth.signOut()
            finish()
        }

    }

    private fun uploadImage(){
        photoView.isDrawingCacheEnabled = true
        photoView.buildDrawingCache()
        val bitmap = (photoView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val storageRef = Firebase.storage.reference.child(authUser.currentUser?.email.toString())
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if((resultCode == RESULT_OK) && (requestCode == CAMERA_REQ_CODE)) {
            val photo = data?.extras?.get("data") as Bitmap
            photoView.setImageBitmap(photo)
        }
    }

    companion object {
        const val CAMERA_REQ_CODE = 100
    }
}