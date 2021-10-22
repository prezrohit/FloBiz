package com.flobiz.app.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.flobiz.app.R
import com.flobiz.app.repository.QuestionRepository
import com.flobiz.app.ui.viewmodel.QuestionViewModel
import com.flobiz.app.ui.viewmodel.QuestionViewModelFactory
import com.flobiz.app.util.Constants
import com.flobiz.app.webservice.WebServiceClient

class MainActivity : BaseActivity() {

	private val TAG = "MainActivity"

	lateinit var questionViewModel: QuestionViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val questionRepository = QuestionRepository(WebServiceClient.apiInterface)
		questionViewModel = ViewModelProvider(this, QuestionViewModelFactory(questionRepository)).get(QuestionViewModel::class.java)
		questionViewModel.fetchAllQuestions(
			Constants.key,
			Constants.order,
			Constants.sort,
			Constants.site

		).observe(this, { response ->
			Log.d(TAG, "onCreate: $response")
		})
	}
}