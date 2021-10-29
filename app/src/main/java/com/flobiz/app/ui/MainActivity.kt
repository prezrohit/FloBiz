package com.flobiz.app.ui

import android.os.Bundle
import android.os.Handler
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
import kotlin.collections.HashSet
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast


class MainActivity : BaseActivity(), TagCheckedListener {

	private var checkedIndex: Int = -1
	private var doubleBackToExitPressedOnce: Boolean = false

	private lateinit var binding: ActivityMainBinding
	private lateinit var questionViewModel: QuestionViewModel
	private val adapter = MainAdapter(this, arrayListOf())

	private var tagList: ArrayList<Tag> = arrayListOf()
	private var originalList: ArrayList<Any> = arrayListOf()
	private var filteredList: ArrayList<Question> = arrayListOf()

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

		binding.swipeRefreshLayout.isRefreshing = true

		loadList()

		binding.rvQuestions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				if (dy > 0 || dy < 0 && binding.fabFilter.isShown)
					binding.fabFilter.hide()
			}

			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				if (newState == RecyclerView.SCROLL_STATE_IDLE)
					binding.fabFilter.show()

				super.onScrollStateChanged(recyclerView, newState)
			}
		})

		binding.swipeRefreshLayout.setOnRefreshListener {
			loadList()
		}
	}

	private fun loadList() {
		questionViewModel.fetchAllQuestions(
			Constants.key,
			Constants.order,
			Constants.sort,
			Constants.site

		).observe(this, { response ->

			binding.swipeRefreshLayout.isRefreshing = false

			checkedIndex = -1

			if (response.items.isEmpty()) {
				binding.txtNoResult.visibility = View.VISIBLE

			} else {
				tagList.clear()
				val tagSet: HashSet<String> = HashSet()
				response.items.forEach { question ->
					question.tags.forEach { tag ->
						tagSet.add(tag)
					}
				}

				tagSet.forEach { tag ->
					tagList.add(Tag(tag, MutableLiveData(false)))
				}

				questionViewModel.getAverageCount(response.items).observe(this, { list ->
					binding.avgCountContainer.visibility = View.VISIBLE
					binding.lblAvgViewCount.text = list[0]
					binding.lblAvgAnsCount.text = list[1]
				})

				originalList.clear()
				originalList.addAll(response.items)

				val imageUrl =
					"https://miro.medium.com/max/1400/1*-WcKNO3KnP2u3bsiNt1tXw.png"

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

	override fun onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}

		doubleBackToExitPressedOnce = true
		Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

		Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
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

	fun onClickFab(view: View) {
		val bottomSheetFragment = BottomSheetFragment(handleCheckedTag(), this)
		bottomSheetFragment.show(supportFragmentManager, BottomSheetFragment.TAG)
	}

	private fun handleCheckedTag(): List<Tag> {
		val newList: ArrayList<Tag> = arrayListOf()
		var temp = checkedIndex
		tagList.forEach { tag ->
			if (temp == 0) newList.add(Tag(tag.name, MutableLiveData(true)))
			else newList.add(tag)
			temp--
		}
		return newList
	}

	override fun onTagChecked(index: Int) {
		checkedIndex = index
	}

	companion object {
		private const val TAG = "MainActivity"
	}
}

