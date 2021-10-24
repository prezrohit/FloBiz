package com.flobiz.app.ui.presenter

import com.flobiz.app.model.Tag
import com.flobiz.app.ui.contract.TagItemContract

class TagItemPresenter(private val view: TagItemContract.View) : TagItemContract.Presenter {
	override fun onClickItem(tag: Tag?) {
		view.onClickItem(tag)
	}
}