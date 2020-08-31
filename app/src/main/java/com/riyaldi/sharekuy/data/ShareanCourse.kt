package com.riyaldi.sharekuy.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
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