package com.rabitech.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rabitech.R
import com.rabitech.databinding.AdminFragmentBinding
import kotlinx.android.synthetic.main.fragment_home.*

class AdminFragment : Fragment(), Toolbar.OnMenuItemClickListener,
    androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {

    private val viewModel: AdminViewModel by viewModels()

    private lateinit var binding: AdminFragmentBinding

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.admin_fragment, container, false)

        mAuth = FirebaseAuth.getInstance()

        binding.harvestToolbar.setOnMenuItemClickListener(this)

        binding.allRequests.setOnClickListener {
            findNavController().navigate(R.id.action_adminFragment_to_allHarvestsFragment)
        }
        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_adminFragment_to_profileFragment3)
        }




        /*binding.profile.setOnClickListener {
            findNavController().navigate(R.id.actiona)
        }*/

        return binding.root
    }

    private fun signOut() {
        mAuth.signOut()
        Toast.makeText(activity, "Looged out successfully", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_adminFragment_to_loginFragment)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        signOut()
        return true
    }

}

