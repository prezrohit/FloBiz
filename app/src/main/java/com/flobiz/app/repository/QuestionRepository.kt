package com.flobiz.app.repository

import androidx.lifecycle.MutableLiveData
import com.flobiz.app.model.QuestionResponse
import com.flobiz.app.webservice.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionRepository(private val webService: WebService) {

	fun fetchAllQuestions(
		key: String,
		order: String,
		sort: String,
		site: String

	): MutableLiveData<QuestionResponse> {

		val questions = MutableLiveData<QuestionResponse>()

		val response = webService.fetchQuestions(key, order, sort, site)
		response.enqueue(object : Callback<QuestionResponse> {

			override fun onResponse(
				call: Call<QuestionResponse>,
				response: Response<QuestionResponse>
			) {
				questions.postValue(response.body())
			}

			override fun onFailure(call: Call<QuestionResponse>, t: Throwable) {
				questions.postValue(null)
			}
		})
		return questions
	}
}