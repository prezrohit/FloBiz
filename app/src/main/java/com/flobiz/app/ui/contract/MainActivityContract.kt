package com.flobiz.app.ui.contract

import com.flobiz.app.model.Question

interface MainActivityContract {
	interface Presenter {
		fun onClickItem(question: Question?)
	}

	interface View {
		fun onClickItem(question: Question?)
	}
}