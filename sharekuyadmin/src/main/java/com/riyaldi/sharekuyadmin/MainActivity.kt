package com.riyaldi.sharekuyadmin

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.riyaldi.sharekuyadmin.data.ShareanCourse
import com.riyaldi.sharekuyadmin.utils.Firebase.COURSES_PATH_COLLECTION
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sharean_card.view.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity(){

    private lateinit var mAdapter: FirestoreRecyclerAdapter<ShareanCourse, ShareanCoursesViewHolder>
    private var status = "pending"
    private var courseCategory = ""
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(COURSES_PATH_COLLECTION)
    private var mQuery = shareanCourseCollection.whereEqualTo("status", status)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setAdapter(mQuery)
    }

    private fun initView() {
        rvSharean.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        chipClick()
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }


    private fun chipClick() {
        chipPendingStatus.setOnClickListener {
            status = "pending"
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }

        chipAcceptedStatus.setOnClickListener {
            status = "accepted"
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }

        chipAll.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipSchool.setOnClickListener {
            mAdapter.stopListening()
            courseCategory = "materi_sekolah"
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipTech.setOnClickListener {
            mAdapter.stopListening()
            courseCategory = "teknologi"
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipGeneral.setOnClickListener {
            mAdapter.stopListening()
            courseCategory = "umum"
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipContent.setOnClickListener {
            mAdapter.stopListening()
            courseCategory = "konten_positif"
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipOthers.setOnClickListener {
            mAdapter.stopListening()
            courseCategory = "yang_lainnya"
            mQuery = shareanCourseCollection.whereEqualTo("status", status)
                .whereEqualTo("courseCategory", courseCategory)
            setAdapter(mQuery)
            mAdapter.startListening()
        }
    }

    private fun setAdapter(query: Query) {
        val options = FirestoreRecyclerOptions.Builder<ShareanCourse>()
            .setQuery(query, ShareanCourse::class.java)
            .build()

        mAdapter = object : FirestoreRecyclerAdapter<ShareanCourse, ShareanCoursesViewHolder>(options) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ShareanCoursesViewHolder {
                return ShareanCoursesViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.sharean_card,
                        parent,
                        false
                    )
                )
            }

            override fun onBindViewHolder(
                holder: ShareanCoursesViewHolder,
                position: Int,
                model: ShareanCourse
            ) {
                holder.bind(model)
                holder.itemView.setOnClickListener {
                    val intent = Intent(this@MainActivity, DetailCourseActivity::class.java)
                    intent.putExtra(DetailCourseActivity.EXTRA_ID, model.id)
                    startActivity(intent)
                }
            }
        }
        mAdapter.notifyDataSetChanged()
        rvSharean.adapter = mAdapter
    }

    inner class ShareanCoursesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(shareanCourse: ShareanCourse) {
            view.apply {
                val name = shareanCourse.courseName
                val description = shareanCourse.courseDescription
                val category = shareanCourse.courseCategory

                tvCourseName.text = name
                tvCourseDescription.text = description
                tvCategory.text = "Kategori : $category"

                btInstagram.isGone = true
                btWebsite.isGone = true

                if (shareanCourse.courseInstagram.isNotEmpty() && shareanCourse.courseWebsite.isNotEmpty()) {
                    btInstagram.isGone = false
                    btWebsite.isGone = false
                } else if (shareanCourse.courseWebsite.isNotEmpty() && shareanCourse.courseInstagram.isEmpty()) {
                    btWebsite.isGone = false

                    val param = btWebsite.layoutParams as ConstraintLayout.LayoutParams
                    param.marginStart = 0
                    btWebsite.requestLayout()
                } else {
                    btInstagram.isGone = false

                    val param = btInstagram.layoutParams as ConstraintLayout.LayoutParams
                    param.marginEnd = 0
                    btInstagram.requestLayout()
                }

                btInstagram.setOnClickListener {
                    val intentIg = Intent(Intent.ACTION_VIEW)
                    intentIg.data = Uri.parse(shareanCourse.courseInstagram)
                    intentIg.setPackage("com.instagram.android")

                    context.startActivity(intentIg)
                }

                btWebsite.setOnClickListener {
                    val intentWeb = Intent(Intent.ACTION_VIEW)
                    intentWeb.data = Uri.parse(shareanCourse.courseWebsite)

                    context.startActivity(intentWeb)
                }
            }
        }
    }
}