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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rabitech.R
import com.rabitech.dataModels.Users
import com.rabitech.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
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
        val userEmail = edit_text_email_register.text.toString()
        val userPass = edit_text_pass_register.text.toString()

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

        mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(activity, "Sign up successful", Toast.LENGTH_LONG).show()
                createUser()
                //findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            }
        }.addOnFailureListener {
            Toast.makeText(activity, "Signup faied" + it.message, Toast.LENGTH_LONG).show()

        }
    }

    private fun createUser() {
        val currentUser = mAuth.currentUser!!
        val user = Users(
            currentUser.email.toString(), "user", currentUser.uid
        )
        mDatabase.collection("users").document(currentUser.uid).set(user)

        /*
            .addOnSuccessListener { _ ->

                Toast.makeText(requireContext().applicationContext, "Sign Up Successfully", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "Sign Up Failed: ${exception.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

}
