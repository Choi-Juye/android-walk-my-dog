package com.chloedewyes.walkmydog.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.Person
import com.chloedewyes.walkmydog.db.User
import com.chloedewyes.walkmydog.repositories.FirestoreRepository
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch


class FirestoreViewModel : ViewModel() {

    var firebaseRepository = FirestoreRepository()
    var personDocument: MutableLiveData<Person> = MutableLiveData()
    var dogDocument: MutableLiveData<List<Dog>> = MutableLiveData()

    fun insertUser(user: User) = viewModelScope.launch {
        firebaseRepository.insertUser(user)
    }

    fun insertPeronProfile(person: Person) = viewModelScope.launch {
        firebaseRepository.insertPeronProfile(person)
    }

    fun insertDogProfile(dog: Dog) = viewModelScope.launch {
        firebaseRepository.insertDogProfile(dog)
    }

    fun selectPersonProfile(): LiveData<Person> {
        firebaseRepository.selectPersonProfile().addSnapshotListener { document, error ->
            if (document != null) {
                val personData = document.toObject<Person>()!!
                personDocument.value = personData
            }
        }
        return personDocument
    }

    fun selectDogProfile(): LiveData<List<Dog>> {
        firebaseRepository.selectDogProfile().addSnapshotListener { document, error ->

            var dogList: MutableList<Dog> = mutableListOf()

            if (document != null) {
                var dogItem = document.toObject<Dog>()
                dogList.add(dogItem!!)
            }
            dogDocument.value = dogList
        }
        return dogDocument
    }
}