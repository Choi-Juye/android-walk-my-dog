package com.chloedewyes.walkmydog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.databinding.FragmentProfilePersonBinding
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel
import com.google.android.material.snackbar.Snackbar

class ProfilePersonFragment: Fragment(R.layout.fragment_profile_person) {

    private var _binding: FragmentProfilePersonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilePersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueBtn.setOnClickListener {
            if (updateUI()){
                viewModel.updateUser(binding.etPersonName.text.toString())
                findNavController().navigate(R.id.profileFragment)
            } else {
                Snackbar.make(requireView(), "프로필을 모두 작성해주세요 :)", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() : Boolean {
        val name = binding.etPersonName.text.toString()
        if (name.isEmpty()){
            return false
        }
        return true
    }

}