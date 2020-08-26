package com.riyaldi.sharekuy.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.riyaldi.sharekuy.db.Course
import com.riyaldi.sharekuy.db.CourseDao
import com.riyaldi.sharekuy.db.CourseDatabase
import com.riyaldi.sharekuy.db.CourseRepository
import kotlinx.coroutines.*

@InternalCoroutinesApi
class CourseFavouriteViewModel (application: Application) : AndroidViewModel(application) {
    private val repository : CourseRepository
    private val courseDao: CourseDao = CourseDatabase.getInstance(application).courseDao()

    private var _favCourse : LiveData<List<Course>>
    val favCourse: LiveData<List<Course>>
        get() = _favCourse

    init {
        repository = CourseRepository(courseDao)
        _favCourse = repository.allCourse
    }

    fun addCourse(
        id : String,
        courseCategory : String,
        courseDescription : String,
        courseInstagram : String,
        courseName : String,
        courseWebsite : String,
        status : String
    ) = CoroutineScope(Dispatchers.IO).launch {
        repository.insert(Course(
            id = id,
            courseCategory = courseCategory,
            courseDescription = courseDescription,
            courseInstagram = courseInstagram,
            courseName = courseName,
            courseWebsite = courseWebsite,
            status = status
        ))
    }

    fun deleteCourse(
        id : String,
        courseCategory : String,
        courseDescription : String,
        courseInstagram : String,
        courseName : String,
        courseWebsite : String,
        status : String
    ) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(Course(
            id = id,
            courseCategory = courseCategory,
            courseDescription = courseDescription,
            courseInstagram = courseInstagram,
            courseName = courseName,
            courseWebsite = courseWebsite,
            status = status
        ))
    }

    override fun onCleared() {
        super.onCleared()
        Job().cancel()
    }
}