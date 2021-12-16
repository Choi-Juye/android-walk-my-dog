package com.chloedewyes.walkmydog.repositories

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    var db = FirebaseFirestore.getInstance()

    fun insertUser(uid: String) {

        val user = hashMapOf(
            "uid" to uid
        )

        db.collection("users").document(uid).set(user)
    }

}