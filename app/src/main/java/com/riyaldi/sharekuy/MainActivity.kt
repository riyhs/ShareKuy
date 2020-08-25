package com.riyaldi.sharekuy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.riyaldi.sharekuy.model.ShareanCourse
import com.riyaldi.sharekuy.utils.Firebase.COURSES_PATH_COLLECTION
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sharean_card.view.*

class MainActivity : AppCompatActivity(){

    private lateinit var mAdapter: FirestoreRecyclerAdapter<ShareanCourse, ShareanCoursesViewHolder>
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(COURSES_PATH_COLLECTION)
    private var mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setAdapter(mQuery)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuAdd -> {
                startActivity(Intent(this@MainActivity, AddActivity::class.java))
                return true
            }

            else -> return true
        }
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
        chipAll.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipSchool.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
                .whereEqualTo("courseCategory", "materi_sekolah")
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipTech.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
                .whereEqualTo("courseCategory", "teknologi")
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipMotivation.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
                .whereEqualTo("courseCategory", "motivasi")
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipCreativity.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
                .whereEqualTo("courseCategory", "kreativitas")
            setAdapter(mQuery)
            mAdapter.startListening()
        }
        chipOthers.setOnClickListener {
            mAdapter.stopListening()
            mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
                .whereEqualTo("courseCategory", "yang_lainnya")
            setAdapter(mQuery)
            mAdapter.startListening()
        }
    }

    private fun initView() {
        rvSharean.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

        chipClick()
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
                    Toast.makeText(
                        applicationContext,
                        "Klik ${model.courseName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        mAdapter.notifyDataSetChanged()
        rvSharean.adapter = mAdapter
    }

    class ShareanCoursesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(shareanCourse: ShareanCourse) {
            view.apply {
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