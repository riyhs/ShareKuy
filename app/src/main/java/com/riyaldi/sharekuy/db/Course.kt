package com.riyaldi.sharekuy.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "course_table")
data class Course(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "courseCategory") var courseCategory: String,
    @ColumnInfo(name = "courseDescription") var courseDescription: String,
    @ColumnInfo(name = "courseInstagram") var courseInstagram: String,
    @ColumnInfo(name = "courseName") var courseName: String,
    @ColumnInfo(name = "courseWebsite") var courseWebsite: String,
    @ColumnInfo(name = "status") var status: String
) : Parcelable