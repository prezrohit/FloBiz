package com.flobiz.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flobiz.app.databinding.LayoutBottomSheetBinding
import com.flobiz.app.model.Tag
import com.flobiz.app.ui.adapter.TagAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment(
	private var tagList: List<Tag>
) : BottomSheetDialogFragment() {

	private lateinit var adapter: TagAdapter
	private lateinit var binding: LayoutBottomSheetBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = LayoutBottomSheetBinding.inflate(inflater, container, false)

		val flexBoxLayoutManager = FlexboxLayoutManager(context)
		flexBoxLayoutManager.flexDirection = FlexDirection.ROW
		flexBoxLayoutManager.justifyContent = JustifyContent.SPACE_AROUND

		binding.rvTags.layoutManager = flexBoxLayoutManager

		adapter = TagAdapter(tagList)

		binding.rvTags.also { rvTag ->
			rvTag.setHasFixedSize(true)
			rvTag.adapter = adapter
		}

		return binding.root
	}

	companion object {
		const val TAG = "BottomSheetFragment"
	}
}