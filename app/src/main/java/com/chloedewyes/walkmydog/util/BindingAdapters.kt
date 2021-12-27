package com.chloedewyes.walkmydog.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chloedewyes.walkmydog.adpater.DogAdapter
import com.chloedewyes.walkmydog.adpater.WalkAdapter
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.Walk

@BindingAdapter("app:bindDogDocument")
fun bindDogDocument(recyclerView: RecyclerView, dogDocument: List<Dog>?) {
    val adapter = recyclerView.adapter as? DogAdapter
    adapter?.submitList(dogDocument)
}

@BindingAdapter("app:bindWalkDocument")
fun bindWalkDocument(recyclerView: RecyclerView, walkDocument: List<Walk>?) {
    val adapter = recyclerView.adapter as? WalkAdapter
    adapter?.submitList(walkDocument)
}

