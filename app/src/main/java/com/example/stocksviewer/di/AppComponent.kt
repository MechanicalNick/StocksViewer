package com.example.stocksviewer.di

import android.app.Application
import com.example.stocksviewer.model.AppDatabase
import com.example.stocksviewer.model.DAO.QuoteDAO
import com.example.stocksviewer.model.MainRepository
import com.example.stocksviewer.model.service.MboumService
import com.example.stocksviewer.view.fragment.DetailsFragment
import com.example.stocksviewer.view.fragment.MainFragment
import com.example.stocksviewer.view.fragment.TabsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        NetworkModule::class,
        RoomModule::class,
        ViewModelModule::class]
)
interface AppComponent {
    fun inject(mainFragment: MainFragment)

    fun inject(tabsFragment: TabsFragment)

    fun inject(detailsFragment: DetailsFragment)

    fun providesDao(): QuoteDAO?

    fun appDatabase(): AppDatabase

    fun mainRepository(): MainRepository?

    fun application(): Application?

    fun mboumService(): MboumService
}
