package com.flobiz.app.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.flobiz.app.R
import com.flobiz.app.databinding.RowTagBinding
import com.flobiz.app.model.Tag
import com.flobiz.app.ui.contract.TagItemContract
import com.flobiz.app.ui.presenter.TagItemPresenter

class TagAdapter(private var tags: List<Tag>) :
	RecyclerView.Adapter<TagAdapter.TagViewHolder>(), TagItemContract.View {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
		val binding: RowTagBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			R.layout.row_tag,
			parent,
			false
		)

		val presenter = TagItemPresenter(this)
		binding.setVariable(BR.presenter, presenter)

		return TagViewHolder(binding)
	}

	override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
		holder.bind(tags[position])
	}

	override fun getItemCount(): Int = tags.size

	inner class TagViewHolder(private val binding: RowTagBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(tag: Tag?) {
			binding.tag = tag
			binding.executePendingBindings()
		}
	}

	override fun onClickItem(tag: Tag?) {
		Log.d(TAG, "onClickItem: ${tag?.name}")
	}

	companion object {
		private const val TAG = "TagAdapter"
	}

}