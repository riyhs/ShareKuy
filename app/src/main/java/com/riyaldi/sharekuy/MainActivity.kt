package com.riyaldi.sharekuy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.riyaldi.sharekuy.data.ShareanCourse
import com.riyaldi.sharekuy.db.CourseDatabase
import com.riyaldi.sharekuy.model.CourseFavouriteViewModel
import com.riyaldi.sharekuy.utils.Firebase.COURSES_PATH_COLLECTION
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sharean_card.*
import kotlinx.android.synthetic.main.sharean_card.view.*
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class MainActivity : AppCompatActivity(){

    private lateinit var mAdapter: FirestoreRecyclerAdapter<ShareanCourse, ShareanCoursesViewHolder>
    private lateinit var courseFavViewmodel: CourseFavouriteViewModel
    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(COURSES_PATH_COLLECTION)
    private var mQuery = shareanCourseCollection.whereEqualTo("status", "accepted")
    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        courseFavViewmodel = ViewModelProvider(this).get(CourseFavouriteViewModel::class.java)

        initView()
        setAdapter(mQuery)
    }

    private fun initView() {
        rvSharean.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }

//        chipInstagram.setChipBackgroundColorResource(R.color.colorIg)

        chipClick()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuAdd -> {
                startActivity(Intent(this@MainActivity, AddActivity::class.java))
                true
            }

            R.id.menuAbout -> {
                startActivity(Intent(this@MainActivity, AboutActivity::class.java))
                true
            }

            R.id.menuFav -> {
                startActivity(Intent(this@MainActivity, FavouriteActivity::class.java))
                true
            }

            else -> true
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

                ibFav.setOnClickListener {
                    isLiked(shareanCourse.id)
                    if (!isFav) {
                        courseFavViewmodel.addCourse(
                            id = shareanCourse.id,
                            courseCategory = shareanCourse.courseCategory,
                            courseDescription = shareanCourse.courseDescription,
                            courseInstagram = shareanCourse.courseInstagram,
                            courseName = shareanCourse.courseName,
                            courseWebsite = shareanCourse.courseWebsite,
                            status = shareanCourse.status
                        )
                        Toast.makeText(this@MainActivity, "Added to Favourite", Toast.LENGTH_SHORT).show()
                    } else {
                        courseFavViewmodel.deleteCourse(
                            id = shareanCourse.id,
                            courseCategory = shareanCourse.courseCategory,
                            courseDescription = shareanCourse.courseDescription,
                            courseInstagram = shareanCourse.courseInstagram,
                            courseName = shareanCourse.courseName,
                            courseWebsite = shareanCourse.courseWebsite,
                            status = shareanCourse.status
                        )
                        Toast.makeText(this@MainActivity, "Deleted from Favourite", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isLiked(id: String) {
        val db = CourseDatabase.getInstance(applicationContext)
        val dao = db.courseDao()
        dao.getById(id).observe(this, Observer { data ->
            Log.d("DATA", "$data")
            if (data.isNotEmpty() && data[0].id.isNotEmpty()) {
                isFav = true
                changeLoveIcon(isFav)
            } else {
                isFav = false
                changeLoveIcon(isFav)
            }
        })
    }

    private fun changeLoveIcon(state: Boolean) {
        if (state) ibFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite))
        else ibFav.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite_empty))
    }
}

//Todo warna tag category onClicked / active
//Todo website / instagram