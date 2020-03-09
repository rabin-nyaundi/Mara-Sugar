package com.rabitech.uiHost

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import com.rabitech.R
import com.rabitech.databinding.FragmentHomeHostBinding


class HomeHostFragment : Fragment() {


    private lateinit var drawerlayout: DrawerLayout
    private  lateinit var binding: FragmentHomeHostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_host, container, false)
    }

}
