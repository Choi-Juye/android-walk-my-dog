package com.chloedewyes.walkmydog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.adpater.DogAdapter
import com.chloedewyes.walkmydog.databinding.FragmentProfileBinding
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    private lateinit var dogAdapter: DogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSignOut.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.signInFragment)
        }

        binding.ivEditProfile.setOnClickListener {
            findNavController().navigate(R.id.profilePersonFragment)
        }

        viewModel.selectPersonProfile().observe(viewLifecycleOwner,{ personDocument ->
            binding.tvPersonName.text = personDocument.name
        })

        viewModel.selectDogProfile().observe(viewLifecycleOwner, {
           dogAdapter.submitList(it)
        })

        setupRecyclerView()
    }

    private fun setupRecyclerView() = binding.rvDogs.apply {
        dogAdapter = DogAdapter()
        adapter = dogAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}