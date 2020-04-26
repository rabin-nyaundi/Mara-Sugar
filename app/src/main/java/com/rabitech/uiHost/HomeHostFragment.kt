package com.rabitech.uiHost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.rabitech.R
import com.rabitech.databinding.FragmentHomeHostBinding
import kotlinx.android.synthetic.main.fragment_home_host.*


class HomeHostFragment : Fragment(), Toolbar.OnMenuItemClickListener{

    private lateinit var drawerlayout: DrawerLayout
    private lateinit var binding: FragmentHomeHostBinding

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//    return inflater.inflate(R.layout.fragment_home_host, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_host, container, false)

        binding.toolbar.setOnMenuItemClickListener(this)

        mAuth = FirebaseAuth.getInstance()

//        val navHostFragment = childFragmentManager.findFragmentById(R.id.myNavHostHome) as NavHostFragment
//        NavigationUI.setupWithNavController(binding.toolbar, NavHostFragment.findNavController(this))

//        navHostFragment.navController.addOnNavigateListener{_,destinaton
//                ->toolbar.title = destinaton.title}

//        val navHostFragment = childFragmentManager.findFragmentById(R.id.myNavHostHome)
//        NavigationUI.setupWithNavController(binding.toolbar, NavHostFragment.findNavController(this))

        val navHostFragment = childFragmentManager.findFragmentById(R.id.myNavHostHome)
        NavigationUI.setupWithNavController(binding.toolbar,navHostFragment!!.findNavController())


        return binding.root

    }

    private fun signOut() {
        mAuth.signOut()
        Toast.makeText(activity, "Looged out successfully", Toast.LENGTH_LONG).show()
        findNavController().navigate(R.id.action_homeHostFragment_to_loginFragment)
//        findNavController().navigate(R.id.action_homeFragment_to_mainActivity)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
            signOut()
            return true
    }
}
