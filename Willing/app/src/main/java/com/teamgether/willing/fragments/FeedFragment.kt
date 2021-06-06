package com.teamgether.willing.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.teamgether.willing.R
import com.teamgether.willing.databinding.FragmentFeedBinding
import com.teamgether.willing.viewmodels.FeedViewModel
import com.teamgether.willing.view.OtherDetailActivity

class  FeedFragment : Fragment() {

    private var current: String = "follow"
    private lateinit var binding: FragmentFeedBinding
    private lateinit var viewModel: FeedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)
        viewModel = ViewModelProvider(this).get(FeedViewModel::class.java)

        viewModel.detail.observe(viewLifecycleOwner, Observer {
            val intent = Intent(context, OtherDetailActivity::class.java).apply {
                putExtra("pictureUrl", it[1])
                putExtra("challengeId", it[0])
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            startActivity(intent)
        })

        binding.lifecycleOwner = this
        binding.feedViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.feedList.layoutManager = GridLayoutManager(view.context, 3)
        viewModel.getData(current, binding.feedList)

        binding.feedFollowBtn.setOnClickListener {
            current = "follow"
            changeBtnStatus(current, view)
            viewModel.getData(current,binding.feedList)
        }

        binding.feedRecentBtn.setOnClickListener {
            current = "recent"
            changeBtnStatus(current, view)
            viewModel.getData(current,binding.feedList)
        }

        binding.feedSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getData(current,binding.feedList)
            binding.feedSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun changeBtnStatus(current: String, view: View) {
        when (current) {
            "follow" -> {
                binding.feedFollowBtn.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.colorPrimary
                    )
                )
                binding.feedRecentBtn.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.black
                    )
                )
            }
            "recent" -> {
                binding.feedRecentBtn.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.colorPrimary
                    )
                )
                binding.feedFollowBtn.setTextColor(
                    ContextCompat.getColor(
                        view.context,
                        R.color.black
                    )
                )
            }
        }
    }
}