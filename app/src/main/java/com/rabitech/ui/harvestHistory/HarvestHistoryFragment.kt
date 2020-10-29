package com.rabitech.ui.harvestHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.rabitech.R
import com.rabitech.databinding.FragmentHarvestHistoryBinding
import com.rabitech.network.NetworkState
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class HarvestHistoryFragment : androidx.fragment.app.Fragment() {

    @ExperimentalCoroutinesApi
    private val viewModel : HarvestHistoryViewModel by viewModels()

    private lateinit var binding: FragmentHarvestHistoryBinding
    private lateinit var adapter: HarvestHistoryAdapter
    private lateinit var auth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_history, container, false)
        auth = FirebaseAuth.getInstance()

        setUpDisplayContent()

        return  binding.root
    }

    private fun setUpDisplayContent() {
        adapter = HarvestHistoryAdapter()
        binding.recyclerViewHarvestHistory.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.getCases(auth.currentUser!!.uid).observe(viewLifecycleOwner) { networkResponse ->

            when (networkResponse) {
                is NetworkState.Loading -> {
//                    binding.progressBarCases.visibility = View.VISIBLE
                }

                is NetworkState.Failed -> {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${networkResponse.exception}",
                        Toast.LENGTH_SHORT
                    ).show()

//                    binding.progressBarCases.visibility = View.INVISIBLE
                }

                is NetworkState.Success -> {
                    adapter.submitList(networkResponse.data)
//                    binding.progressBarCases.visibility = View.INVISIBLE
                }
            }

        }

    }



}
