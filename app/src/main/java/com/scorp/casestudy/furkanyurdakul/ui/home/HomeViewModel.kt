package com.scorp.casestudy.furkanyurdakul.ui.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.scorp.casestudy.furkanyurdakul.data.model.Person
import com.scorp.casestudy.furkanyurdakul.data.service.PersonPagingSource
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val personPagingSource: PersonPagingSource
): BaseViewModel()
{
    fun loadPeople(): Flow<PagingData<Person>>
    {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { personPagingSource }
        ).flow.cachedIn(viewModelScope)
    }
}