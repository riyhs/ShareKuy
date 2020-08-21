package com.riyaldi.sharekuy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShareanCourse(
    val courseCategory: String = "",
    val courseDescription: String = "",
    val courseInstagram: String = "",
    val courseName: String = "",
    val courseWebsite: String = "",
    val id: String = "",
    val status: String = ""
) : Parcelable