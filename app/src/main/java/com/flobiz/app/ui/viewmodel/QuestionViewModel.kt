package com.flobiz.app.ui.viewmodel

import androidx.lifecycle.*
import com.flobiz.app.model.Question
import com.flobiz.app.model.QuestionResponse
import com.flobiz.app.model.Tag
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

	fun getAverageCount(questionList: List<Any>): MutableLiveData<List<String>> {

		val liveData: MutableLiveData<List<String>> = MutableLiveData()

		viewModelScope.launch {
			liveData.postValue(calculateAverageCount(questionList).value)
		}

		return liveData
	}

	fun getChangedTagList(count: Int, tagList: ArrayList<Tag>): MutableLiveData<ArrayList<Tag>> {
		val liveData: MutableLiveData<ArrayList<Tag>> = MutableLiveData()
		viewModelScope.launch {
			liveData.postValue(generateChangedTagList(count, tagList).value)
		}
		return liveData
	}

	private suspend fun calculateAverageCount(questionList: List<Any>): MutableLiveData<List<String>> =

		withContext(Dispatchers.Default) {
			val avgAnsCount: Double
			val avgViewCount: Double

			var totalItemViewCount = 0
			var totalItemAnsCount = 0

			var sumViewCount = 0.0
			var sumAnsCount = 0.0

			questionList.forEach { question ->
				if (question is Question) {
					if (question.view_count > 0) {
						totalItemViewCount++
						sumViewCount += question.view_count
					}

					if (question.answer_count > 0) {
						totalItemAnsCount++
						sumAnsCount += question.answer_count
					}
				}
			}

			avgAnsCount = sumAnsCount / (totalItemAnsCount).toDouble()
			avgViewCount = sumViewCount / (totalItemViewCount).toDouble()

			return@withContext MutableLiveData(
				arrayListOf(
					String.format("%.2f", avgViewCount),
					String.format("%.2f", avgAnsCount)
				)
			)
		}

	private suspend fun generateChangedTagList(
		count: Int,
		tagList: ArrayList<Tag>

	): LiveData<ArrayList<Tag>> {
		val listData: MutableLiveData<ArrayList<Tag>> = MutableLiveData()
		var temp = count
		withContext(Dispatchers.Default) {
			val resultList: ArrayList<Tag> = ArrayList()
			tagList.forEach { tag ->
				if (temp == 0) resultList.add(Tag(tag.name, MutableLiveData(true)))
				else resultList.add(tag)
				temp--
			}
			listData.postValue(resultList)
		}
		return listData
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
