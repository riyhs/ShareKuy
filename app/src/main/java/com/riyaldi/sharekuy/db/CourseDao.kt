package com.riyaldi.sharekuy.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CourseDao {
    @Query("SELECT * FROM course_table")
    fun getAll(): List<Course>

    @Query("SELECT * FROM course_table WHERE id = :id")
    fun getByUsername(id: String): List<Course>

    @Insert(onConflict = REPLACE)
    fun add(course: Course)

    @Delete
    fun delete(course: Course)
}