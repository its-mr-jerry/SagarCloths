package com.example.sagarcloths.fragments


import android.net.Uri
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.sagarcloths.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val phoneNo: String = auth.currentUser?.phoneNumber.toString()
    var ref = FirebaseDatabase.getInstance().reference.child("phone").child(phoneNo)
    var rStorage = FirebaseStorage.getInstance().reference.child(
        "User/" + (auth.currentUser?.uid ?: String()) + "/profilePic"
    )
    lateinit var picUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    var binding: FragmentProfileBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        getUser()
        val getpic = registerForActivityResult(ActivityResultContracts.GetContent(),
            { uri ->
                rStorage.putFile(uri).addOnCompleteListener({
                    var picUrl : String = rStorage.downloadUrl.toString()
                    ref.child("ProfileImage").setValue(picUrl).addOnCompleteListener({
                       binding!!.userPic.setImageURI(uri)
                    }).addOnFailureListener({
                        Toast.makeText(
                            context, it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                    Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT)
                        .show()
                }).addOnFailureListener({
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                })

            })
        binding!!.saveName.setOnClickListener { View: View? ->
            var userName: String = binding!!.editUserName.text.toString()
             ref.child("Name").setValue(userName).addOnCompleteListener({
                Toast.makeText(
                    context,
                    "Name Saved Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }).addOnFailureListener({
                Toast.makeText(
                    context, it.message,
                    Toast.LENGTH_SHORT
                ).show()
            })

            if (binding!!.editUserName.text.toString().isEmpty()) {
                Toast.makeText(context, "Name Can't Be Empty", Toast.LENGTH_SHORT).show()
            } else {
                  binding!!.userName.setText(userName)
            }
        }
        binding!!.imageButton.setOnClickListener({
            getpic.launch("image/*")
        })
        return binding!!.root
    }
    fun getUser(){
        // Read from the database
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val Name = snapshot.child("Name").getValue().toString()
                val pic = snapshot.child("ProfileImage").toString()
                binding?.userName?.setText(Name)
                context?.let { binding?.let { it1 -> Glide.with(it).load(pic).into(it1.userPic) } }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): Profile {
            val fragment = Profile()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}