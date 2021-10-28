package com.flobiz.app.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.flobiz.app.R
import com.flobiz.app.databinding.ActivityMainBinding
import com.flobiz.app.model.Ad
import com.flobiz.app.model.Question
import com.flobiz.app.model.Tag
import com.flobiz.app.repository.QuestionRepository
import com.flobiz.app.ui.adapter.MainAdapter
import com.flobiz.app.ui.base.BaseActivity
import com.flobiz.app.ui.contract.TagCheckedListener
import com.flobiz.app.ui.viewmodel.QuestionViewModel
import com.flobiz.app.ui.viewmodel.QuestionViewModelFactory
import com.flobiz.app.util.Constants
import com.flobiz.app.webservice.WebServiceClient
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), TagCheckedListener {

	private var checkedIndex: Int = -1

	private lateinit var binding: ActivityMainBinding
	private lateinit var questionViewModel: QuestionViewModel
	private val adapter = MainAdapter(this, arrayListOf())

	private var tagList: ArrayList<Tag> = arrayListOf()
	private var filteredList: ArrayList<Question> = arrayListOf()
	private var originalList: ArrayList<Any> = arrayListOf()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

		setSupportActionBar(binding.toolbar)

		val questionRepository = QuestionRepository(WebServiceClient(this).apiInterface)
		questionViewModel =
			ViewModelProvider(
				this,
				QuestionViewModelFactory(questionRepository)
			)[QuestionViewModel::class.java]

		binding.rvQuestions.also {
			it.setHasFixedSize(true)
			it.adapter = adapter
		}

		questionViewModel.fetchAllQuestions(
			Constants.key,
			Constants.order,
			Constants.sort,
			Constants.site

		).observe(this, { response ->

			binding.progressBar.visibility = View.GONE

			if (response.items.isEmpty())
				binding.txtNoResult.visibility = View.VISIBLE

			else {
				response.items.forEach { question ->
					question.tags.forEach { tag ->
						tagList.add(Tag(tag, MutableLiveData(false)))
					}
				}

				questionViewModel.getAverageCount(response.items).observe(this, { list ->
					binding.avgCountContainer.visibility = View.VISIBLE
					binding.lblAvgViewCount.text = list[0]
					binding.lblAvgAnsCount.text = list[1]
				})

				originalList.clear()
				originalList.addAll(response.items)

				val imageUrl =
					"data:image/jpeg;base64,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"

				originalList.add(Ad(imageUrl, "Ad 1"))
				originalList.add(Ad(imageUrl, "Ad 2"))
				originalList.add(Ad(imageUrl, "Ad 3"))
				originalList.shuffle()

				adapter.setList(originalList)
			}
		})
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu, menu)
		val item = menu?.findItem(R.id.action_search)
		val searchView = item?.actionView as SearchView

		val filterItem = menu.findItem(R.id.action_filter)
		filterItem.setOnMenuItemClickListener {
			val bottomSheetFragment = BottomSheetFragment(handleCheckedTag(), this)
			bottomSheetFragment.show(supportFragmentManager, BottomSheetFragment.TAG)
			
			return@setOnMenuItemClickListener true
		}

		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String?): Boolean {
				if (query != null && query.isNotEmpty()) {
					searchQuery(query)
					searchView.clearFocus()
				}
				return true
			}

			override fun onQueryTextChange(query: String?): Boolean {
				searchQuery(query)
				return true
			}
		})

		item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
			override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
				searchView.setQuery("", false)
				binding.rvQuestions.visibility = View.VISIBLE
				binding.txtNoResult.visibility = View.GONE
				adapter.setList(originalList)

				questionViewModel.getAverageCount(originalList)
					.observe(this@MainActivity, { list ->
						binding.avgCountContainer.visibility = View.VISIBLE
						binding.lblAvgViewCount.text = list[0]
						binding.lblAvgAnsCount.text = list[1]
					})

				return true
			}

			override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
				if (checkedIndex != -1) searchQuery("")
				return true
			}
		})

		return super.onCreateOptionsMenu(menu)
	}

	fun searchQuery(string: String?) {
		filteredList.clear()

		originalList.forEach { question ->
			if (question is Question)
				if ((checkedIndex == -1 || question.tags.contains(tagList[checkedIndex - 1].name))
					&& (question.title.contains(string!!, true)
							|| question.owner.display_name.contains(string, true))
				)
					filteredList.add(question)
		}

		if (filteredList.isEmpty()) {
			binding.avgCountContainer.visibility = View.GONE
			binding.txtNoResult.visibility = View.VISIBLE
			binding.rvQuestions.visibility = View.GONE

		} else {
			questionViewModel.getAverageCount(filteredList)
				.observe(this, { list ->
					binding.lblAvgViewCount.text = list[0]
					binding.lblAvgAnsCount.text = list[1]
				})
			binding.avgCountContainer.visibility = View.VISIBLE
			binding.rvQuestions.visibility = View.VISIBLE
			binding.txtNoResult.visibility = View.GONE
			adapter.setList(filteredList)
		}
	}

	private fun handleCheckedTag(): List<Tag> {
		val newList: ArrayList<Tag> = arrayListOf()
		var temp = checkedIndex
		tagList.forEach { tag ->
			temp--
			if (temp == 0) newList.add(Tag(tag.name, MutableLiveData(true)))
			else newList.add(tag)
		}
		return newList
	}

	override fun onTagChecked(index: Int) {
		checkedIndex = index

		if (checkedIndex >= tagList.size) {
			checkedIndex %= tagList.size
		}
	}

	companion object {
		private const val TAG = "MainActivity"
	}
}
