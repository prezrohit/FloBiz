package com.flobiz.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.flobiz.app.R
import com.flobiz.app.databinding.RowQuestionBinding
import com.flobiz.app.main.FloBizApplication
import com.flobiz.app.model.Question
import com.flobiz.app.ui.contract.MainActivityContract
import com.flobiz.app.ui.presenter.MainActivityPresenter

class QuestionAdapter(private val context: Context, private var questionList: List<Question>) :
	RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>(), MainActivityContract.View {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {

		val binding: RowQuestionBinding = DataBindingUtil.inflate(
			LayoutInflater.from(parent.context),
			R.layout.row_question,
			parent,
			false
		)

		val presenter = MainActivityPresenter(this)
		binding.setVariable(BR.presenter, presenter)

		return QuestionViewHolder(binding)
	}

	override fun getItemCount(): Int = questionList.size

	override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
		holder.bind(questionList[position])
	}

	fun setList(questionList: List<Question>) {
		this.questionList = questionList
		notifyDataSetChanged()
	}

	inner class QuestionViewHolder(private val binding: RowQuestionBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: Question) {

			binding.question = item
		}
	}

	override fun onClickItem(question: Question?) {
		if (FloBizApplication.hasNetwork()) {
			val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(question?.link))
			context.startActivity(browserIntent)

		} else Toast.makeText(context, "You are not connected to the Internet", Toast.LENGTH_SHORT)
			.show()
	}
}
