package com.flobiz.app.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class FloBizApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		if (instance == null) {
			instance = this
		}
	}

	private val isNetworkConnected: Boolean
		get() {
			val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
			val activeNetwork = cm.activeNetworkInfo
			return activeNetwork != null &&
					activeNetwork.isConnectedOrConnecting
		}

	companion object {
		var instance: FloBizApplication? = null
			private set

		fun hasNetwork(): Boolean {
			return instance!!.isNetworkConnected
		}
	}
}