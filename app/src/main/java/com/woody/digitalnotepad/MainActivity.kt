package com.woody.digitalnotepad

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(){
    private lateinit var firebaseWrapper: FirebaseFirestoreWrapper
    private lateinit var updater: RecyclerViewUpdater
    private var recyclerViewChildren = HashMap<Int, Pair<Boolean, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updater = RecyclerViewUpdater()
        firebaseWrapper = FirebaseFirestoreWrapper(this)
        firebaseWrapper.initializeFirebaseFirestore(updater)
        firebaseWrapper.signInExistingFirebaseUsers()
        setContentView(R.layout.activity_main)
        setButtonListeners()
    }
    fun setButtonListeners(){
        val showIdeasButton: Button = findViewById(R.id.show_all_ideas)
        showIdeasButton.setOnClickListener{
            if (firebaseWrapper.userIsSignedIn()) {
                showIdeas(firebaseWrapper.getDb())
            }
        }
        val showTasksButton: Button = findViewById(R.id.show_all_tasks)
        showTasksButton.setOnClickListener{
            if (firebaseWrapper.userIsSignedIn()) {
                showTasks(firebaseWrapper.getDb())
            }
        }

        val deleteTaskButton: Button = findViewById(R.id.delete_task)
        deleteTaskButton.setOnClickListener{
            if (firebaseWrapper.userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val docId = recyclerViewChildren.get(i)!!.second
                        deleteDocument(firebaseWrapper.getDb(), "tasks", docId)
                        break;
                    }
                }
            }
        }

        val deleteIdeaButton: Button = findViewById(R.id.delete_idea)
        deleteIdeaButton.setOnClickListener{
            if (firebaseWrapper.userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val docId = recyclerViewChildren.get(i)!!.second
                        deleteDocument(firebaseWrapper.getDb(), "ideas", docId)
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
            createIdea(firebaseWrapper.getDb(), idea)
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
            createTask(firebaseWrapper.getDb(), task)
            titleTxt.setText("")
            compOnTxt.setText("")
            descTxt.setText("")
        }
    }

    fun deleteDocument(db: FirebaseFirestore, collectionPath: String, documentId: String){
        db.collection(collectionPath).document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot with id: $documentId successfully deleted!")
                updater.updateTextRecyclerView(this, arrayOf("DocumentSnapshot $documentId", " successfully deleted!"))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                updater.updateTextRecyclerView(this,arrayOf("Error deleting document", e.message.toString()))
            }
    }

    fun createIdea(db: FirebaseFirestore, idea: HashMap<String, String>): Task<DocumentReference?> {
        return db.collection("ideas")
            .add(idea)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                updater.updateTextRecyclerView(this,arrayOf("DocumentSnapshot added ","with ID: ${documentReference.id}"))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                updater.updateTextRecyclerView(this,arrayOf("Error adding document", e.message.toString()))
            }
    }

    fun createTask(db: FirebaseFirestore, task: HashMap<String, String>): Task<DocumentReference?> {
        return db.collection("tasks")
            .add(task)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                updater.updateTextRecyclerView(this,arrayOf("DocumentSnapshot added","with ID: ${documentReference.id}"))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                updater.updateTextRecyclerView(this,arrayOf("Error adding document", e.message.toString()))
            }
    }

    fun showIdeas(db: FirebaseFirestore) {
        db.collection("ideas")
            .get()
            .addOnSuccessListener { result ->
                val ideaStrings = ArrayList<String>()
                for (document in result) {
                    val i: Int = result.indexOf(document)
                    recyclerViewChildren.put(i, Pair(false, document.id))
                    ideaStrings.add("DocId: " + document.id)
                    val title = document.get("title") as String
                    recyclerViewChildren.put(i+1, Pair(false, title))
                    ideaStrings.add("Title: $title")
                    val imp = document.get("implemented") as String
                    recyclerViewChildren.put(i+2, Pair(false, imp))
                    ideaStrings.add("Implemented: $imp")
                    val desc = document.get("description") as String
                    recyclerViewChildren.put(i+3, Pair(false, desc))
                    ideaStrings.add("Description: $desc\n")
                }
                updater.updateTextRecyclerView(this,ideaStrings.toTypedArray())
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                updater.updateTextRecyclerView(this, arrayOf(
                    "Error getting documents.",
                    exception.message.toString()
                ))
            }
    }

    fun showTasks(db: FirebaseFirestore) {
        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val taskStrings = ArrayList<String>()
                for (document in result) {
                    val i: Int = result.indexOf(document)
                    val docId = document.id
                    recyclerViewChildren.put(i, Pair(false, docId))
                    taskStrings.add("DocId: $docId")
                    val title = document.get("title") as String
                    recyclerViewChildren.put(i+1, Pair(false, title))
                    taskStrings.add("Title: $title")
                    val fin = document.get("finished") as String
                    recyclerViewChildren.put(i+2, Pair(false,fin))
                    taskStrings.add("Finished: $fin")
                    val steps = document.get("steps") as String
                    recyclerViewChildren.put(i+3, Pair(false, steps))
                    taskStrings.add("Steps: $steps\n")
                }

                updater.updateTextRecyclerView(this,taskStrings.toTypedArray())
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                updater.updateTextRecyclerView(this,arrayOf(
                    "Error getting documents.",
                    exception.message.toString()
                ))
            }
    }
}