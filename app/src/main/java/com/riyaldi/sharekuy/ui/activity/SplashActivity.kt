package com.riyaldi.sharekuy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.riyaldi.sharekuy.R
import com.riyaldi.sharekuy.ui.fragment.SplashFragment
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentSplash, SplashFragment(), "SPLASH_FRAGMENT_TAG")
            .commit()

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)

    }
}