package com.scorp.casestudy.furkanyurdakul.ui.home

import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.databinding.FragmentHomeBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>()
{
    override val layoutId: Int = R.layout.fragment_home

    override suspend fun setupUi()
    {

    }
}