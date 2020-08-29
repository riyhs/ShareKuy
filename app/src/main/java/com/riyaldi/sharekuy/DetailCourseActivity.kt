@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.riyaldi.sharekuy

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.riyaldi.sharekuy.data.ShareanCourse
import com.riyaldi.sharekuy.db.CourseDatabase
import com.riyaldi.sharekuy.model.CourseFavouriteViewModel
import com.riyaldi.sharekuy.utils.Firebase
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

    private lateinit var courseFavViewmodel: CourseFavouriteViewModel
    private var isFav : Boolean = false
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(Firebase.COURSES_PATH_COLLECTION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_course)

        courseFavViewmodel = ViewModelProvider(this).get(CourseFavouriteViewModel::class.java)

        val userId = intent.getStringExtra(EXTRA_ID) as String

        retrieveData(userId)
        isLiked(userId)
    }

    private fun initView(course: ShareanCourse) {
        tvDetailCourseName.text = course.courseName
        tvDetailCourseCategory.text = course.courseCategory
        tvdetailCourseDescription.text = course.courseDescription

        chipsValidation(course)

        supportActionBar?.title = course.courseName

        fabClick(course)
    }

    private fun retrieveData(id: String) = CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot = shareanCourseCollection.document(id)
        querySnapshot.get()
            .addOnCompleteListener {
                if (!it.isSuccessful) Toast.makeText(this@DetailCourseActivity, "Pastikan Terhubung Internet", Toast.LENGTH_SHORT).show()
            }
            .addOnSuccessListener {
                val course = it.toObject<ShareanCourse>() as ShareanCourse
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

    private fun fabClick(shareanCourse: ShareanCourse) {
        fabFav.setOnClickListener {
            isLiked(shareanCourse.id)
            if (!isFav) {
                courseFavViewmodel.addCourse(
                    id = shareanCourse.id,
                    courseCategory = shareanCourse.courseCategory,
                    courseDescription = shareanCourse.courseDescription,
                    courseInstagram = shareanCourse.courseInstagram,
                    courseName = shareanCourse.courseName,
                    courseWebsite = shareanCourse.courseWebsite,
                    status = shareanCourse.status
                )
                Toast.makeText(this@DetailCourseActivity, "Added to Favourite", Toast.LENGTH_SHORT).show()
            } else {
                courseFavViewmodel.deleteCourse(
                    id = shareanCourse.id,
                    courseCategory = shareanCourse.courseCategory,
                    courseDescription = shareanCourse.courseDescription,
                    courseInstagram = shareanCourse.courseInstagram,
                    courseName = shareanCourse.courseName,
                    courseWebsite = shareanCourse.courseWebsite,
                    status = shareanCourse.status
                )
                Toast.makeText(
                    this@DetailCourseActivity,
                    "Deleted from Favourite",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isLiked(id: String) {
        val db = CourseDatabase.getInstance(applicationContext)
        val dao = db.courseDao()
        dao.getById(id).observe(this, Observer { data ->
            if (data.isNotEmpty() && data[0].id.isNotEmpty()) {
                isFav = true
                changeLoveIcon(isFav)
            } else {
                isFav = false
                changeLoveIcon(isFav)
            }
        })
    }

    private fun changeLoveIcon(state: Boolean) {
        if (state) fabFav.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_favorite
            )
        )
        else fabFav.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_favorite_empty
            )
        )
    }
}