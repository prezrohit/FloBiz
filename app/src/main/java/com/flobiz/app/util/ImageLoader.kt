package com.flobiz.app.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.flobiz.app.R

class ImageLoader {
	companion object Loader {
		@JvmStatic
		@BindingAdapter("url")
		fun loadImage(view: ImageView, url: String) {
			Glide.with(view.context)
				.load(url)
				.centerCrop()
				.placeholder(R.drawable.loading)
				.into(view)
		}
	}
}