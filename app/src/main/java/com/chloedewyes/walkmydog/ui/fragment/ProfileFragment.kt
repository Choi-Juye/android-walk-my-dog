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

        setupRecyclerView()

        binding.tvSignOut.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().navigate(R.id.signInFragment)
        }

        binding.ivEditProfile.setOnClickListener {
            findNavController().navigate(R.id.profilePersonFragment)
        }

        binding.btnAddDog.setOnClickListener {
            findNavController().navigate(R.id.profileDogFragment)
        }

        viewModel.selectUser().observe(viewLifecycleOwner, { userDocument ->
            binding.tvPersonName.text = userDocument.name
        })

        viewModel.selectDog().observe(viewLifecycleOwner, { dogDocument ->
            if (dogDocument != null) {
                binding.rvDogs.visibility = View.VISIBLE
                dogAdapter.submitList(dogDocument)
            }
        })

    }


    private fun setupRecyclerView() = binding.rvDogs.apply {
        dogAdapter = DogAdapter()
        adapter = dogAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }
}