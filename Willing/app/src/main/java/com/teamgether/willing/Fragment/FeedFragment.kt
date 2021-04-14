package com.teamgether.willing.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.teamgether.willing.Adapter.FragmentAdapter
import com.teamgether.willing.R

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = FragmentAdapter(activity!!.supportFragmentManager)
        val pager = view.findViewById<ViewPager>(R.id.viewPager)
        pager.adapter = pagerAdapter
        val tab = view.findViewById<TabLayout>(R.id.tabLayout)
        tab.setupWithViewPager(pager)

    }
}