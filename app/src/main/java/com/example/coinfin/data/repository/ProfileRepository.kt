package com.example.coinfin.data.repository

import com.google.firebase.firestore.FirebaseFirestore

data class UserProfile(
    val uid: String,
    val savingsGoal: Int,
    val resetDay: Int,
    val avoidCategories: List<String>
)

object ProfileRepository {

    private val db = FirebaseFirestore.getInstance()

    fun saveUserProfile(profile: UserProfile, onResult: (Boolean) -> Unit) {
        db.collection("profiles")
            .document(profile.uid)
            .set(profile)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun getUserProfile(uid: String, onResult: (UserProfile?) -> Unit) {
        db.collection("profiles")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->
                document.toObject(UserProfile::class.java)?.let { onResult(it) }
            }
            .addOnFailureListener { onResult(null) }
    }
}
