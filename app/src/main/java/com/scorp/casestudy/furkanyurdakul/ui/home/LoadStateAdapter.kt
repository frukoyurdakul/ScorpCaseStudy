package com.scorp.casestudy.furkanyurdakul.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.scorp.casestudy.furkanyurdakul.databinding.ItemLoadStateBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseViewHolder
import com.scorp.casestudy.furkanyurdakul.util.DataLoadState
import com.scorp.casestudy.furkanyurdakul.util.toDataLoadState

class PersonLoadStateAdapter: LoadStateAdapter<LoadStateViewHolder>()
{
    private lateinit var layoutInflater: LayoutInflater

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState)
    {
        holder.bind(loadState.toDataLoadState())
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder
    {
        if (!this::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)

        return LoadStateViewHolder(ItemLoadStateBinding.inflate(layoutInflater, parent, false))
    }
}

class LoadStateViewHolder(binding: ItemLoadStateBinding)
    : BaseViewHolder<DataLoadState, ItemLoadStateBinding>(binding)
{
    override fun bind(item: DataLoadState)
    {
        binding.item = item
    }
}