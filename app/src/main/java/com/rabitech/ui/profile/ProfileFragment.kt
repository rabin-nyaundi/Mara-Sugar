package com.rabitech.ui.profile

import android.os.Bundle
import android.text.method.KeyListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.dataModels.ProfileUser
import com.rabitech.databinding.FragmentProfileBinding
import com.rabitech.ui.harvestRequest.HarvestRequestViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileBinding
    private lateinit var storageReference: StorageReference
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    private lateinit var listener: KeyListener
    val progressBar = CustomLoading()

//    private var firstname = ""
//    private var lastname = ""
//    private var phone = ""
//    private var email = ""
//    private var id_number = ""

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

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.saveProfile.setOnClickListener {
            if (validateInput()) {
                insertProfile()
            } else {
                val snack =
                    Snackbar.make(binding.saveProfile, "An error occured", Snackbar.LENGTH_LONG)
                snack.show()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = false

        viewModel.firstName = binding.textViewFname.text.toString()
        viewModel.lastName = binding.textViewLname.text.toString()
        viewModel.phone = binding.textViewPhone.text.toString()
        viewModel.email = binding.textViewEmail.text.toString()
        viewModel.nationalId = binding.textViewIdNumber.text.toString()


//        Validate input from text fields

        if (viewModel.firstName.isEmpty()) {
            textView_fname.error = "Please enter the first name"
            textView_fname.requestFocus()
            isValid = false
        }
        if (viewModel.lastName.isEmpty()) {
            textView_lname.error = "Please enter the last name"
            textView_lname.requestFocus()
            isValid = false
        }
        if (viewModel.phone.isEmpty()) {
            textView_phone.error = "Please enter the phone number"
            textView_phone.requestFocus()
            isValid = false
        }
        if (viewModel.email.isEmpty()) {
            textView_email.error = "Please enter the email address"
            textView_email.requestFocus()
            isValid = false
        }
        if (viewModel.nationalId.isEmpty()) {
            textView_id_number.error = "Please enter the National ID Number"
            textView_id_number.requestFocus()
            isValid = false
        }
        if (viewModel.nationalId.length < 7) {
            textView_id_number.error = "The National ID should be 8 Characters Long"
            textView_id_number.requestFocus()
            isValid = false
        }
        if (viewModel.firstName.isNotEmpty() && viewModel.lastName.isNotEmpty() && viewModel.phone.isNotEmpty() && viewModel.email.isNotEmpty()
            && viewModel.nationalId.length > 7 && viewModel.nationalId.length < 8

        ) {
            isValid = true
        }

        return isValid
    }

    private fun insertProfile() {

        val userId = mAuth.currentUser!!.uid

        val userDetails = ProfileUser(
            viewModel.firstName,
            viewModel.lastName,
            viewModel.phone,
            viewModel.email,
            viewModel.nationalId,
            userId,


            )
        progressBar.show(this.requireContext(), "Please wait...")
        mDatabase.collection("profileUser").document(userId).set(userDetails)
            .addOnSuccessListener { document ->

                Log.d("@@@@insertion@@@@", "details added Successfully ${document}")
                progressBar.loadingDialog.dismiss()
                val snack =Snackbar.make(binding.textViewIdNumber,"Profile Successfully Updated",Snackbar.LENGTH_LONG)
                snack.show()
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


        val ref = mDatabase.collection("profileUser").document(userId)
        ref.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    if (ref == null) {
                        findNavController().navigate(R.id.action_homeFragment_to_harvestRequsetFragment)
                    }
                    viewModel.firstName = documentSnapshot.getString("firstName").toString()
                    viewModel.lastName = documentSnapshot.getString("lastName").toString()
                    viewModel.phone = documentSnapshot.getString("phone").toString()
                    viewModel.email = user_email.toString()
                    viewModel.nationalId = documentSnapshot.getString("nationalId").toString()

                    binding.textViewFname.setText(viewModel.firstName)
                    binding.textViewLname.setText(viewModel.lastName)
                    binding.textViewPhone.setText(viewModel.phone)
                    binding.textViewEmail.setText(user_email)
                    binding.textViewIdNumber.setText(viewModel.nationalId)

                    disableField()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    activity,
                    "Failed to fetch data from the database$exception",
                    Toast.LENGTH_LONG
                ).show()
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
