package com.flobiz.app.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.flobiz.app.R
import com.flobiz.app.databinding.ActivityMainBinding
import com.flobiz.app.model.Question
import com.flobiz.app.model.QuestionResponse
import com.flobiz.app.model.Tag
import com.flobiz.app.repository.QuestionRepository
import com.flobiz.app.ui.adapter.QuestionAdapter
import com.flobiz.app.ui.base.BaseActivity
import com.flobiz.app.ui.contract.TagCheckedListener
import com.flobiz.app.ui.viewmodel.QuestionViewModel
import com.flobiz.app.ui.viewmodel.QuestionViewModelFactory
import com.flobiz.app.util.Constants
import com.flobiz.app.webservice.WebServiceClient

class MainActivity : BaseActivity(), TagCheckedListener {

	private var checkedIndex: Int = -1

	private lateinit var binding: ActivityMainBinding
	private lateinit var questionViewModel: QuestionViewModel
	private val adapter = QuestionAdapter(this, arrayListOf())

	private var tagList: ArrayList<Tag> = arrayListOf()
	private var filteredList: ArrayList<Question> = arrayListOf()
	private var originalList: ArrayList<Question> = arrayListOf()

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

			if (response.items.isEmpty())
				binding.txtNoResult.visibility = View.VISIBLE

			binding.progressBar.visibility = View.GONE

			response.items.forEach { question ->
				question.tags.forEach { tag ->
					tagList.add(Tag(tag, MutableLiveData(false)))
				}
			}

			questionViewModel.getAverageCount(response).observe(this, { list ->
				binding.avgCountContainer.visibility = View.VISIBLE
				binding.lblAvgViewCount.text = list[0]
				binding.lblAvgAnsCount.text = list[1]
			})

			binding.rvQuestions.also {
				originalList.clear()
				originalList.addAll(response.items)
				adapter.setList(response.items)
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

				questionViewModel.getAverageCount(QuestionResponse(originalList))
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
			questionViewModel.getAverageCount(QuestionResponse(filteredList))
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
