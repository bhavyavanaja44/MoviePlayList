package com.bhavya.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bhavya.movielist.data.model.Movie
import com.bhavya.movielist.data.repository.SearchRepository
import com.bhavya.movielist.ui.search.SearchViewModel
import com.bhavya.util.LifeCycleTestOwner
import com.bhavya.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.*
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

/**
 * viewModel unit test
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    companion object {
        private const val SEARCH_TITLE = "test"
        private const val KEY = "726e21d9"
        private const val PAGE_INDEX = 1
        private const val TOTAL_RESULTS = "10"
        private const val RESPONSE = "True"
        private const val ERROR = "false"

    }

    @get:Rule
    val coroutineTestRule = TestCoroutineRule()
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val searchRepository: SearchRepository = mock()

    private lateinit var lifeCycleTestOwner: LifeCycleTestOwner
    private lateinit var searchViewModel: SearchViewModel

    private val searchItem: Movie.SearchItem by lazy {
        Movie.SearchItem(
            type = "movie",
            imdbID = "123",
            year = "1999",
            poster = "test",
            title = "test"
        )
    }

    @Before
    fun setUp() {
        lifeCycleTestOwner = LifeCycleTestOwner()
        lifeCycleTestOwner.onCreate()
        searchViewModel = SearchViewModel(searchRepository)
    }

    @After
    fun tearDown() {
        lifeCycleTestOwner.onDestroy()
    }

    @Test
    fun `getMovie shows the movies after it was successfully fetched`() {
        coroutineTestRule.runBlockingTest {
            // Given
            lifeCycleTestOwner.onResume()
            val movie = Movie(response = RESPONSE, error = ERROR, totalResults = TOTAL_RESULTS, search = arrayListOf(searchItem))
            When calling searchRepository.getMovies(SEARCH_TITLE, KEY, PAGE_INDEX) itReturns movie

            // When
            searchViewModel.getMovies()

            //then
           Verify on searchRepository that searchRepository.getMovies(SEARCH_TITLE, KEY, PAGE_INDEX) was called

        }
    }
}