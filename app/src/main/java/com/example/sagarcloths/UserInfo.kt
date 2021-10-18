package com.example.sagarcloths
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sagarcloths.databinding.ActivityUserInfoBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UserInfo : AppCompatActivity() {
    lateinit var binding: ActivityUserInfoBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var rStorage = FirebaseStorage.getInstance().reference.child(
        "User/" + (auth.currentUser?.uid ?: String()) + "/profilePic"
    )
    val phoneNo: String = auth.currentUser?.phoneNumber.toString()
    var ref = FirebaseDatabase.getInstance().reference.child("phone").child(phoneNo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val getpic = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            { uri ->
                rStorage.putFile(uri).addOnCompleteListener({
                    var picUrl : String = rStorage.downloadUrl.toString()
                    ref.child("ProfileImage").setValue(picUrl).addOnCompleteListener({
                        binding!!.userPic.setImageURI(uri)
                    }).addOnFailureListener({
                        Toast.makeText(
                            applicationContext, it.message,
                            Toast.LENGTH_SHORT
                        ).show()})
                    Toast.makeText(applicationContext, "Image uploaded successfully", Toast.LENGTH_SHORT)
                        .show()
                    binding!!.userPic.setImageURI(uri)
                }).addOnFailureListener({
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                })

            })
        binding.saveName.setOnClickListener({
            var userName: String = binding.editUserName.text.toString()
            if (userName.isBlank()) {
                Toast.makeText(applicationContext, "Name can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                ref.child("Name").setValue(userName).addOnCompleteListener({
                    Toast.makeText(
                        applicationContext,
                        "Name Saved Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.userName.setText(userName)
                    intent= Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }).addOnFailureListener({
                    Toast.makeText(
                        applicationContext,it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }
        })
        binding!!.imageButton.setOnClickListener({
            getpic.launch("image/*")
        })

    }


}