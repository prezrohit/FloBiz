package com.flobiz.app.webservice

import com.flobiz.app.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WebServiceClient {

	private const val BASEURL = "https://api.stackexchange.com/2.2/"

	private val retrofitClient: Retrofit.Builder by lazy {

		val levelType: HttpLoggingInterceptor.Level
		if (BuildConfig.BUILD_TYPE.contentEquals("debug"))
			levelType = HttpLoggingInterceptor.Level.BODY else levelType =
			HttpLoggingInterceptor.Level.NONE

		val logging = HttpLoggingInterceptor()
		logging.setLevel(levelType)

		val okhttpClient = OkHttpClient.Builder()
		okhttpClient.addInterceptor(logging)

		Retrofit.Builder()
			.baseUrl(BASEURL)
			.client(okhttpClient.build())
			.addConverterFactory(GsonConverterFactory.create())
	}

	val apiInterface: WebService by lazy {
		retrofitClient
			.build()
			.create(WebService::class.java)
	}
}