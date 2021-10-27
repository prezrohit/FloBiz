package com.flobiz.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import com.flobiz.app.databinding.LayoutBottomSheetBinding
import com.flobiz.app.model.Tag
import com.flobiz.app.ui.contract.TagCheckedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class BottomSheetFragment(
	private var list: List<Tag>,
	private val tagCheckedListener: TagCheckedListener
) : BottomSheetDialogFragment() {

	private lateinit var binding: LayoutBottomSheetBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = LayoutBottomSheetBinding.inflate(inflater, container, false)

		binding.chipGroup.isSingleSelection = true


		list.forEach { tag ->
			val chip = Chip(context)
			chip.text = tag.name
			chip.isCheckable = true
			tag.isChecked.observe(this, {
				chip.isChecked = it
			})
			binding.chipGroup.addView(chip)
		}

		binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
			/*Log.d(TAG, "onCreateView: $checkedId")
			if (checkedId >= 0)
				Log.d(TAG, "from List: ${list[checkedId - 1].name}")
*/
			tagCheckedListener.onTagChecked(checkedId)
		}

		Log.d(TAG, "group size: ${binding.chipGroup.size}")

		return binding.root
	}

	companion object {
		const val TAG = "BottomSheetFragment"
	}
}