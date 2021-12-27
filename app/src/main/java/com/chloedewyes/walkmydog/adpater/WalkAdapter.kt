package com.chloedewyes.walkmydog.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chloedewyes.walkmydog.databinding.ItemWalkBinding
import com.chloedewyes.walkmydog.db.Walk
import com.chloedewyes.walkmydog.repositories.FirestoreRepository

class WalkAdapter: ListAdapter<Walk, WalkAdapter.WalkViewHolder>(diffCallback) {

    var firebaseRepository = FirestoreRepository()

    inner class WalkViewHolder(private val binding: ItemWalkBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(walk: Walk){
            binding.walk = walk

            firebaseRepository.mapReference(walk.mapId).downloadUrl.addOnSuccessListener { url ->
                Glide.with(binding.ivMapImage)
                    .load(url)
                    .into(binding.ivMapImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WalkViewHolder(
       ItemWalkBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,
           false
       )
    )

    override fun onBindViewHolder(holder: WalkAdapter.WalkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Walk>() {

            override fun areItemsTheSame(oldItem: Walk, newItem: Walk): Boolean {
                return oldItem.mapId == newItem.mapId
            }

            override fun areContentsTheSame(oldItem: Walk, newItem: Walk): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    }

}