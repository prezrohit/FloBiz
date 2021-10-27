package com.flobiz.app.util

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.flobiz.app.R

class BindingAdapter {

	companion object {
		@JvmStatic
		@BindingAdapter("url")
		fun loadImage(view: ImageView, url: String?) {
			if (url != null)
				Glide.with(view.context)
					.load(url)
					.centerCrop()
					.placeholder(R.drawable.loading)
					.into(view)
		}
	}
}
