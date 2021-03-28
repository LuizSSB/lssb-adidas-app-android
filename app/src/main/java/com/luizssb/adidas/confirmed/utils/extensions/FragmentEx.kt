package com.luizssb.adidas.confirmed.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

abstract class FragmentEx {
    companion object {
        fun Fragment.setSupportActionBar(toolbar: Toolbar) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        }

        fun Fragment.enableActionBarBackButton() {
            (requireActivity() as AppCompatActivity).supportActionBar!!.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
        }
    }
}
