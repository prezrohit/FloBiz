package com.flobiz.app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flobiz.app.model.QuestionResponse
import com.flobiz.app.repository.QuestionRepository

class QuestionViewModel(
	private val repository: QuestionRepository

) : ViewModel() {

	fun fetchAllQuestions(
		key: String,
		order: String,
		sort: String,
		site: String

	): MutableLiveData<QuestionResponse> {
		return repository.fetchAllQuestions(key, order, sort, site)
	}
}

class QuestionViewModelFactory constructor(private val repository: QuestionRepository): ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
			QuestionViewModel(this.repository) as T
		} else {
			throw IllegalArgumentException("ViewModel Not Found")
		}
	}
}