package com.bhavya.movielist.ui.search

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhavya.movielist.databinding.SearchActivityBinding
import com.bhavya.movielist.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import android.widget.ListView
import android.widget.ArrayAdapter
import com.bhavya.movielist.R

class SearchActivity : AppCompatActivity(), KodeinAware {
    private val TAG = SearchActivity::class.qualifiedName

    private lateinit var binding: SearchActivityBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var movieListAdapter:  MovieListAdapter
    private lateinit var searchView: SearchView
    override val kodein by kodein()
    private val factory: SearchViewModelFactory by instance()
    lateinit var adapter: ArrayAdapter<String>
    lateinit var listView: ListView
    lateinit var builder: AlertDialog.Builder
    lateinit var dialog: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.search_activity
        )

        initViewModel()
        initUi()
        initObserver()
        handleNetworkConnection()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.apply {
            queryHint = context.getString(R.string.search)
            isSubmitButtonEnabled = true
            onActionViewExpanded()
        }
        search(searchView)
        return true
    }

    private fun initUi() {
        movieListAdapter = MovieListAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieListAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    val visibleItemCount = layoutManager!!.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    viewModel.loadMoreItems(
                        visibleItemCount,
                        totalItemCount,
                        firstVisibleItemPosition
                    )
                }

            })
        }
        binding.fab.setOnClickListener {
            if (movieListAdapter.getSelected()?.size!! > 0) {
                var arrayIndices = movieListAdapter.getSelected()!!.indices
                val arrayList: ArrayList<String> = ArrayList()
                for (i in arrayIndices) {
                    arrayList.add(movieListAdapter.getSelected()!![i]?.title!!)
                }
                openDialog(arrayList)
            } else {
                binding.root.snackbar(getString(R.string.no_selection))

            }
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

    }

    private fun initObserver() {
        viewModel.movieNameLiveData.observe(this, Observer {
            Log.i(TAG, "Movie = $it")
        })
        viewModel.loadMoreListLiveData.observe(this, Observer {
            if (it) {
                movieListAdapter.setData(null)
                Handler().postDelayed({
                    viewModel.loadMore()
                }, 2000)
            }
        })
        viewModel.moviesLiveData.observe(this, Observer { state ->
            when (state) {
                is ViewState.Loading -> {
                    binding.recyclerview.hide()
                    binding.welcomeSearch.hide()
                    binding.progressBar.show()
                }
                is ViewState.Success -> {
                    binding.recyclerview.show()
                    binding.welcomeSearch.hide()
                    binding.progressBar.hide()
                    movieListAdapter.setData(state.data)
                }
                is ViewState.Error -> {
                    binding.progressBar.hide()
                    showToast(state.message)
                }
            }
        })
    }

    private fun handleNetworkConnection() {
            if (NetworkUtil.isNetworkAvail(applicationContext)) {
                if (viewModel.moviesLiveData.value is ViewState.Error || movieListAdapter.itemCount == 0) {
                    viewModel.getMovies()
                }
                Log.i("Info",getString(R.string.msg_connected))
            } else {
                showToast(getString(R.string.msg_no_network))
            }
    }


    private fun search(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                dismissKeyboard(searchView)
                searchView.clearFocus()
                viewModel.searchMovie(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun openDialog(arrayList: ArrayList<String>) {
        builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(getString(R.string.your_playlist))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                dialog.cancel()
            }
        val rowList: View = layoutInflater.inflate(R.layout.selected_playlist, null)
        listView = rowList.findViewById(R.id.list)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
        listView.adapter = adapter
        adapter.notifyDataSetChanged()
        builder.setView(rowList)
        dialog = builder.create()
        dialog.show()
    }

}
