package com.flobiz.app.ui.presenter

import android.content.Context
import com.flobiz.app.model.Question
import com.flobiz.app.ui.contract.MainActivityContract

class MainActivityPresenter(private val view: MainActivityContract.View) :
	MainActivityContract.Presenter {

	override fun onClickItem(question: Question?) {
		view.onClickItem(question)
	}
}