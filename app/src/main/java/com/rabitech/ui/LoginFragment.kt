package com.rabitech.ui

import android.app.AlertDialog
import android.app.ProgressDialog
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
import com.rabitech.R
import com.rabitech.dataModels.CustomLoading
import com.rabitech.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    val progressBar = CustomLoading()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnSigupLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkLogedUser()
    }

    private fun checkLogedUser() {
        if (mAuth.currentUser != null){
            findNavController().navigate(R.id.action_loginFragment_to_homeHostFragment)
        }
    }


    private fun login() {

        val userEmail = text_email_login.text.toString()
        val userPass = text_input_pass.text.toString()

        if (userEmail.isEmpty()) {
            text_email_login.error = "Please enter the email address"
            text_email_login.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            text_email_login.error = "Please enter valid email address"
            text_email_login.requestFocus()
            return
        }
        if (userPass.isEmpty()) {
            text_input_pass.error = "Please enter password"
            text_input_pass.requestFocus()
            return
        }


        progressBar.show(this.requireContext(),"Please wait...")
//        binding.progressBar.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener {
            if (it.isSuccessful) {

                Toast.makeText(activity, "login successful", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeHostFragment)
                progressBar.loadingDialog.dismiss()
            }

        }.addOnFailureListener {
            progressBar.loadingDialog.dismiss()
            val snack = Snackbar.make(binding.btnSigupLogin,"Wrong Username or Password", Snackbar.LENGTH_LONG)
            snack.show()
            Toast.makeText(activity, "Login failed No records matching the user details entered .", Toast.LENGTH_LONG).show()
        }
    }

}
