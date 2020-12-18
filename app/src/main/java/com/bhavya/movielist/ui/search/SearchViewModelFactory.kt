package com.bhavya.movielist.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bhavya.movielist.data.repository.SearchRepository

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(
    private val repository: SearchRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}