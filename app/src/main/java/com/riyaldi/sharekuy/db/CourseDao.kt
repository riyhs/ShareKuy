package com.riyaldi.sharekuy.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CourseDao {
    @Query("SELECT * FROM course_table")
    fun getAll(): LiveData<List<Course>>

    @Query("SELECT * FROM course_table WHERE id = :id")
    fun getById(id: String): LiveData<List<Course>>

    @Insert(onConflict = REPLACE)
    fun add(course: Course)

    @Delete
    fun delete(course: Course)
}