package com.flobiz.app.webservice

import com.flobiz.app.model.QuestionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

	@GET("questions")
	fun fetchQuestions(
		@Query("key") key: String,
		@Query("order") order: String,
		@Query("sort") sort: String,
		@Query("site") site: String

	) : Call<QuestionResponse>
}