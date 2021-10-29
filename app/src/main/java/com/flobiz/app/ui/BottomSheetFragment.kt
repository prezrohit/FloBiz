package com.flobiz.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.flobiz.app.R
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

		list.forEachIndexed { index, tag ->
			val chip = layoutInflater.inflate(
				R.layout.layout_custom_chip,
				binding.chipGroup,
				false

			) as Chip

			chip.text = tag.name
			tag.isChecked.observe(this, {
				chip.isChecked = it
			})
			chip.id = index
			binding.chipGroup.addView(chip)
		}

		binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
			tagCheckedListener.onTagChecked(checkedId)
		}

		binding.btnClearFilter.setOnClickListener {
			if (binding.chipGroup.checkedChipId >= 0)
				list[binding.chipGroup.checkedChipId].isChecked.postValue(false)
			tagCheckedListener.onTagChecked(-1)
		}

		return binding.root
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialog)
	}

	companion object {
		const val TAG = "BottomSheetFragment"
	}


}
