package com.rabitech.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.rabitech.R
import com.rabitech.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        setHasOptionsMenu(true)

        mAuth = FirebaseAuth.getInstance()

        binding.harvestRequest.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_harvestRequsetFragment)
        )

        binding.harvestRequest.setOnClickListener {view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_harvestRequsetFragment)
        }

        binding.notifications.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_notificationsFragment)
        )

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.sign_out_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mAuth.signOut()
        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        mAuth.signOut()
        Toast.makeText(activity, "Looged out successfully", Toast.LENGTH_LONG).show()
//        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

    }


}
