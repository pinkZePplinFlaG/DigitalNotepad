package com.woody.digitalnotepad

import android.content.ContentValues.TAG
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class UtilityFunctions(
    private var recUpdater: RecyclerViewUpdater,
    private var mainContext: AppCompatActivity
) {
    fun showIdeas(db: FirebaseFirestore, recyclerViewChildren: RecyclerViewChildren) {
        db.collection("ideas")
            .get()
            .addOnSuccessListener { result ->
                val ideaItems = ArrayList<RecyclerItem>()

                for (document in result) {
                    val i: Int = result.indexOf(document)
                    val docId = document.id
                    val title = document.get("title") as String
                    val imp = document.get("implemented") as String
                    val desc = document.get("description") as String
                    val ideaItem = RecyclerItem(i,"ideas","", docId,title,"", imp, desc)
                    recyclerViewChildren.addChild(ideaItem)
                    ideaItems.add(ideaItem)
                }
                recUpdater.updateRecyclerView(mainContext,ideaItems)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","Error getting documents.","","","","","")))
            }
    }

    fun showTasks(db: FirebaseFirestore, recyclerViewChildren: RecyclerViewChildren) {
        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val taskItems = ArrayList<RecyclerItem>()
                for (document in result) {
                    val i: Int = result.indexOf(document)
                    val docId = document.id
                    val title = document.get("title") as String
                    val fin = document.get("finished") as String
                    val steps = document.get("steps") as String
                    val taskItem = RecyclerItem(i,"tasks","",docId, title,"",fin, steps )
                    recyclerViewChildren.addChild(taskItem)
                    taskItems.add(taskItem)
                }

                recUpdater.updateRecyclerView(mainContext,taskItems)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","Error getting documents.","","","","","")))
            }
    }
    fun createIdea(db: FirebaseFirestore, idea: HashMap<String, String>): Task<DocumentReference?> {
        return db.collection("ideas")
            .add(idea)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"ideas","","","DocumentSnapshot added with ID: "+documentReference.id, "","","")))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","Error adding document.","","","","","")))
            }
    }

    fun createTask(db: FirebaseFirestore, task: HashMap<String, String>): Task<DocumentReference?> {
        return db.collection("tasks")
            .add(task)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","DocumentSnapshot added", documentReference.id, "","","","")))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","Error adding document.","","","","","")))
            }
    }
    fun deleteDocument(db: FirebaseFirestore, collectionPath: String, documentId: String){
        db.collection(collectionPath).document(documentId)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot with id: $documentId successfully deleted!")
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","","","DocumentSnapshot with id: $documentId successfully deleted!", "","","")))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
                recUpdater.updateRecyclerView(mainContext,arrayListOf(RecyclerItem(0,"","","","Error deleting document", "","","")))
            }
    }
}