package com.example.stocksviewer.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.stocksviewer.view.fragment.MainFragment
import com.example.stocksviewer.view.fragment.TabsFragment

class TabsFragmentPagerAdapter(mainFragment: MainFragment) :
    FragmentStateAdapter(mainFragment) {

    override fun getItemCount(): Int {
        return ITEM_SIZE
    }

    override fun createFragment(position: Int): Fragment {
        return TabsFragment.newInstance(position)
    }

    companion object {
        private const val ITEM_SIZE = 2
    }
}