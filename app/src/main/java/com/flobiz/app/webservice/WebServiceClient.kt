package com.flobiz.app.webservice

import android.content.Context
import com.flobiz.app.MyApplication
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class WebServiceClient(private var context: Context) {

	private val BASE_URL = "https://api.stackexchange.com/2.2/"

	private val gson: Gson by lazy {
		GsonBuilder().setLenient().create()
	}

	private val cacheSize = (5 * 1024 * 1024).toLong()

	private fun cache(): Cache {
		return Cache(File(context.cacheDir, "someIdentifier"), cacheSize)
	}

	private var onlineInterceptor: Interceptor = object : Interceptor {
		override fun intercept(chain: Interceptor.Chain): Response {
			val response: Response = chain.proceed(chain.request())
			val maxAge = 5
			return response.newBuilder()
				.header("Cache-Control", "public, max-age=$maxAge")
				.removeHeader("Pragma")
				.build()
		}
	}

	private var offlineInterceptor: Interceptor = object : Interceptor {
		override fun intercept(chain: Interceptor.Chain): Response {
			var request: Request = chain.request()
			if (!MyApplication.hasNetwork()) {
				val maxStale = 60 * 60 * 24 * 30
				request = request.newBuilder()
					.header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
					.removeHeader("Pragma")
					.build()
			}
			return chain.proceed(request)
		}
	}

	private var okHttpClient: OkHttpClient = OkHttpClient.Builder()
		.cache(cache())
		.addNetworkInterceptor(onlineInterceptor)
		.addInterceptor(offlineInterceptor)
		.build()

	private val retrofit: Retrofit by lazy {
		Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build()
	}

	val apiInterface: WebService by lazy {
		retrofit.create(WebService::class.java)
	}
}