package com.scorp.casestudy.furkanyurdakul.ui.home

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.databinding.FragmentHomeBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseFragment
import com.scorp.casestudy.furkanyurdakul.util.addOnLastItemVisibleListener
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
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter

            addOnLastItemVisibleListener { isVisible ->
                if (isVisible)
                    viewModel.loadPeople()
            }
        }

        binding.swipeToRefreshLayout.setOnRefreshListener {
            withLifecycle {
                binding.swipeToRefreshLayout.isRefreshing = false
                viewModel.refreshPeople()
            }
        }

        viewModel.liveData.observe(viewLifecycleOwner) { list ->
            listAdapter.submitList(list)
        }

        viewModel.loadState.observe(viewLifecycleOwner) { state ->
            binding.swipeToRefreshLayout.isEnabled = !state.isLoading
        }

        viewModel.loadPeople()
    }
}