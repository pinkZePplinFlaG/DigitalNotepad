package com.woody.digitalnotepad

import android.content.ContentValues.TAG
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getString
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FirebaseFirestoreWrapper(private var mainContext: AppCompatActivity) {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var updater: RecyclerViewUpdater

    fun initializeFirebaseFirestore( updater: RecyclerViewUpdater){
        FirebaseApp.initializeApp(mainContext)
        this.updater = updater
        this.db = Firebase.firestore
        this.auth = Firebase.auth
    }

    fun accessUserInformation(): ArrayList<String> {
        val user = auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = "name: "+ it.displayName
            val email = "email: " + it.email
            val photoUrl = "photoUrl: " + it.photoUrl

            // Check if user's email is verified
            val emailVerified =  "emailVerified: " + it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid =  "userId: " + it.uid
            return arrayListOf(uid, email, emailVerified)
        }
        return arrayListOf("no users signed in")
    }

    fun signInExistingFirebaseUsers(): Task<AuthResult?> {
        val email = getString(mainContext, R.string.firestore_email)
        val password = getString(mainContext, R.string.firestore_password)
        return this.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(mainContext) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Signed in to Firestore!")
                    updater.updateTextRecyclerView(mainContext,arrayOf("Signed in to Firestore!"))
                } else {
                    Log.w(TAG, "Failed to sign in to Firebase!", task.exception)
                    updater.updateTextRecyclerView(mainContext,arrayOf( "Failed to sign in to Firebase!"))
                }
            }
    }

    fun userIsSignedIn():Boolean{
        return auth.currentUser != null
    }

    fun getDb(): FirebaseFirestore{ return this.db }

    fun getAuth(): FirebaseAuth{ return this.auth }

    fun createNewUserWithEmailAndPassword(email: String, password: String){
        this.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(mainContext) { task ->
                if (task.isSuccessful) {
                    updater.updateTextRecyclerView(mainContext,arrayOf("Successfully created new user with email: $email"))
                } else {
                    updater.updateTextRecyclerView(mainContext,arrayOf("Authentication failed."))
                }
            }
    }
}