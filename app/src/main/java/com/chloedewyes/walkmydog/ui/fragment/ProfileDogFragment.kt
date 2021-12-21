package com.chloedewyes.walkmydog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.databinding.FragmentProfileDogBinding
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel
import com.google.android.material.snackbar.Snackbar

class ProfileDogFragment: Fragment(R.layout.fragment_profile_dog) {

    private var _binding: FragmentProfileDogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    private var genderIs: MutableLiveData<String> = MutableLiveData()
    private var isGender: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isGender.postValue(false)

        binding.tvDogGenderMale.setOnClickListener {
            isGender.postValue(true)
            genderIs.postValue("남")
        }
        binding.tvDogGenderFemale.setOnClickListener {
            isGender.postValue(true)
            genderIs.postValue("여")
        }

        genderIs.observe(viewLifecycleOwner,{ isGender ->
            updateGender(isGender)

            binding.continueBtn.setOnClickListener {
                if (updateUI()){
                    val dog = Dog(binding.etDogName.text.toString(), isGender, binding.etDogAge.text.toString(), binding.etDogWeight.text.toString())
                    viewModel.insertDogProfile(dog)
                    findNavController().navigate(R.id.trackingFragment)
                }else {
                    Snackbar.make(requireView(), "프로필을 모두 작성해주세요 :)", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun updateGender(genderIs: String){
        if (genderIs=="남"){
            binding.tvDogGenderMale.setBackgroundResource(R.drawable.theme_radius)
            binding.tvDogGenderFemale.setBackgroundResource(R.drawable.grey_radius)
        } else {
            binding.tvDogGenderMale.setBackgroundResource(R.drawable.grey_radius)
            binding.tvDogGenderFemale.setBackgroundResource(R.drawable.theme_radius)
        }
    }

    private fun updateUI() : Boolean {
        val name = binding.etDogName.text.toString()
        val age = binding.etDogAge.text.toString()
        val weight = binding.etDogWeight.text.toString()
        if (name.isEmpty() || age.isEmpty() || weight.isEmpty()|| isGender.value == false){
            return false
        }
        return true
    }
}