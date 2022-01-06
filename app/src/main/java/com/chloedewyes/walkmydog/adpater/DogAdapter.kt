package com.chloedewyes.walkmydog.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloedewyes.walkmydog.databinding.ItemDogBinding
import com.chloedewyes.walkmydog.db.Dog

class DogAdapter: ListAdapter<Dog, DogAdapter.DogViewHolder>(diffCallback) {

    inner class DogViewHolder(private val binding: ItemDogBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dog: Dog){
            binding.dog = dog
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DogViewHolder(
        ItemDogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Dog>() {

            override fun areItemsTheSame(oldItem: Dog, newItem: Dog): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Dog, newItem: Dog): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        }
    }

}