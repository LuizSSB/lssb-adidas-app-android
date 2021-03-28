package com.luizssb.adidas.confirmed.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.luizssb.adidas.confirmed.R

abstract class ImageViewEx private constructor() {
    companion object {
        fun ImageView.setRemoteImage(uri: String?) {
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher_desaturated)
                    .into(this)
        }
    }
}
