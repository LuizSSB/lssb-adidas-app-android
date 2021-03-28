package com.luizssb.adidas.confirmed.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luizssb.adidas.confirmed.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val layout by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.root)
        setSupportActionBar(layout.toolbar)
    }
}
