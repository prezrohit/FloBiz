package com.flobiz.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.flobiz.app.databinding.LayoutBottomSheetBinding
import com.flobiz.app.model.Question
import com.flobiz.app.model.Tag
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BottomSheetFragment(
	private var list: List<Question>
) : BottomSheetDialogFragment() {

	private var tagList: ArrayList<Tag> = arrayListOf()
	private lateinit var binding: LayoutBottomSheetBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = LayoutBottomSheetBinding.inflate(inflater, container, false)

		binding.chipGroup.isSingleSelection = true
		list.forEach { question ->
			question.tags.forEach { tag ->
				tagList.add(Tag(tag, MutableLiveData(false)))
			}
		}

		tagList.forEach { tag ->
			val chip = Chip(context)
			chip.text = tag.name
			chip.isCheckable = true
			tag.isChecked.observe(this, {
				chip.isChecked = it
			})
			binding.chipGroup.addView(chip)
		}

		binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
			Log.d(TAG, "onCreateView: ${binding.chipGroup.checkedChipId}" )
		}

		return binding.root
	}

	companion object {
		const val TAG = "BottomSheetFragment"
	}
}