package com.riyaldi.sharekuy.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.riyaldi.sharekuy.R
import com.riyaldi.sharekuy.db.Course
import kotlinx.android.synthetic.main.sharean_card.view.*

class FavCourseAdapter (private val course: List<Course>) : RecyclerView.Adapter<FavCourseAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavCourseAdapter.ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sharean_card, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavCourseAdapter.ListViewHolder, position: Int) = holder.bind(course[position])

    override fun getItemCount(): Int = course.size

    inner class ListViewHolder (itemview: View) : RecyclerView.ViewHolder(itemview){
        @SuppressLint("SetTextI18n")
        fun bind(shareanCourse: Course) {
            with(itemView) {
                val name = shareanCourse.courseName
                val description = shareanCourse.courseDescription
                val category = shareanCourse.courseCategory

                tvCourseName.text = name
                tvCourseDescription.text = description
                tvCategory.text = "Kategori : $category"

                if (shareanCourse.courseInstagram.isNotEmpty() && shareanCourse.courseWebsite.isNotEmpty()) {
                    chipInstagram.isGone = false
                    chipWebsite.isGone = false
                } else if (shareanCourse.courseWebsite.isNotEmpty() && shareanCourse.courseInstagram.isEmpty()) {
                    chipWebsite.isGone = false

                    // Remove margin when chipInstagram.isGone = true
                    val param = chipWebsite.layoutParams as ViewGroup.MarginLayoutParams
                    param.marginStart = 0
                    chipWebsite.layoutParams = param
                } else {
                    chipInstagram.isGone = false
                }

                chipInstagram.setOnClickListener {
                    val intentIg = Intent(Intent.ACTION_VIEW)
                    intentIg.data = Uri.parse(shareanCourse.courseInstagram)
                    intentIg.setPackage("com.instagram.android")

                    context.startActivity(intentIg)
                }

                chipWebsite.setOnClickListener {
                    val intentWeb = Intent(Intent.ACTION_VIEW)
                    intentWeb.data = Uri.parse(shareanCourse.courseWebsite)

                    context.startActivity(intentWeb)
                }
            }
        }
    }
}