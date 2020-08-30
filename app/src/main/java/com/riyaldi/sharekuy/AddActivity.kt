package com.riyaldi.sharekuy

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.riyaldi.sharekuy.utils.Firebase.COURSES_PATH_COLLECTION
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(COURSES_PATH_COLLECTION)
    private var id = "id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initView()
    }

    private fun initView() {
        btSaveAddCourses.setOnClickListener {
            if (etInstagram.text != null && etWebsite.text != null && rgCategory.checkedRadioButtonId != -1 && etCourseName.text != null && etCourseDescription.text != null) {
                if (etCourseName.text!!.isNotEmpty() && etCourseDescription.text!!.isNotEmpty()) {
                    if (etInstagram.text!!.isNotEmpty() || etWebsite.text!!.isNotEmpty()) {
                        buildDialog()
                    }
                }
            }
        }
    }

    private fun buildDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Catatan")
            .setMessage("Pastikan data yang dimasukan sudah sesuai,\nsetiap data yang di unggah akan kami tinjau dalam waktu 1 x 24 jam sebelum di tampilkan ke aplikasi.")

            .setPositiveButton("Simpan Data") { _, _ ->
                id = setIdByTime()
                val shareanCourse = initData()
                saveData(shareanCourse)
            }
            .show()
    }

    private fun saveData(shareanCourse : MutableMap<String, Any>) = CoroutineScope(Dispatchers.IO).launch {
        shareanCourseCollection.document(id).set(shareanCourse)
            .addOnCompleteListener {
                finish()
                if (it.isSuccessful) Toast.makeText(this@AddActivity, "Berhasil menambahkan ${shareanCourse["courseName"]}", Toast.LENGTH_LONG).show()
                else Toast.makeText(this@AddActivity, "Gagal menambahkan ${shareanCourse["courseName"]}, ulangi beberapa menit lagi.", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@AddActivity, "Error ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun initData() : MutableMap<String, Any> {
        val courseName = etCourseName.text
        val courseDescription = etCourseDescription.text
        val courseInstagram = etInstagram.text
        val courseWebsite = etWebsite.text
        val courseCategory = categoryValidation()

        val courseSharean = mutableMapOf<String, Any>()

        setTemplateData(courseSharean)

        if (courseName != null) courseSharean["courseName"] = courseName.toString()
        if (courseDescription != null) courseSharean["courseDescription"] = courseDescription.toString()
        if (courseInstagram != null) courseSharean["courseInstagram"] = "http://instagram.com/$courseInstagram"
        if (courseWebsite != null) courseSharean["courseWebsite"] = courseWebsite.toString()
        courseSharean["courseCategory"] = courseCategory

        warningEditText()

        return courseSharean
    }

    private fun categoryValidation(): String {
        var courseCategory = ""

        val id: Int = rgCategory.checkedRadioButtonId
        if (id!=-1) {
            val radio: RadioButton = findViewById(id)
            courseCategory = radio.text.toString()
        } else {
            Toast.makeText(applicationContext,"Pilih salah satu kategori", Toast.LENGTH_SHORT).show()
        }

        return courseCategory
    }

    private fun warningEditText() {
        val courseName = etCourseName.text
        val courseDescription = etCourseDescription.text
        val courseInstagram = etInstagram.text
        val courseWebsite = etWebsite.text

        if (courseWebsite != null && courseInstagram != null) {
            if(courseInstagram.isEmpty() && courseWebsite.isEmpty()) {
                Toast.makeText(this@AddActivity, "Salah satu form INSTAGRAM / WEBSITE harus di isi", Toast.LENGTH_LONG).show()
                etInstagram.error = "Belum di isi"
                etWebsite.error = "Belum di isi"
            }
        }

        if (courseName != null && courseName.isEmpty()) {
            etCourseName.error = "Wajib di isi"
        }

        if (courseDescription != null && courseDescription.isEmpty()) {
            etCourseDescription.error = "Wajib di isi"
        }
    }

    private fun setTemplateData(courseSharean: MutableMap<String, Any>): MutableMap<String, Any> {
        courseSharean["id"] = id
        courseSharean["status"] = "pending"
        courseSharean["courseInstagram"] = "null"
        courseSharean["courseWebsite"] = "null"
        return courseSharean
    }

    private fun setIdByTime(): String {
        val id = System.currentTimeMillis().toString().substring(0..9)
        return "cs$id"
    }
}