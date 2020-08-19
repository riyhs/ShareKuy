package com.riyaldi.sharekuy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import com.riyaldi.sharekuy.utils.Firebase.COURSES_PATH_COLLECTION

class MainActivity : AppCompatActivity() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val shareanCourseCollection = mFirestore.collection(COURSES_PATH_COLLECTION)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                startActivity(Intent(this@MainActivity, AddActivity::class.java))
                return true
            }

            else -> return true
        }
    }
}

//Todo 1. Home (Daftar ShareAn)