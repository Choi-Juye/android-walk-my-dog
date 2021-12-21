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
import com.chloedewyes.walkmydog.databinding.FragmentProfilePersonBinding
import com.chloedewyes.walkmydog.db.Person
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel
import com.google.android.material.snackbar.Snackbar

class ProfilePersonFragment: Fragment(R.layout.fragment_profile_person) {

    private var _binding: FragmentProfilePersonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    private var genderIs: MutableLiveData<String> = MutableLiveData<String>()
    private var isGender: MutableLiveData<Boolean> = MutableLiveData()


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
        isGender.postValue(false)

        binding.tvPersonGenderMale.setOnClickListener {
            isGender.postValue(true)
            genderIs.postValue("남")
        }
        binding.tvPersonGenderFemale.setOnClickListener {
            isGender.postValue(true)
            genderIs.postValue("여")
        }

        genderIs.observe(viewLifecycleOwner,{ isGender ->
            updateGender(isGender)

            binding.continueBtn.setOnClickListener {
                if (updateUI()){
                    val person = Person(binding.etPersonName.text.toString(), isGender)
                    viewModel.insertPeronProfile(person)
                    findNavController().navigate(R.id.action_profilePersonFragment_to_profileDogFragment)
                } else {
                    Snackbar.make(requireView(), "프로필을 모두 작성해주세요 :)", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun updateGender(genderIs: String){
        if (genderIs == "남"){
            binding.tvPersonGenderMale.setBackgroundResource(R.drawable.theme_radius)
            binding.tvPersonGenderFemale.setBackgroundResource(R.drawable.grey_radius)
        } else {
            binding.tvPersonGenderMale.setBackgroundResource(R.drawable.grey_radius)
            binding.tvPersonGenderFemale.setBackgroundResource(R.drawable.theme_radius)
        }
    }

    private fun updateUI() : Boolean {
        val name = binding.etPersonName.text.toString()
        if (name.isEmpty() || isGender.value == false){
            return false
        }
        return true
    }


}