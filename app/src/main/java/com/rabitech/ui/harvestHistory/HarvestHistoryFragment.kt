package com.rabitech.ui.harvestHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.rabitech.R
import com.rabitech.databinding.FragmentHarvestHistoryBinding

class HarvestHistoryFragment : androidx.fragment.app.Fragment() {


    private lateinit var binding: FragmentHarvestHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_history, container, false)

        return  binding.root
    }



}
