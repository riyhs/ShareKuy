package com.riyaldi.sharekuy.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CourseRepository (private val courseDao: CourseDao) {
    val allCourse: LiveData<List<Course>> = courseDao.getAll()

    fun delete(course: Course) {
        courseDao.delete(course)
    }

    fun insert(course: Course) {
        courseDao.add(course)
    }
}