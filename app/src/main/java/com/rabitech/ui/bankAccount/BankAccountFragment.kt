package com.rabitech.ui.bankAccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import com.rabitech.R
import com.rabitech.dataModels.BankAccount
import com.rabitech.dataModels.CustomLoading
import com.rabitech.databinding.FragmentBankAccountBinding
import com.rabitech.databinding.FragmentHarvestRequsetBinding

class BankAccountFragment : Fragment() {

    private  val viewModel: BankAccountViewModel by viewModels()
    private lateinit var binding:FragmentBankAccountBinding

    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference

    val progressBar = CustomLoading()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate( inflater, R.layout.fragment_bank__account_, container, false)

        mDatabase = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        setOnClickListeners()


        return  binding.root
    }

    private fun setOnClickListeners() {
        binding.bankSubmitBtn.setOnClickListener {
            if(validateInput()){
                addBankAccount()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = false

        viewModel.bankName = binding.textBank.text.toString()
        viewModel.bankHolderName = binding.textBankHolder.text.toString()
        viewModel.bankBranch = binding.textBankBranch.text.toString()
        viewModel.bankAccountNumber = binding.textBankAccountNumber.text.toString()

        if(viewModel.bankName.isEmpty()){
            binding.textBank.error = "Please enter the the Bank Account Name"
            binding.textBank.requestFocus()
            isValid = false
        }
        if(viewModel.bankHolderName.isEmpty()){
            binding.textBankHolder.error = "Please enter the the Bank Holder Name Name"
            binding.textBankHolder.requestFocus()
            isValid = false
        }
        if(viewModel.bankBranch.isEmpty()){
            binding.textBankBranch.error = "Please enter the the Bank Branch Name Name"
            binding.textBankBranch.requestFocus()
            isValid = false
        }
        if(viewModel.bankAccountNumber.isEmpty()){
            binding.textBankAccountNumber.error = "Please enter the the Bank Account Number"
            binding.textBankAccountNumber.requestFocus()
            isValid = false
        }

        if (viewModel.bankName.isNotEmpty() && viewModel.bankHolderName.isNotEmpty() && viewModel.bankBranch.isNotEmpty() && viewModel.bankAccountNumber.isNotEmpty()
        ) {
            isValid = true
        }

        return  isValid
    }

    private fun addBankAccount() {

        val userId = mAuth.currentUser!!.uid

        val bankAccount = BankAccount(
            viewModel.bankName,
            viewModel.bankHolderName,
            viewModel.bankBranch,
            viewModel.bankAccountNumber,
            userId
        )

        progressBar.show(this.requireContext(), "Please wait...")
        mDatabase.collection("bankAccountDetails").document(userId).set(bankAccount)
            .addOnSuccessListener {

                progressBar.loadingDialog.dismiss()
                loadSnackbar()
                Toast.makeText(
                    activity,
                    "Personal details inserted successfully",
                    Toast.LENGTH_LONG
                )
            }
            .addOnFailureListener {
                Toast.makeText(activity, "Failed to insert personal details", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun loadSnackbar() {
        val snack = Snackbar.make(binding.imageView, "Bank Account Added Successfully", Snackbar.LENGTH_LONG)
        snack.show()
    }

}
