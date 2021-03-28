package com.luizssb.adidas.confirmed.view

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import com.luizssb.adidas.confirmed.ProductListFragmentDirections
import com.luizssb.adidas.confirmed.R
import com.luizssb.adidas.confirmed.databinding.ActivityMainBinding
import com.luizssb.adidas.confirmed.view.adapter.ProductsAdapter
import com.luizssb.adidas.confirmed.viewmodel.product.ProductListViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val layout by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_navhost) as NavHostFragment
//        val navController = navHostFragment.navController
//        val action = ProductListFragmentDirections.actionProductListFragmentToProductFragment()
        setContentView(layout.root)
        setSupportActionBar(layout.toolbar)
    }
}
