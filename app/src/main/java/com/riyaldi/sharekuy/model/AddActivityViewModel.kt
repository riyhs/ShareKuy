package com.riyaldi.sharekuy.model

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.riyaldi.sharekuy.utils.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(Firebase.COURSES_PATH_COLLECTION)

    fun saveData(shareanCourse : MutableMap<String, Any>, id : String) = CoroutineScope(Dispatchers.IO).launch {
        shareanCourseCollection.document(id).set(shareanCourse)
            .addOnCompleteListener {
                if (it.isSuccessful) Toast.makeText(getApplication(), "Berhasil menambahkan ${shareanCourse["courseName"]}", Toast.LENGTH_LONG).show()
                else Toast.makeText(getApplication(), "Gagal menambahkan ${shareanCourse["courseName"]}, ulangi beberapa menit lagi.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}