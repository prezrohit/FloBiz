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
import com.flobiz.app.databinding.RowAdBinding
import com.flobiz.app.databinding.RowQuestionBinding
import com.flobiz.app.main.FloBizApplication
import com.flobiz.app.model.Ad
import com.flobiz.app.model.Question
import com.flobiz.app.ui.contract.MainActivityContract
import com.flobiz.app.ui.presenter.MainActivityPresenter

class MainAdapter(private val context: Context, private var list: ArrayList<Any>) :
	RecyclerView.Adapter<RecyclerView.ViewHolder>(), MainActivityContract.View {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

		if (viewType == 0) {
			val binding: RowAdBinding = DataBindingUtil.inflate(
				LayoutInflater.from(parent.context),
				R.layout.row_ad,
				parent,
				false
			)
			return AdViewHolder(binding)

		} else {

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
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (getItemViewType(position) == 0) {
			val adViewHolder: AdViewHolder = holder as AdViewHolder
			adViewHolder.bind(list[position] as Ad, position)

		} else {
			val questionViewHolder: QuestionViewHolder = holder as QuestionViewHolder
			questionViewHolder.bind(list[position] as Question)
		}
	}

	override fun getItemViewType(position: Int): Int {
		return if (list[position] is Ad) 0 else 1
	}

	override fun getItemCount(): Int = list.size

	fun setList(list: List<Any>) {
		this.list.clear()
		this.list.addAll(list)
		notifyDataSetChanged()
	}

	override fun onClickItem(question: Question?) {
		if (FloBizApplication.hasNetwork()) {
			val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(question?.link))
			context.startActivity(browserIntent)

		} else Toast.makeText(
			context,
			"You are not connected to the Internet",
			Toast.LENGTH_SHORT

		).show()
	}

	inner class QuestionViewHolder(private val binding: RowQuestionBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: Question) {
			binding.question = item
		}
	}

	inner class AdViewHolder(private val binding: RowAdBinding) :
		RecyclerView.ViewHolder(binding.root) {

		fun bind(item: Ad, position: Int) {
			binding.btnClose.setOnClickListener {
				list.removeAt(position)
				notifyItemRemoved(position)
				notifyItemRangeChanged(position, list.size);
			}
			binding.ad = item
		}
	}
}
