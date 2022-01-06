package com.chloedewyes.walkmydog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chloedewyes.walkmydog.R
import com.chloedewyes.walkmydog.adpater.DogAdapter
import com.chloedewyes.walkmydog.adpater.WalkAdapter
import com.chloedewyes.walkmydog.databinding.FragmentJournalBinding
import com.chloedewyes.walkmydog.ui.viewmodels.FirestoreViewModel
import com.google.android.material.snackbar.Snackbar

class JournalFragment : Fragment() {

    private var _binding: FragmentJournalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FirestoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_journal, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.selectWalk()
        binding.rvWalks.adapter = WalkAdapter()


    }

}