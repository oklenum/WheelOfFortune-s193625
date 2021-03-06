package com.example.wheeloffortune

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.wheeloffortune.databinding.FragmentTitleBinding


class TitleFragment : Fragment() {
    lateinit var binding:FragmentTitleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTitleBinding.inflate(inflater, container, false)
        binding.btnPlay.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_titleFragment_to_mainFragment)
        }
        binding.btnRules.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_titleFragment_to_rulesFragment)
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}