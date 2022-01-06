package com.chloedewyes.walkmydog.ui.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.User
import com.chloedewyes.walkmydog.db.Walk
import com.chloedewyes.walkmydog.repositories.FirestoreRepository
import com.google.firebase.firestore.ktx.toObject
import java.io.ByteArrayOutputStream

class FirestoreViewModel : ViewModel() {
    var firebaseRepository = FirestoreRepository()

    private val _userName = MutableLiveData("Name")
    private val _dogDocument: MutableLiveData<List<Dog>> = MutableLiveData()
    private val _walkDocument: MutableLiveData<List<Walk>> = MutableLiveData()

    val userName: LiveData<String> = _userName
    val dogDocument: LiveData<List<Dog>> = _dogDocument
    val walkDocument: LiveData<List<Walk>> = _walkDocument


    fun insertUser(user: User) {
        firebaseRepository.userReference().set(user)
    }

    fun insertDog(dog: Dog) {
        firebaseRepository.dogReference().add(dog)
    }

    fun insertWalk(walk: Walk) {
        firebaseRepository.walkReference().add(walk)
    }

    fun insertMap(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val mapData = outputStream.toByteArray()
        firebaseRepository.mapReference(bitmap.toString()).putBytes(mapData)
    }

    fun updateUser(name: String) {
        firebaseRepository.userReference().update("name", name)
    }

    fun selectUser() {
        firebaseRepository.userReference().addSnapshotListener { document, error ->
            if (document != null) {
                val userData = document.toObject<User>()!!
                _userName.value = userData.name
            }
        }
    }

    fun selectDog() {
        firebaseRepository.dogReference().get()
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

    fun selectWalk() {
        firebaseRepository.walkReference().get()
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
}