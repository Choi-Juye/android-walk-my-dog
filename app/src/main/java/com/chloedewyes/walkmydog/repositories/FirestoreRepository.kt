package com.chloedewyes.walkmydog.repositories


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreRepository {

    var db = FirebaseFirestore.getInstance()
    var auth = FirebaseAuth.getInstance()
    var storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    private val uid = auth.uid

    fun upsertUser(): DocumentReference {
        return db.collection("users").document("$uid")
    }

    fun upsertDog(): CollectionReference {
        return db.collection("users/$uid/dog")
    }

    fun upsertWalk(): CollectionReference {
        return db.collection("users/$uid/walk")
    }

    fun storageMap(mapId: String): StorageReference {
        return storageRef.child("$uid/mapImages/$mapId")
    }
}