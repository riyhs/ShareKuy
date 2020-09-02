@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.riyaldi.sharekuyadmin

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.riyaldi.sharekuyadmin.data.ShareanCourse
import com.riyaldi.sharekuyadmin.utils.Firebase
import kotlinx.android.synthetic.main.activity_detail_course.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class DetailCourseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private var isFav : Boolean = false
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(Firebase.COURSES_PATH_COLLECTION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_course)

        val userId = intent.getStringExtra(EXTRA_ID) as String

        retrieveData(userId)
    }

    private fun initView(course: ShareanCourse) {
        tvDetailCourseName.text = course.courseName
        tvDetailCourseCategory.text = course.courseCategory
        tvdetailCourseDescription.text = course.courseDescription

        chipsValidation(course)

        supportActionBar?.title = course.courseName
    }

    private fun retrieveData(id: String) = CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot = shareanCourseCollection.document(id)
        querySnapshot.get()
            .addOnCompleteListener {
                if (!it.isSuccessful) Toast.makeText(this@DetailCourseActivity, "Pastikan Terhubung Internet", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                val course = it.toObject(ShareanCourse::class.java) as ShareanCourse
                initView(course)
            }
            .addOnFailureListener {
                Toast.makeText(this@DetailCourseActivity, "Error : ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun chipsValidation(shareanCourse: ShareanCourse) {
        val dip = 24f
        val r: Resources = resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.displayMetrics
        )

        if (shareanCourse.courseInstagram.isNotEmpty() && shareanCourse.courseWebsite.isNotEmpty()) {
            btDetailInstagram.isGone = false
            btDetailWebsite.isGone = false
        } else if (shareanCourse.courseWebsite.isNotEmpty() && shareanCourse.courseInstagram.isEmpty()) {
            btDetailWebsite.isGone = false

            val param = btDetailWebsite.layoutParams as ViewGroup.MarginLayoutParams
            param.topMargin = px.toInt()
            btDetailWebsite.layoutParams = param
        } else {
            btDetailInstagram.isGone = false
        }

        btDetailInstagram.setOnClickListener {
            val intentIg = Intent(Intent.ACTION_VIEW)
            intentIg.data = Uri.parse(shareanCourse.courseInstagram)
            intentIg.setPackage("com.instagram.android")

            startActivity(intentIg)
        }

        btDetailWebsite.setOnClickListener {
            val intentWeb = Intent(Intent.ACTION_VIEW)
            intentWeb.data = Uri.parse(shareanCourse.courseWebsite)

            startActivity(intentWeb)
        }
    }
}