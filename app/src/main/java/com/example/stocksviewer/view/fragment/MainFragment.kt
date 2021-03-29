package com.example.stocksviewer.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.stocksviewer.App
import com.example.stocksviewer.R
import com.example.stocksviewer.view.adapter.TabsFragmentPagerAdapter
import com.example.stocksviewer.viewmodels.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class MainFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    init {
        App.component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_main, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        var viewPager = view.findViewById<ViewPager2>(R.id.pager)
        var tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        var searchView = view.findViewById<SearchView>(R.id.searchView)
        var chipGroup = view.findViewById<ChipGroup>(R.id.chipGroup)
        var adapter = TabsFragmentPagerAdapter(this)
        viewPager.adapter = adapter

        var chipLayout = view.findViewById<LinearLayout>(R.id.chipLayout)
        setSearchIsVisible(false, tabLayout, viewPager, chipLayout)

        initSearchView(searchView, tabLayout, viewPager, chipLayout, chipGroup)

        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.Stocks)
                1 -> tab.text = getString(R.string.Favourite)
            }
        }.attach()


        setTabsTextSize(tabLayout)
    }

    private fun initSearchView(
        searchView: SearchView,
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        chipLayout: LinearLayout,
        chipGroup: ChipGroup
    ) {
        searchView.setOnQueryTextFocusChangeListener({ v, hasFocus ->
            if (hasFocus) {
                setSearchIsVisible(true, tabLayout, viewPager, chipLayout)
                chipGroup.removeAllViews()
                for (query in viewModel.submitedQuery.toSet()) {
                    createChip(query, searchView, chipGroup)
                }
            } else {
                setSearchIsVisible(false, tabLayout, viewPager, chipLayout)
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.handleSearchQuery(newText)
                setSearchIsVisible(false, tabLayout, viewPager, chipLayout)
                return true
            }
        })
    }

    private fun createChip(
        text: String,
        searchView: SearchView,
        chipGroup: ChipGroup
    ) {
        val chip = Chip(this.context)
        chip.text = text
        chip.setOnClickListener { searchView.setQuery(text, true) }
        chipGroup.addView(chip)
    }

    private fun setTabsTextSize(tabLayout: TabLayout) {
        var secondTab = tabLayout.getTabAt(1)
        secondTab?.let { setTextSize(it, 18f) }
        tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                setTextSize(tab, 28f)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                setTextSize(tab, 18f)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setSearchIsVisible(
        isVisible: Boolean,
        tabLayout: TabLayout,
        viewPager: ViewPager2,
        chipsLayout: LinearLayout
    ) {
        setIsVisible(!isVisible, tabLayout, viewPager)
        setIsVisible(isVisible, chipsLayout)
    }

    private fun setIsVisible(isVisible: Boolean, vararg views: View) {
        for (view in views) {
            view.isVisible = isVisible
        }
    }

    private fun setTextSize(tab: TabLayout.Tab, textSize: Float) {
        val views = arrayListOf<View>()
        tab.view.findViewsWithText(views, tab.text, View.FIND_VIEWS_WITH_TEXT)
        views.forEach { view ->
            if (view is TextView) {
                view.textSize = textSize
            }
        }
    }
}

