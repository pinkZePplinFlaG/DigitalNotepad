package com.woody.digitalnotepad

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseWrapper: FirebaseFirestoreWrapper
    private lateinit var recUpdater: RecyclerViewUpdater
    private lateinit var recyclerViewChildren: ArrayList<RecyclerItem>
    private lateinit var uiUpdater: UIUpdaterFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseWrapper = FirebaseFirestoreWrapper(this)
        recUpdater = RecyclerViewUpdater()
        firebaseWrapper.initializeFirebaseFirestore(recUpdater)
        firebaseWrapper.signInExistingFirebaseUsers()
        recyclerViewChildren = ArrayList()
        uiUpdater = UIUpdaterFunctions(recUpdater,recyclerViewChildren, this)
        setContentView(R.layout.activity_main)
        setButtonListeners()
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

        val deleteIdeaButton: Button = findViewById(R.id.delete_btn_left)
        setDeleteIdeaButtonListener(deleteIdeaButton)

        val deleteTaskButton: Button = findViewById(R.id.delete_btn_right)
        setDeleteTaskButtonListener(deleteTaskButton)
    }

    fun setDeleteIdeaButtonListener(dIB: Button){
        dIB.setOnClickListener{
            if (firebaseWrapper.userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val item = recyclerViewChildren[i]
                        uiUpdater.deleteDocument(firebaseWrapper.getDb(), "ideas", item.docId)
                        break;
                    }
                }
            }
        }
    }

    fun setDeleteTaskButtonListener(dTB: Button){
        dTB.setOnClickListener{
            if (firebaseWrapper.userIsSignedIn()) {
                val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
                for(child in recyclerView.children ){
                    val linLay: LinearLayout = child as LinearLayout
                    val checkbox = linLay.findViewById<CheckBox>(R.id.checkbox)
                    if(checkbox.isChecked) {
                        val i: Int = recyclerView.children.indexOf(child)
                        val item = recyclerViewChildren[i]
                        uiUpdater.deleteDocument(firebaseWrapper.getDb(), "tasks", item.docId)
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
            uiUpdater.createIdea(firebaseWrapper.getDb(), idea)
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
            uiUpdater.createTask(firebaseWrapper.getDb(), task)
            titleTxt.setText("")
            compOnTxt.setText("")
            descTxt.setText("")
        }
    }
    fun setShowIdeasButtonListener(sIB: Button){
        sIB.setOnClickListener {
            if (firebaseWrapper.userIsSignedIn()) {
                uiUpdater.showIdeas(firebaseWrapper.getDb())
            }
        }
    }
    fun setShowTasksButtonListener(sTB: Button){
        sTB.setOnClickListener {
            if (firebaseWrapper.userIsSignedIn()) {
                uiUpdater.showTasks(firebaseWrapper.getDb())
            }
        }
    }
}