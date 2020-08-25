package com.riyaldi.sharekuy

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        tvAboutRiyaldi.setOnClickListener {
            val intentWeb = Intent(Intent.ACTION_VIEW)
            intentWeb.data = Uri.parse("http://riyhs.github.io")
            startActivity(intentWeb)
        }

    }
}