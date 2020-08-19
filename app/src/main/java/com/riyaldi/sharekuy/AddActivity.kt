package com.riyaldi.sharekuy

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.riyaldi.sharekuy.utils.Firebase.COURSES_PATH_COLLECTION
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(COURSES_PATH_COLLECTION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    fun initView() {
        btSaveAddCourses.setOnClickListener {
            val shareanCourse = initData()
            saveData(shareanCourse)
        }
    }

    private fun saveData(shareanCourse : MutableMap<String, Any>) = CoroutineScope(Dispatchers.IO).launch {
        shareanCourseCollection.document(setIdByTime()).set(shareanCourse)
            .addOnCompleteListener {
                if (it.isSuccessful) Toast.makeText(this@AddActivity, "Berhasil menambahkan ${shareanCourse["courseName"]}, status = \"pending\".", Toast.LENGTH_LONG).show()
                else Toast.makeText(this@AddActivity, "Gagal menambahkan ${shareanCourse["courseName"]}, ulangi beberapa menit lagi.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@AddActivity, "Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun initData() : MutableMap<String, Any> {
        val courseName = etCourseName.text
        val courseShortDescription = etCourseShortDescription.text
        val courseLongDescription = etLongDescription.text

        val courseSharean = mutableMapOf<String, Any>()

        setTemplateData(courseSharean)

        if (courseName != null) courseSharean["courseName"] = courseName.toString()
        if (courseShortDescription != null) courseSharean["courseShortDescription"] = courseShortDescription.toString()
        if (courseLongDescription != null) courseSharean["courseLongDescription"] = courseLongDescription.toString()

        return courseSharean
    }

    private fun setTemplateData(courseSharean: MutableMap<String, Any>): MutableMap<String, Any> {
        courseSharean["id"] = setIdByTime()
        courseSharean["status"] = "pending"
        return courseSharean
    }

    private fun setIdByTime(): String {
        val id = System.currentTimeMillis().toString().substring(0..9)
        return "cs$id"
    }
}