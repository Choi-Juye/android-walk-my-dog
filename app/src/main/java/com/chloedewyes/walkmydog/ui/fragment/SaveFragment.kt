package com.chloedewyes.walkmydog.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.databinding.FragmentSaveBinding
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel

class SaveFragment : Fragment(R.layout.item_walk) {

    private var _binding: FragmentSaveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}