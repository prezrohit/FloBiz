package com.flobiz.app.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.flobiz.app.R
import com.google.android.material.snackbar.BaseTransientBottomBar

class CustomSnackBar private constructor(
	parent: ViewGroup, content: View,
	callback: com.google.android.material.snackbar.ContentViewCallback
) : BaseTransientBottomBar<CustomSnackBar>(parent, content, callback) {

	fun setText(text: CharSequence): CustomSnackBar {
		val textView = getView().findViewById<View>(R.id.snackbar_text) as TextView
		textView.text = text
		return this
	}

	fun setBackgroundColor(color: Int) {
		val relativeLayout = getView().findViewById<View>(R.id.snackbar_relativelayout) as RelativeLayout
		relativeLayout.setBackgroundColor(color)
	}

	private class CustomContentViewCallback(private val content: View) :
		com.google.android.material.snackbar.ContentViewCallback {

		override fun animateContentIn(delay: Int, duration: Int) {
		}

		override fun animateContentOut(delay: Int, duration: Int) {
		}
	}

	companion object {

		fun make(parent: ViewGroup, duration: Int): CustomSnackBar {
			val inflater = LayoutInflater.from(parent.context)
			val content = inflater.inflate(R.layout.custom_snackbar, parent, false)
			val viewCallback = CustomContentViewCallback(content)

			return CustomSnackBar(parent, content, viewCallback).run {
				getView().setPadding(0, 0, 0, 0)
				this.duration = duration
				this
			}
		}
	}
}