package com.scorp.casestudy.furkanyurdakul.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.scorp.casestudy.furkanyurdakul.data.model.Person
import com.scorp.casestudy.furkanyurdakul.databinding.ItemPersonBinding
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseViewHolder

class PersonListAdapter: PagingDataAdapter<Person, ViewHolder>(PersonComparator)
{
    private lateinit var layoutInflater: LayoutInflater

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        if (!this::layoutInflater.isInitialized)
            layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(ItemPersonBinding.inflate(layoutInflater, parent, false))
    }
}

class ViewHolder(binding: ItemPersonBinding): BaseViewHolder<Person, ItemPersonBinding>(binding)
{
    override fun bind(item: Person)
    {
        binding.item = item
    }
}

object PersonComparator: DiffUtil.ItemCallback<Person>()
{
    override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
}