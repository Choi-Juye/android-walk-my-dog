package com.chloedewyes.walkmydog.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.User
import com.chloedewyes.walkmydog.db.Walk
import com.chloedewyes.walkmydog.repositories.FirestoreRepository
import com.google.firebase.firestore.ktx.toObject

class FirestoreViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()

    var walkDocument: MutableLiveData<List<Walk>> = MutableLiveData()

    var dogDocument: MutableLiveData<List<Dog>> = MutableLiveData()
    var userDocument: MutableLiveData<User> = MutableLiveData()

    fun insertUser(user: User) {
        firebaseRepository.upsertUser().set(user)
    }

    fun updateUser(name: String) {
        firebaseRepository.upsertUser().update("name", name)
    }

    fun selectUser(): LiveData<User> {
        firebaseRepository.upsertUser().addSnapshotListener { document, error ->
            if (document != null) {
                val userData = document.toObject<User>()!!
                userDocument.value = userData
            }
        }
        return userDocument
    }

    fun insertDog(dog: Dog) {
        firebaseRepository.upsertDog().add(dog)
    }

    fun selectDog(): LiveData<List<Dog>>{
        firebaseRepository.upsertDog().get()
            .addOnSuccessListener { result ->
                var dogList: MutableList<Dog> = mutableListOf()

                if (result == null){
                    dogDocument.value = null
                } else {
                    for (document in result){
                        var dogData = document.toObject<Dog>()
                        dogList.add(dogData!!)
                        //Log.d("test", "${document.id} => ${document.data}")
                    }
                    dogDocument.value = dogList
                }
            }
        return dogDocument
    }

/*
    fun insertMap(bitmap: Bitmap) {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val mapData = outputStream.toByteArray()

        firebaseRepository.storageMap(bitmap.toString()).putBytes(mapData)

    }

    fun selectMap(mapId: String) {
        val ONE_MEGABYTE: Long = 1024 * 1024
        firebaseRepository.storageMap(mapId).getBytes(ONE_MEGABYTE)
            .addOnSuccessListener {

            }
    }

    fun insertWalk(walk: Walk) = viewModelScope.launch {
        firebaseRepository.insertWalk(walk)
    }

    fun selectWalk(): LiveData<List<Walk>>{
        firebaseRepository.selectWalk("android.graphics.Bitmap@57f8158").addSnapshotListener { document, error ->

            var walkList: MutableList<Walk> = mutableListOf()

            if (document != null){
                var walkItem = document.toObject<Walk>()
                walkList.add(walkItem!!)
            }
            walkDocument.value = walkList
        }
        return walkDocument
    }
*/


}