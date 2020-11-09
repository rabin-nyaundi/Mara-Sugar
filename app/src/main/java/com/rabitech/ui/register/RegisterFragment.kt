package com.rabitech.ui.register

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.dataModels.Users
import com.rabitech.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var  binding: FragmentRegisterBinding

    val progressBar = CustomLoading()

    private var userEmail =""
    private var userPass = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false
        )
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseFirestore.getInstance()

        binding.btnSigup.setOnClickListener {
            validateInputs()
        }
        binding.textLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return binding.root
    }

    private fun validateInputs() {
        userEmail = edit_text_email_register.text.toString()
        userPass = edit_text_pass_register.text.toString()

        if (userEmail.isEmpty()) {
            edit_text_email_register.error = "Please enter the email address"
            edit_text_email_register.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            edit_text_email_register.error = "Please enter valid email address"
            edit_text_email_register.requestFocus()
            return
        }
        if (userPass.isEmpty()) {
            edit_text_pass_register.error = "Please enter password"
            edit_text_pass_register.requestFocus()
            return
        }

      registerUser()
    }

    private fun registerUser() {
        progressBar.show(this.requireContext(), "Please wait...")

        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Sign up successful", Toast.LENGTH_LONG).show()
                createUser()
                progressBar.loadingDialog.dismiss()

                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }.addOnFailureListener {
            val snack =  Snackbar.make(
                binding.btnSigup,
                "Failed to register",
                Snackbar.LENGTH_LONG
            )
            snack.show()

            Toast.makeText(activity, "Signup faied" + it.message, Toast.LENGTH_LONG).show()

        }
    }

    private fun createUser() {
        val currentUser = mAuth.currentUser!!
        val user = Users(
            currentUser.email.toString(), "user", currentUser.uid
        )
        mDatabase.collection("users").document(currentUser.uid).set(user)
    }

}
