package com.riyaldi.sharekuy

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.riyaldi.sharekuy.data.ShareanCourse
import com.riyaldi.sharekuy.db.CourseDatabase
import com.riyaldi.sharekuy.model.CourseFavouriteViewModel
import kotlinx.android.synthetic.main.activity_detail_course.*
import kotlinx.android.synthetic.main.sharean_card.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class DetailCourseActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var courseFavViewmodel: CourseFavouriteViewModel
    private var isFav : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_course)

        courseFavViewmodel = ViewModelProvider(this).get(CourseFavouriteViewModel::class.java)

        val shareanCourse = intent.getParcelableExtra<ShareanCourse>(EXTRA_ID) as ShareanCourse

        isLiked(shareanCourse.id)
        initView(shareanCourse)
    }

    private fun initView(course: ShareanCourse) {
        tvDetailCourseName.text = course.courseName
        tvDetailCourseCategory.text = course.courseCategory
        tvdetailCourseDescription.text = course.courseDescription

        chipsValidation(course)

        fabClick(course)
    }

    private fun chipsValidation(shareanCourse: ShareanCourse) {
        if (shareanCourse.courseInstagram.isNotEmpty() && shareanCourse.courseWebsite.isNotEmpty()) {
            chipDetailCourseInstagram.isGone = false
            chipDetailCourseWebsite.isGone = false
        } else if (shareanCourse.courseWebsite.isNotEmpty() && shareanCourse.courseInstagram.isEmpty()) {
            chipDetailCourseWebsite.isGone = false

            // Remove margin when chipInstagram.isGone = true
            val param = chipDetailCourseInstagram.layoutParams as ViewGroup.MarginLayoutParams
            param.marginStart = 0
            chipDetailCourseWebsite.layoutParams = param
        } else {
            chipDetailCourseInstagram.isGone = false
        }

        chipDetailCourseInstagram.setOnClickListener {
            val intentIg = Intent(Intent.ACTION_VIEW)
            intentIg.data = Uri.parse(shareanCourse.courseInstagram)
            intentIg.setPackage("com.instagram.android")

            startActivity(intentIg)
        }

        chipDetailCourseWebsite.setOnClickListener {
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
                Toast.makeText(this@DetailCourseActivity, "Deleted from Favourite", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isLiked(id: String) {
        val db = CourseDatabase.getInstance(applicationContext)
        val dao = db.courseDao()
        dao.getById(id).observe(this, Observer { data ->
            Log.d("DATA", "$data")
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
        if (state) fabFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite))
        else fabFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite_empty))
    }
}