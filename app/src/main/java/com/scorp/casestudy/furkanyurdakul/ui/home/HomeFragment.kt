package com.scorp.casestudy.furkanyurdakul.ui.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.databinding.FragmentHomeBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseFragment
import com.scorp.casestudy.furkanyurdakul.util.withLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>()
{
    override val layoutId: Int = R.layout.fragment_home

    private val viewModel: HomeViewModel by viewModels()

    override suspend fun setupUi()
    {
        val listAdapter = PersonListAdapter()
        with (binding.recyclerView)
        {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        withLifecycle {
            binding.swipeToRefreshLayout.isRefreshing = false
        }
    }
}