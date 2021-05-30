package com.teamgether.willing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamgether.willing.R
import com.teamgether.willing.databinding.FragmentFriendsBinding
import com.teamgether.willing.viewmodels.FriendsViewModel

class FriendsFragment : Fragment() {

    private lateinit var binding: FragmentFriendsBinding
    private lateinit var viewModel: FriendsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends, container, false)
        viewModel = ViewModelProvider(this).get(FriendsViewModel::class.java)

        binding.lifecycleOwner = this
        binding.friends = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(view.context)
        viewModel.loadData(binding.friendsRecyclerView)
    }

}