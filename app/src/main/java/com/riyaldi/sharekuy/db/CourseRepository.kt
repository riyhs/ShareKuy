package com.riyaldi.sharekuy.db

class CourseRepository (private val courseDao: CourseDao) {
    val allCourse: List<Course> = courseDao.getAll()

    fun delete(course: Course) {
        courseDao.delete(course)
    }

    fun insert(course: Course) {
        courseDao.add(course)
    }
}