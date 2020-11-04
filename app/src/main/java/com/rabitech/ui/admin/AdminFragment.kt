package com.rabitech.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rabitech.R
import com.rabitech.databinding.AdminFragmentBinding

class AdminFragment : Fragment() {

    private val viewModel: AdminViewModel by viewModels()

    private lateinit var binding: AdminFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.admin_fragment, container, false)


        binding.allRequests.setOnClickListener {
            findNavController().navigate(R.id.action_adminFragment_to_allHarvestsFragment)
        }

        /*binding.profile.setOnClickListener {
            findNavController().navigate(R.id.actiona)
        }*/

        return binding.root
    }

}