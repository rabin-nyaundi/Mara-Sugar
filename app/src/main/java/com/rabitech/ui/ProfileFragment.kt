package com.rabitech.ui

import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.dataModels.UserDetails
import com.rabitech.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var storageReference: StorageReference
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth:FirebaseAuth

    private lateinit var listener:KeyListener
    val progressBar = CustomLoading()

    private var firstname = ""
    private var lastname = ""
    private var phone = ""
    private var email = ""
    private var id_number = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        retrieveProfile()

        binding.editProfile.setOnClickListener {
            enableEdit()
        }
        binding.saveProfile.setOnClickListener {
            validateInput()
            insertProfile()
        }

        return binding.root
    }

    private fun validateInput() {
        firstname = binding.textViewFname.text.toString()
        lastname = binding.textViewLname.text.toString()
        phone = binding.textViewPhone.text.toString()
        email = binding.textViewEmail.text.toString()
        id_number = binding.textViewIdNumber.text.toString()
//        time_request_sent = Timestamp.now()


//        validate input from text fields

        if (firstname.isEmpty()) {
            textView_fname.error = "Please enter the first name"
            textView_fname.requestFocus()
            return
        }
        if (lastname.isEmpty()) {
            textView_lname.error = "Please enter the last name"
            textView_lname.requestFocus()
            return
        }
        if (phone.isEmpty()) {
            textView_phone.error = "Please enter the phone number"
            textView_phone.requestFocus()
            return
        }
        if (email.isEmpty()) {
            textView_email.error = "Please enter the email address"
            textView_email.requestFocus()
            return
        }
        if (id_number.isEmpty()) {
            textView_id_number.error = "Please enter the National ID Number"
            textView_id_number.requestFocus()
            return
        }
    }

    private fun insertProfile() {

        val userId = mAuth.currentUser!!.uid

        val userdetails = UserDetails(
            firstname,
            lastname,
            phone,
            email,
            id_number,
            Timestamp.now(),
            false

        )
        progressBar.show(this.context!!, "Please wait...")
        mDatabase.collection("UserDetails").document(userId).set(userdetails)
            .addOnSuccessListener { document ->

                Log.d("@@@@insertion@@@@", "details added Successfully ${document}")
                progressBar.loadingDialog.dismiss()
                Toast.makeText(
                    activity,
                    "Personal details inserted successfully",
                    Toast.LENGTH_LONG
                )

            }.addOnFailureListener { exception ->

                Toast.makeText(activity, "Failed to insert personal details", Toast.LENGTH_LONG)
                    .show()
            }
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

        binding.textViewFname.isFocusableInTouchMode = true
        binding.textViewLname.isFocusableInTouchMode = true
        binding.textViewPhone.isFocusableInTouchMode = true
        binding.textViewEmail.isFocusableInTouchMode = true
        binding.textViewIdNumber.isFocusableInTouchMode = true
    }

    private fun disableField() {

        binding.textViewFname.isFocusable = false
        binding.textViewLname.isFocusable = false
        binding.textViewPhone.isFocusable = false
        binding.textViewEmail.isFocusable = false
        binding.textViewIdNumber.isFocusable = false

    }

}
