package com.chloedewyes.walkmydog.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.Person
import com.chloedewyes.walkmydog.db.User
import com.chloedewyes.walkmydog.repositories.FirestoreRepository


class FirestoreViewModel : ViewModel(){

    var firebaseRepository = FirestoreRepository()

    fun insertUser(user: User){
        firebaseRepository.insertUser(user)
    }

    fun insertPeronProfile(person: Person) {
        firebaseRepository.insertPeronProfile(person)
    }

    fun insertDogProfile(dog: Dog) {
        firebaseRepository.insertDogProfile(dog)
    }

    /*
    fun selectPersonProfile(name: String){
        firebaseRepository.selectPersonProfile(name)
    }*/
}