package com.woody.digitalnotepad

import android.os.Bundle
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var firebase: FirebaseFirestoreWrapper
    private lateinit var recUpdater: RecyclerViewUpdater
    private lateinit var recyclerViewChildren: RecyclerViewChildren
    private lateinit var uiUpdater: UtilityFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        initializeFirebase()
        initUIUpdater()
        setContentView(R.layout.activity_main)
        setButtonListeners()
    }
    fun initRecyclerView(){
        recyclerViewChildren = RecyclerViewChildren()
        recUpdater = RecyclerViewUpdater()
    }
    fun initUIUpdater(){
        uiUpdater = UtilityFunctions(recUpdater, this)
    }
    fun initializeFirebase(){
        firebase = FirebaseFirestoreWrapper(this)
        firebase.initializeFirebaseFirestore(recUpdater)
        firebase.signInExistingFirebaseUsers()
    }
    fun setButtonListeners() {
        val showIdeasButton: Button = findViewById(R.id.show_btn_left)
        setShowIdeasButtonListener(showIdeasButton)

        val showTasksButton: Button = findViewById(R.id.show_btn_right)
        setShowTasksButtonListener(showTasksButton)

        val createIdeaButton: Button = findViewById(R.id.create_btn_left)
        setCreateIdeaButtonListener(createIdeaButton)

        val createTaskButton: Button = findViewById(R.id.create_btn_right)
        setCreateTaskButtonListener(createTaskButton)

        val deleteButton: Button = findViewById(R.id.delete_btn)
        setDeleteButtonListener(deleteButton)

    }

    fun setDeleteButtonListener(dIB: Button){
        dIB.setOnClickListener{
            if (firebase.userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val item = recyclerViewChildren.getChild(i)
                        Log.w(TAG,item.docId )
                        uiUpdater.deleteDocument(firebase.getDb(), item.collectionPath, item.docId)
                        break;
                    }
                }
            }
        }
    }
    fun setCreateIdeaButtonListener(cIB: Button){
        cIB.setOnClickListener {
            val titleTxt = findViewById<EditText>(R.id.create_title_txt)
            val compOnTxt = findViewById<EditText>(R.id.create_completed_on_txt)
            val descTxt = findViewById<EditText>(R.id.create_description_txt)
            val idea = hashMapOf(
                "title" to titleTxt.text.toString(),
                "implemented" to compOnTxt.text.toString(),
                "description" to descTxt.text.toString(),
            )
            uiUpdater.createIdea(firebase.getDb(), idea)
            titleTxt.setText("")
            compOnTxt.setText("")
            descTxt.setText("")
        }
    }

    fun setCreateTaskButtonListener(cTB: Button){
        cTB.setOnClickListener {
            val titleTxt = findViewById<EditText>(R.id.create_title_txt)
            val compOnTxt = findViewById<EditText>(R.id.create_completed_on_txt)
            val descTxt = findViewById<EditText>(R.id.create_description_txt)
            val task = hashMapOf(
                "title" to titleTxt.text.toString(),
                "finished" to compOnTxt.text.toString(),
                "steps" to descTxt.text.toString(),
            )
            uiUpdater.createTask(firebase.getDb(), task)
            titleTxt.setText("")
            compOnTxt.setText("")
            descTxt.setText("")
        }
    }
    fun setShowIdeasButtonListener(sIB: Button){
        sIB.setOnClickListener {
            if (firebase.userIsSignedIn()) {
                recyclerViewChildren.clearChildren()
                uiUpdater.showIdeas(firebase.getDb(), recyclerViewChildren)
            }
        }
    }
    fun setShowTasksButtonListener(sTB: Button){
        sTB.setOnClickListener {
            if (firebase.userIsSignedIn()) {
                recyclerViewChildren.clearChildren()
                uiUpdater.showTasks(firebase.getDb(), recyclerViewChildren)
            }
        }
    }
}