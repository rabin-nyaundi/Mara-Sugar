package com.rabitech.ui

import android.os.Bundle
import android.text.method.KeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rabitech.R
import com.rabitech.databinding.FragmentProfileBinding

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var storageReference: StorageReference
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth:FirebaseAuth

    private lateinit var listener:KeyListener

    private var firstname = ""
    private var lastname = ""
    private var phone = ""
    private var email = ""
    private var id_number = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_profile, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)


        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        retrieveProfile()

        binding.editProfile.setOnClickListener {
            enableEdit()
        }

        return binding.root
    }

    private fun retrieveProfile() {

        val userId = mAuth.currentUser!!.uid
        val user_email = mAuth.currentUser!!.email


        val ref = mDatabase.collection("UserDetails").document(userId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()){
                    firstname = documentSnapshot.getString("user_fname").toString()
                    lastname = documentSnapshot.getString("user_lname").toString()
                    phone = documentSnapshot.getString("user_phone").toString()
                    email = user_email.toString()
                    id_number = documentSnapshot.getString("user_National_id").toString()

                    binding.textViewFname.setText(firstname)
                    binding.textViewLname.setText(lastname)
                    binding.textViewPhone.setText(phone)
                    binding.textViewEmail.setText(email)
                    binding.textViewIdNumber.setText(id_number)

                    disableField()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(activity,"Failed to fetch data from the database" +exception, Toast.LENGTH_LONG).show()
            }
    }

    private fun enableEdit() {

        binding.textViewFname.setFocusableInTouchMode(true)
        binding.textViewLname.setFocusableInTouchMode(true)
        binding.textViewPhone.setFocusableInTouchMode(true)
        binding.textViewEmail.setFocusableInTouchMode(true)
        binding.textViewIdNumber.setFocusableInTouchMode(true)
    }

    private fun disableField() {

        binding.textViewFname.setFocusable(false)
        binding.textViewLname.setFocusable(false)
        binding.textViewPhone.setFocusable(false)
        binding.textViewEmail.setFocusable(false)
        binding.textViewIdNumber.setFocusable(false)

    }

}
