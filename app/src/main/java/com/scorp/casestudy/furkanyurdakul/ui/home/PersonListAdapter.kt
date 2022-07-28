package com.scorp.casestudy.furkanyurdakul.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.data.model.Person
import com.scorp.casestudy.furkanyurdakul.databinding.ItemLoadStateMatchBinding
import com.scorp.casestudy.furkanyurdakul.databinding.ItemLoadStateWrapBinding
import com.scorp.casestudy.furkanyurdakul.databinding.ItemPersonBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseDisplayItem
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseViewHolder
import com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems.FirstLoadStateDisplayItem
import com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems.ItemLoadStateDisplayItem
import com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems.PersonDisplayItem
import com.scorp.casestudy.furkanyurdakul.util.DataState

class PersonListAdapter(private val clickAction: (Boolean, Boolean) -> Unit)
    : ListAdapter<BaseDisplayItem, RecyclerView.ViewHolder>(PersonComparator)
{
    private lateinit var layoutInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        if (!this::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType)
        {
            R.layout.item_person ->
                PersonViewHolder(ItemPersonBinding.inflate(layoutInflater, parent, false))
            R.layout.item_load_state_match ->
                LoadStateMatchViewHolder(ItemLoadStateMatchBinding
                    .inflate(layoutInflater, parent, false), clickAction)
            R.layout.item_load_state_wrap ->
                LoadStateWrapViewHolder(ItemLoadStateWrapBinding
                    .inflate(layoutInflater, parent, false), clickAction)
            else -> throw IllegalArgumentException("Unknown view type: 0x${Integer.toHexString(viewType)}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        when (val item = getItem(position))
        {
            is FirstLoadStateDisplayItem -> (holder as LoadStateMatchViewHolder).bind(item.loadState)
            is ItemLoadStateDisplayItem -> (holder as LoadStateWrapViewHolder).bind(item.loadState)
            is PersonDisplayItem -> (holder as PersonViewHolder).bind(item.person)
        }
    }

    override fun getItemViewType(position: Int): Int
    {
        return getItem(position).layoutId
    }
}

class PersonViewHolder(binding: ItemPersonBinding): BaseViewHolder<Person, ItemPersonBinding>(binding)
{
    override fun bind(item: Person)
    {
        binding.item = item
    }
}

class LoadStateMatchViewHolder(binding: ItemLoadStateMatchBinding,
    private val clickAction: (Boolean, Boolean) -> Unit)
    : BaseViewHolder<DataState, ItemLoadStateMatchBinding>(binding)
{
    init
    {
        binding.tryAgainButton.setOnClickListener {
            binding.item?.let { item -> clickAction(item.isError, item.isEnded) }
        }
    }

    override fun bind(item: DataState)
    {
        binding.item = item
    }
}

class LoadStateWrapViewHolder(binding: ItemLoadStateWrapBinding,
    private val clickAction: (Boolean, Boolean) -> Unit)
    : BaseViewHolder<DataState, ItemLoadStateWrapBinding>(binding)
{
    init
    {
        binding.tryAgainButton.setOnClickListener {
            binding.item?.let { item -> clickAction(item.isError, item.isEnded) }
        }
    }

    override fun bind(item: DataState)
    {
        binding.item = item
    }
}

object PersonComparator: DiffUtil.ItemCallback<BaseDisplayItem>()
{
    override fun areItemsTheSame(oldItem: BaseDisplayItem, newItem: BaseDisplayItem) = oldItem == newItem
    override fun areContentsTheSame(oldItem: BaseDisplayItem, newItem: BaseDisplayItem) = oldItem == newItem
}