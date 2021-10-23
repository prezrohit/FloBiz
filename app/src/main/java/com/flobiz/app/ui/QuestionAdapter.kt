package com.flobiz.app.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flobiz.app.R
import com.flobiz.app.databinding.RowQuestionBinding
import com.flobiz.app.model.Question
import com.flobiz.app.ui.contract.MainActivityContract
import com.flobiz.app.ui.presenter.MainActivityPresenter
import java.util.*
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat


class QuestionAdapter(private val context : Context, private var questionList: List<Question>) :
	RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>(), MainActivityContract.View {

	private val TAG = "QuestionAdapter"

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {

		val binding: RowQuestionBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			R.layout.row_question,
			parent,
			false
		)

		val presenter = MainActivityPresenter(this, binding.root.context)
		binding.setVariable(BR.presenter, presenter)

		return QuestionViewHolder(binding)
	}

	override fun getItemCount(): Int = questionList.size

	override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
		holder.bind(questionList[position])
	}

	inner class QuestionViewHolder(private val binding: RowQuestionBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: Question) {

			binding.question = item
			binding.executePendingBindings()
		}
	}

	override fun onClickItem(question: Question?) {
		val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(question?.link))
		context.startActivity(browserIntent)
	}
}
