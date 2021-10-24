package com.flobiz.app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.flobiz.app.model.QuestionResponse
import com.flobiz.app.repository.QuestionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionViewModel(private val repository: QuestionRepository) : ViewModel() {

	private val TAG = "QuestionViewModel"

	fun fetchAllQuestions(
		key: String,
		order: String,
		sort: String,
		site: String

	): MutableLiveData<QuestionResponse> {
		return repository.fetchAllQuestions(key, order, sort, site)
	}

	fun getAverageCount(questionResponse: QuestionResponse): MutableLiveData<List<String>> {

		var liveData: MutableLiveData<List<String>> = MutableLiveData()

		viewModelScope.launch {
			liveData.postValue(calculateAverageCount(questionResponse).value)
		}

		return liveData
	}

	private suspend fun calculateAverageCount(questionResponse: QuestionResponse): MutableLiveData<List<String>> =

		withContext(Dispatchers.Default) {
			var avgAnsCount = 0.0
			var avgViewCount = 0.0

			var totalItemViewCount = 0
			var totalItemAnsCount = 0

			var sumViewCount = 0.0
			var sumAnsCount = 0.0

			questionResponse.items.forEach { question ->
				if (question.view_count > 0) {
					totalItemViewCount++
					sumViewCount += question.view_count
				}

				if (question.answer_count > 0) {
					totalItemAnsCount++
					sumAnsCount += question.answer_count
				}
			}

			avgAnsCount = sumAnsCount / (totalItemAnsCount).toDouble()
			avgViewCount = sumViewCount / (totalItemViewCount).toDouble()

			return@withContext MutableLiveData(arrayListOf(String.format("%.2f", avgViewCount), String.format("%.2f", avgAnsCount)))
		}


}


class QuestionViewModelFactory constructor(private val repository: QuestionRepository) :
	ViewModelProvider.Factory {

	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		return if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
			QuestionViewModel(this.repository) as T
		} else {
			throw IllegalArgumentException("ViewModel Not Found")
		}
	}
}