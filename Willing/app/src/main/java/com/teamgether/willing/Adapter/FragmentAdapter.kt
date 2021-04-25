package com.teamgether.willing.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.teamgether.willing.Fragment.FollowFragment
import com.teamgether.willing.Fragment.RecentFragment

class FragmentAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment = when (position)
        {
            0 -> FollowFragment().newInstance()
            1 -> RecentFragment().newInstance()
            else -> RecentFragment().newInstance()
        }
        return fragment
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position) {
            0 -> "팔로우순"
            1 -> "최신순"
            else -> "최신순"
        }
        return title
    }

}