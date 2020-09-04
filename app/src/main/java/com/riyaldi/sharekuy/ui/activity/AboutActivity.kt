package com.riyaldi.sharekuy.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riyaldi.sharekuy.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.title = "Tentang ShareKuy"

        tvAboutRiyaldi.setOnClickListener {
            val intentWeb = Intent(Intent.ACTION_VIEW)
            intentWeb.data = Uri.parse("http://riyhs.github.io")
            startActivity(intentWeb)
        }

    }
}