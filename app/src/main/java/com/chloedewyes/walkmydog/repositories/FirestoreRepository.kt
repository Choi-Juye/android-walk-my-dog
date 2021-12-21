package com.chloedewyes.walkmydog.repositories

import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.Person
import com.chloedewyes.walkmydog.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    private val uid = auth.uid

    fun insertUser(user: User) {
        db.collection("users").document("$uid").set(user)
    }

    fun insertPeronProfile(person: Person) {
        db.collection("users/$uid/person").document("$uid").set(person)
    }

    fun insertDogProfile(dog: Dog) {
        db.collection("users/$uid/dog").document("$uid").set(dog)
    }

    fun selectPersonProfile(): DocumentReference {
        return db.collection("users/$uid/person").document("$uid")
    }

    fun selectDogProfile(): DocumentReference {
       return db.collection("users/$uid/dog").document("$uid")
    }

}