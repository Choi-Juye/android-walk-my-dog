package com.chloedewyes.walkmydog.repositories

import androidx.lifecycle.MutableLiveData
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.Person
import com.chloedewyes.walkmydog.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    private val uid = auth.uid

    //var personData : MutableMap<String, Any> = HashMap()

    //var personData : MutableLiveData<HashMap<String, Any>> = MutableLiveData<HashMap<String, Any>>()

    fun insertUser(user: User) {
        db.collection("users").document(user.uid!!).set(user)
    }

    fun insertPeronProfile(person: Person) {
        db.collection("users/$uid/person").document(person.name!!).set(person)
    }

    fun insertDogProfile(dog: Dog) {
        db.collection("users/$uid/dog").document(dog.name!!).set(dog)
    }

    /*
    fun selectPersonProfile(name: String){
        db.collection("users/$uid/person").document(name).get()
            .addOnSuccessListener { document ->
                if (document != null){
                    personData = document.data as MutableMap<String, Any>
                }
            }

    }

    fun selectDogProfile(dog: Dog){
        db.collection("users/$uid/dog").document(dog.name!!).get()
    }*/

}