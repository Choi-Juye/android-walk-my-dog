package com.chloedewyes.walkmydog.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.databinding.FragmentCreateAccountBinding
import com.chloedewyes.walkmydog.databinding.FragmentTrackingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.continueLayout.setOnClickListener {
            if (updateUI()){
                createAccount(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            } else {
                Snackbar.make(requireView(), "이메일과 비밀번호를 모두 입력해주세요 :)", Snackbar.LENGTH_SHORT).show()
            }
        }

    }

    private fun createAccount(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("test", "createUserWithEmail:success")
                    findNavController().navigate(R.id.signInFragment)
                } else {
                    Log.w("test", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun updateUI() : Boolean {
        val name = binding.etEmail.text.toString()
        val weight = binding.etPassword.text.toString()
        if (name.isEmpty() || weight.isEmpty()){
            return false
        }
        return true
    }

}