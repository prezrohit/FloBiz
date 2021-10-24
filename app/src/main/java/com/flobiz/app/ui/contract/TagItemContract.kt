package com.flobiz.app.ui.contract

import com.flobiz.app.model.Tag

interface TagItemContract {

	interface Presenter {
		fun onClickItem(tag: Tag?)
	}

	interface View {
		fun onClickItem(tag: Tag?)
	}
}
