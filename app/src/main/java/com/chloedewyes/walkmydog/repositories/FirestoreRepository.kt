package com.chloedewyes.walkmydog.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chloedewyes.walkmydog.db.Dog
import com.chloedewyes.walkmydog.db.Person
import com.chloedewyes.walkmydog.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class FirestoreRepository {

    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    private val uid = auth.uid

    fun insertUser(user: User) {
        db.collection("users").document(user.uid!!).set(user)
    }

    fun insertPeronProfile(person: Person) {
        db.collection("users/$uid/person").document("$uid").set(person)
    }

    fun insertDogProfile(dog: Dog) {
        db.collection("users/$uid/dog").document("$uid").set(dog)
    }


    fun selectPersonProfile(){
        db.collection("users/$uid/person").document("$uid").get()
            .addOnSuccessListener { document ->
                if (document != null){
                    val personDocument = document.toObject<Person>()!!
                    Log.d("test", "DocumentSnapshot person: $personDocument")
                }
            }

    }

}