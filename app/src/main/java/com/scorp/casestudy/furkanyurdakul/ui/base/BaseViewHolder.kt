package com.scorp.casestudy.furkanyurdakul.ui.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T, VDB: ViewDataBinding>(protected val binding: VDB)
    : RecyclerView.ViewHolder(binding.root)
{
    abstract fun bind(item: T)
}