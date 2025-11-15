package com.woody.digitalnotepad

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class MainActivity : AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var recyclerViewChildren = HashMap<Int, Pair<Boolean, String>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeFirebaseFirestoreDb()
        setContentView(R.layout.activity_main)
        //setButtonListeners()
        signInExistingFirebaseUsers()
    }

    fun updateTextRecyclerView(messages: Array<String>) {
        val textRecyclerView: RecyclerView = findViewById(R.id.recycler_view)
        textRecyclerView.adapter = TextRecyclerViewAdapter(messages.toList())
    }

    fun setButtonListeners(){
        val showIdeasButton: Button = findViewById(R.id.show_all_ideas)
        showIdeasButton.setOnClickListener{
            if (userIsSignedIn()) {
                //showIdeas(db)
            }
        }
        val showTasksButton: Button = findViewById(R.id.show_all_tasks)
        showTasksButton.setOnClickListener{
            if (userIsSignedIn()) {
                //showTasks(db)
            }
        }
        val deleteTaskButton: Button = findViewById(R.id.delete_task)
        deleteTaskButton.setOnClickListener{
            if (userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
//                    Log.d(TAG, checkbox.isChecked.toString())
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val docId = recyclerViewChildren.get(i)!!.second
                        //deleteDocument(db, "tasks", docId)
                        break;
                    }
                }
            }
        }
        val deleteIdeaButton: Button = findViewById(R.id.delete_idea)
        deleteIdeaButton.setOnClickListener{
            if (userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
//                    Log.d(TAG, checkbox.isChecked.toString())
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val docId = recyclerViewChildren.get(i)!!.second
                        //deleteDocument(db, "ideas", docId)
                        break;
                    }
                }
            }
        }

        val createIdeaButton: Button = findViewById(R.id.create_idea)
        createIdeaButton.setOnClickListener {
            val titleTxt = findViewById<EditText>(R.id.create_title_txt)
            val compOnTxt = findViewById<EditText>(R.id.create_completed_on_txt)
            val descTxt = findViewById<EditText>(R.id.create_description_txt)
            val idea = hashMapOf(
                "title" to titleTxt.text.toString(),
                "implemented" to compOnTxt.text.toString(),
                "description" to descTxt.text.toString(),
            )
            //createIdea(db, idea)
            titleTxt.setText("")
            compOnTxt.setText("")
            descTxt.setText("")
        }

        val createTaskButton: Button = findViewById(R.id.create_task)
        createTaskButton.setOnClickListener {
            val titleTxt = findViewById<EditText>(R.id.create_title_txt)
            val compOnTxt = findViewById<EditText>(R.id.create_completed_on_txt)
            val descTxt = findViewById<EditText>(R.id.create_description_txt)
            val task = hashMapOf(
                "title" to titleTxt.text.toString(),
                "finished" to compOnTxt.text.toString(),
                "steps" to descTxt.text.toString(),
            )
            //createTask(db, task)
            titleTxt.setText("")
            compOnTxt.setText("")
            descTxt.setText("")
        }
    }

//    fun deleteDocument(db: FirebaseFirestore, collectionPath: String, documentId: String){
//        db.collection(collectionPath).document(documentId)
//            .delete()
//            .addOnSuccessListener {
//                Log.d(TAG, "DocumentSnapshot with id: "+ documentId +" successfully deleted!")
//                updateTextRecyclerView(arrayOf("DocumentSnapshot "+documentId, " successfully deleted!"))
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error deleting document", e)
//                updateTextRecyclerView(arrayOf("Error deleting document", e.message.toString()))
//            }
//    }
//
//    fun createIdea(db: FirebaseFirestore, idea: HashMap<String, String>): Task<DocumentReference?> {
//        return db.collection("ideas")
//            .add(idea)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                updateTextRecyclerView(arrayOf("DocumentSnapshot added ","with ID: ${documentReference.id}"))
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//                updateTextRecyclerView(arrayOf("Error adding document", e.message.toString()))
//            }
//    }
//
//    fun createTask(db: FirebaseFirestore, task: HashMap<String, String>): Task<DocumentReference?> {
//        return db.collection("tasks")
//            .add(task)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                updateTextRecyclerView(arrayOf("DocumentSnapshot added","with ID: ${documentReference.id}"))
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//                updateTextRecyclerView(arrayOf("Error adding document", e.message.toString()))
//            }
//    }
//
//    fun showIdeas(db: FirebaseFirestore) {
//        db.collection("ideas")
//            .get()
//            .addOnSuccessListener { result ->
//                val ideaStrings = ArrayList<String>()
//                for (document in result) {
//                    val i: Int = result.indexOf(document)
//                    recyclerViewChildren.put(i, Pair(false, document.id))
//                    ideaStrings.add("DocId: " + document.id)
//                    val title = document.get("title") as String
//                    recyclerViewChildren.put(i+1, Pair(false, title))
//                    ideaStrings.add("Title: $title")
//                    val imp = document.get("implemented") as String
//                    recyclerViewChildren.put(i+2, Pair(false, imp))
//                    ideaStrings.add("Implemented: $imp")
//                    val desc = document.get("description") as String
//                    recyclerViewChildren.put(i+3, Pair(false, desc))
//                    ideaStrings.add("Description: $desc\n")
//                }
//                updateTextRecyclerView(ideaStrings.toTypedArray())
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//                updateTextRecyclerView( arrayOf(
//                    "Error getting documents.",
//                    exception.message.toString()
//                ))
//            }
//    }

//    fun showTasks(db: FirebaseFirestore) {
//        db.collection("tasks")
//            .get()
//            .addOnSuccessListener { result ->
//                val taskStrings = ArrayList<String>()
//                for (document in result) {
//                    val i: Int = result.indexOf(document)
//                    val docId = document.id
//                    recyclerViewChildren.put(i, Pair(false, docId))
//                    taskStrings.add("DocId: $docId")
//                    val title = document.get("title") as String
//                    recyclerViewChildren.put(i+1, Pair(false, title))
//                    taskStrings.add("Title: $title")
//                    val fin = document.get("finished") as String
//                    recyclerViewChildren.put(i+2, Pair(false,fin))
//                    taskStrings.add("Finished: $fin")
//                    val steps = document.get("steps") as String
//                    recyclerViewChildren.put(i+3, Pair(false, steps))
//                    taskStrings.add("Steps: $steps\n")
//                }
//
//                updateTextRecyclerView(taskStrings.toTypedArray())
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//                updateTextRecyclerView(arrayOf(
//                    "Error getting documents.",
//                    exception.message.toString()
//                ))
//            }
//
//    }

    fun initializeFirebaseFirestoreDb(){
        FirebaseApp.initializeApp(this)
        db = Firebase.firestore
        auth = Firebase.auth
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

    fun signInExistingFirebaseUsers(){
        val email = getString(R.string.firestore_email)
        val password = getString(R.string.firestore_password)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "Signed in to Firestore!")
                    //updateTextRecyclerView(arrayOf("Signed in to Firestore!"))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "Failed to sign in to Firestore!", task.exception)
                   // updateTextRecyclerView(arrayOf( "Failed to sign in to Firestore!"))
                }
            }
    }

    fun createNewUserWithEmailAndPassword(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    //updateTextRecyclerView(arrayOf("Authentication failed."))
                }
            }
    }

    fun userIsSignedIn():Boolean{
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        return currentUser != null
    }
}