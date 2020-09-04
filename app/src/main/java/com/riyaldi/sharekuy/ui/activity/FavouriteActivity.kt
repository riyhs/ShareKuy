package com.riyaldi.sharekuy.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.riyaldi.sharekuy.R
import com.riyaldi.sharekuy.adapter.FavCourseAdapter
import com.riyaldi.sharekuy.db.Course
import com.riyaldi.sharekuy.db.CourseDatabase
import com.riyaldi.sharekuy.model.CourseFavouriteViewModel
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class FavouriteActivity : AppCompatActivity() {
    private lateinit var viewModel: CourseFavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        viewModel = ViewModelProvider(this).get(CourseFavouriteViewModel::class.java)

        getFavUserData()

        supportActionBar?.title = "Favorit"
    }

    private fun setAdapter(courseData: List<Course>) {
        rvFavCourse.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
            adapter = FavCourseAdapter(courseData)
            hasFixedSize()
        }
    }

    private fun getFavUserData() {
        val db = CourseDatabase.getInstance(applicationContext)
        val dao = db.courseDao()

        dao.getAll().observe(this, Observer { liveUserData ->
            if (liveUserData.isNotEmpty() && liveUserData != null){
                setAdapter(liveUserData)
            } else {
                setAdapter(liveUserData)
                tvEmptyData.isGone = false
            }
        })
    }
}