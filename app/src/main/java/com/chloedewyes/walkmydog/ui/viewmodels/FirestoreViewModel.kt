package com.chloedewyes.walkmydog.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.chloedewyes.walkmydog.repositories.FirestoreRepository


class FirestoreViewModel : ViewModel(){

    var firebaseRepository = FirestoreRepository()

    fun insertUser(uid: String){
        firebaseRepository.insertUser(uid)
    }

}