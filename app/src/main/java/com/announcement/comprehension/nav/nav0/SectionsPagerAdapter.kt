package com.announcement.comprehension.nav.nav0

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.announcement.comprehension.R


class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3
    )

    override fun getItem(position: Int): Fragment {
        return PlaceholderFragment.newInstance(position )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return TAB_TITLES.size
    }
}