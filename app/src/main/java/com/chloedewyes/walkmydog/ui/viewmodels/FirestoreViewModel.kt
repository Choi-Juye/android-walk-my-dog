package com.chloedewyes.walkmydog.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.User
import com.chloedewyes.walkmydog.db.Walk
import com.chloedewyes.walkmydog.repositories.FirestoreRepository
import com.google.firebase.firestore.ktx.toObject
import java.io.ByteArrayOutputStream

class FirestoreViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()

    private val _walkDocument: MutableLiveData<List<Walk>> = MutableLiveData()
    val walkDocument: LiveData<List<Walk>> = _walkDocument

    private val _dogDocument: MutableLiveData<List<Dog>> = MutableLiveData()
    val dogDocument: LiveData<List<Dog>> = _dogDocument

    private val _userName = MutableLiveData("Name")
    val userName: LiveData<String> = _userName

    private val _mapUrl = MutableLiveData("url")
    val mapUrl: LiveData<String> = _mapUrl


    fun insertUser(user: User) {
        firebaseRepository.upsertUser().set(user)
    }

    fun selectUser() {
        firebaseRepository.upsertUser().addSnapshotListener { document, error ->
            if (document != null) {
                val userData = document.toObject<User>()!!
                _userName.value = userData.name
            }
        }
    }

    fun updateUser(name: String) {
        firebaseRepository.upsertUser().update("name", name)
    }

    fun insertDog(dog: Dog) {
        firebaseRepository.upsertDog().add(dog)
    }

    fun selectDog() {
        firebaseRepository.upsertDog().get()
            .addOnSuccessListener { result ->
                var dogList: MutableList<Dog> = mutableListOf()

                if (result == null) {
                    _dogDocument.value = null
                } else {
                    for (document in result) {
                        var dogData = document.toObject<Dog>()
                        dogList.add(dogData!!)
                    }
                    _dogDocument.value = dogList
                }
            }
    }

    fun insertWalk(walk: Walk) {
        firebaseRepository.upsertWalk().add(walk)
    }

    fun selectWalk() {
        firebaseRepository.upsertWalk().get()
            .addOnSuccessListener { result ->
                var walkList: MutableList<Walk> = mutableListOf()

                if (result == null) {
                    _walkDocument.value = null
                } else {
                    for (document in result) {
                        var walkData = document.toObject<Walk>()
                        walkList.add(walkData!!)
                    }
                    _walkDocument.value = walkList
                }
                Log.d("test", "${walkDocument.value}")
            }
    }


    fun insertMap(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val mapData = outputStream.toByteArray()
        firebaseRepository.storageMap(bitmap.toString()).putBytes(mapData)
    }
}