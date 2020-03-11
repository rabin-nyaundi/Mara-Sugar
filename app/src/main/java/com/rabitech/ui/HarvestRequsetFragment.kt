package com.rabitech.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rabitech.R
import com.rabitech.dataModels.UserDetails
import com.rabitech.databinding.FragmentHarvestRequsetBinding
import kotlinx.android.synthetic.main.fragment_harvest_requset.*
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class HarvestRequsetFragment : Fragment() {

    private lateinit var binding: FragmentHarvestRequsetBinding
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth


    private var firstname = ""
    private var lastname = ""
    private var phone = ""
    private var email = ""
    private var id_number = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_harvest_requset, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_requset, container,false)

        mDatabase = FirebaseFirestore.getInstance()

        mAuth = FirebaseAuth.getInstance()

        binding.submitRequest.setOnClickListener {
            insertPersonalDetals()

        }

        return binding.root

    }

    private fun insertPersonalDetals() {
        firstname = binding.textFname.text.toString()
        lastname = binding.textLname.text.toString()
        phone = binding.textPhone.text.toString()
        email = binding.textEmail.text.toString()
        id_number = binding.textIdcard.text.toString()

        if (firstname.isEmpty()) {
            text_fname.error = "Please enter the first name"
            text_fname.requestFocus()
            return
        }
        if (lastname.isEmpty()) {
            text_lname.error = "Please enter the last name"
            text_lname.requestFocus()
            return
        }
        if (phone.isEmpty()) {
            text_phone.error = "Please enter the phone number"
            text_phone.requestFocus()
            return
        }
        if (email.isEmpty()) {
            text_email.error = "Please enter the email address"
            text_email_login.requestFocus()
            return
        }
        if (id_number.isEmpty()) {
            text_idcard.error = "Please enter the National ID Number"
            text_idcard.requestFocus()
            return
        }

        insertDetails()
    }

    private fun insertDetails() {
//
//        val userdetails= mDatabase.collection("details").document()
//        val details =UserDetails(
//            mAuth.currentUser?.email.toString(),
//        firstn
//
//        )
        val userdetails = UserDetails(
            firstname,
            lastname,
            phone,
            email,
            id_number

        )
        mDatabase.collection("UserDetails").add(userdetails)
            .addOnSuccessListener { documentReference ->
//                Toast.makeText(context,"Successfully added to the database",Toast.LENGTH_LONG).show()
                Log.d("@@@@insertion@@@@", "details added Successfully ${documentReference.id}")

            }.addOnFailureListener { exception ->
//                Toast.makeText(context,"Failed to insert to database",Toast.LENGTH_LONG).show()

                Log.d(
                    "@@@@insertion failedd@",
                    "User details insertion failed ${exception.localizedMessage}"
                )

            }
    }


}
