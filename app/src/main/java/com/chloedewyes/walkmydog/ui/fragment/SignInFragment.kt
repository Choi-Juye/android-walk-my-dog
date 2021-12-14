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
import com.chloedewyes.walkmydog.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.continueLayout.setOnClickListener {
            if (updateUI()){
                signIn(binding.etEmail.text.toString(), binding.etPassword.text.toString())
            } else {
                Snackbar.make(requireView(), "이메일과 비밀번호를 모두 입력해주세요 :)", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(
                R.id.action_signInFragment_to_createAccountFragment
            )
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("test", "signInWithEmail:success")
                    findNavController().navigate(
                        R.id.action_signInFragment_to_trackingFragment
                    )

                } else {
                    Snackbar.make(requireView(), "로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요 :(", Snackbar.LENGTH_SHORT).show()
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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            findNavController().navigate(
                R.id.action_signInFragment_to_trackingFragment
            )
        }
    }
}