package com.flobiz.app.ui.base

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.flobiz.app.R
import com.flobiz.app.util.ConnectionStateMonitor
import com.flobiz.app.util.CustomSnackBar
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity(),
	ConnectionStateMonitor.OnNetworkAvailableCallbacks {

	private var firstLaunch = true
	private var viewGroup: ViewGroup? = null
	private var snackbar: CustomSnackBar? = null
	private var connectionStateMonitor: ConnectionStateMonitor? = null

	override fun onResume() {
		super.onResume()
		if (viewGroup == null) viewGroup = findViewById(android.R.id.content)
		if (snackbar == null)
			snackbar = CustomSnackBar.make(viewGroup!!, Snackbar.LENGTH_INDEFINITE)
				.setText("No internet connection")

		if (connectionStateMonitor == null)
			connectionStateMonitor = ConnectionStateMonitor(this, this)
		connectionStateMonitor?.enable()

		val res = connectionStateMonitor?.hasNetworkConnection()
		if (res == false) {
			onNegative()
			firstLaunch = false

		} else if (!firstLaunch && res == true)
			onPositive()
	}

	override fun onPause() {
		snackbar?.dismiss()
		snackbar = null
		firstLaunch = true
		connectionStateMonitor?.disable()
		connectionStateMonitor = null
		super.onPause()
	}

	override fun onPositive() {
		runOnUiThread {
			if (!firstLaunch) {
				snackbar?.setBackgroundColor(resources.getColor(R.color.green))
				snackbar?.duration = Snackbar.LENGTH_SHORT
				snackbar?.setText("Back Online")?.show()

			} else {
				snackbar?.dismiss()
				firstLaunch = false
			}
		}
	}

	override fun onNegative() {
		runOnUiThread {
			snackbar?.setBackgroundColor(resources.getColor(R.color.red))
			snackbar?.duration = Snackbar.LENGTH_INDEFINITE
			snackbar?.setText("No internet connection")?.show()
			firstLaunch = false
		}
	}

}
