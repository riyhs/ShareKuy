package com.riyaldi.sharekuy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.riyaldi.sharekuy.adapter.FavCourseAdapter
import com.riyaldi.sharekuy.db.Course
import com.riyaldi.sharekuy.db.CourseDatabase
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavouriteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        getFavUserData()

        supportActionBar?.title = "Favorit"

    }

    private fun setAdapter(courseData: List<Course>) {
        rvFavCourse.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
            adapter = FavCourseAdapter(courseData)
        }
    }

    private fun getFavUserData() {
        val db = CourseDatabase.getInstance(applicationContext)
        val dao = db.courseDao()

        dao.getAll().observe(this, Observer { liveUserData ->
            if (liveUserData.isNotEmpty() && liveUserData != null){
                setAdapter(liveUserData)
            } else {
                tvEmptyData.isGone = false
            }
        })
    }
}