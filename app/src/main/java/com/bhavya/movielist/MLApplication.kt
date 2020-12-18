package com.bhavya.movielist

import android.app.Application
import com.bhavya.movielist.data.network.ApiService
import com.bhavya.movielist.data.network.NetworkInterceptor
import com.bhavya.movielist.data.repository.SearchRepository
import com.bhavya.movielist.ui.search.SearchViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MLApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MLApplication))

        bind() from singleton { NetworkInterceptor(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind() from singleton { SearchRepository(instance()) }
        bind() from provider { SearchViewModelFactory(instance())}
    }

}


