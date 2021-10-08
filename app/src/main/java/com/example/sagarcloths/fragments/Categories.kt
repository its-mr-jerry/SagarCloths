package com.example.sagarcloths.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sagarcloths.*
import com.example.sagarcloths.R
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Categories.newInstance] factory method to
 * create an instance of this fragment.
 */
class Categories : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbRef: DatabaseReference
    private lateinit var RacView: RecyclerView
    private lateinit var catArrayList: ArrayList<CatModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_categories, container, false)
        RacView = view.findViewById(R.id.CateRecView)
        RacView.layoutManager = GridLayoutManager(context, 2)

        RacView.setHasFixedSize(true)
        catArrayList = arrayListOf()
        getUserData()
        return view
    }

    private fun getUserData() {
        dbRef =
            FirebaseDatabase.getInstance("https://sagar-cloth-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("SagarCloths").child("Categories")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (catSnapshot in snapshot.children) {

                        var cat = catSnapshot.getValue(CatModel::class.java)
                        catArrayList.add(cat!!)
                    }
                    RacView.adapter = CatAdapter(catArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
