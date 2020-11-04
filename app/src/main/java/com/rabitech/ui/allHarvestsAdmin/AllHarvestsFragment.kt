package com.rabitech.ui.allHarvestsAdmin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.rabitech.R
import com.rabitech.databinding.AllHarvestsFragmentBinding
import com.rabitech.network.NetworkState
import com.rabitech.ui.harvestHistory.HarvestHistoryAdapter
import com.rabitech.ui.harvestHistory.HarvestHistoryViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AllHarvestsFragment : Fragment() {

    private val viewModel : AllHarvestsViewModel by viewModels()

   private lateinit var binding: AllHarvestsFragmentBinding


    private lateinit var adapter:AllHarvestAdapter
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.all_harvests_fragment, container, false)
        auth = FirebaseAuth.getInstance()

        setUpDisplayContent()
        return  binding.root
    }

    private fun setUpDisplayContent() {
        adapter = AllHarvestAdapter()
        binding.recyclerViewHarvestHistory.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObserver()
    }
    private fun setupObserver() {
        viewModel.getHarvestRequests().observe(viewLifecycleOwner) { networkResponse ->

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