package com.rabitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rabitech.R
import com.rabitech.databinding.FragmentHarvestHistoryBinding

class HarvestHistoryFragment : androidx.fragment.app.Fragment() {


    private lateinit var binding: FragmentHarvestHistoryBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_harvest_history, container, false)

        return inflater.inflate(R.layout.fragment_harvest_history, container, false)

//        val expandableLayout = binding.constLayout
//        val card = binding.cardView
//
//        binding.expand.setOnClickListener {
//            if(expandableLayout.visibility == View.GONE){
//                TransitionManager.beginDelayedTransition(card, AutoTransition())
//                expandableLayout.visibility = View.VISIBLE
//                binding.expand.text = ("Close")
//            } else{
//                TransitionManager.beginDelayedTransition(card, AutoTransition())
//                expandableLayout.visibility = View.GONE
//                binding.expand.text = ("View")
//            }
//        }
//        return  binding.root
    }



}
