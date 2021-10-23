package com.flobiz.app.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.flobiz.app.R
import com.flobiz.app.databinding.ActivityMainBinding
import com.flobiz.app.repository.QuestionRepository
import com.flobiz.app.ui.base.BaseActivity
import com.flobiz.app.ui.viewmodel.QuestionViewModel
import com.flobiz.app.ui.viewmodel.QuestionViewModelFactory
import com.flobiz.app.util.Constants
import com.flobiz.app.webservice.WebServiceClient

class MainActivity : BaseActivity() {

	private lateinit var binding: ActivityMainBinding;
	private val TAG = "MainActivity"

	private lateinit var questionViewModel: QuestionViewModel

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

		val questionRepository = QuestionRepository(WebServiceClient.apiInterface)
		questionViewModel =
			ViewModelProvider(this, QuestionViewModelFactory(questionRepository)).get(
				QuestionViewModel::class.java
			)

		questionViewModel.fetchAllQuestions(
			Constants.key,
			Constants.order,
			Constants.sort,
			Constants.site

		).observe(this, { response ->
			binding.rvQuestions.also {
				binding.rvQuestions.adapter = QuestionAdapter(this, response.items)
				binding.rvQuestions.adapter!!.notifyDataSetChanged()
			}
		})

	}
}
