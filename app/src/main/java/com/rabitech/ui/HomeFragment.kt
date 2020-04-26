package com.rabitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rabitech.R
import com.rabitech.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)

        mAuth = FirebaseAuth.getInstance()

        binding.harvestRequest.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_harvestRequsetFragment)
        )

        binding.harvestRequest.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_harvestRequsetFragment)
        }

        binding.notifications.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_notificationsFragment)
        )

        binding.profile.setOnClickListener {view :View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        return binding.root
    }


    private fun signOut() {
        mAuth.signOut()
        Toast.makeText(activity, "Looged out successfully", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_homeFragment_to_mainActivity)
    }

}
