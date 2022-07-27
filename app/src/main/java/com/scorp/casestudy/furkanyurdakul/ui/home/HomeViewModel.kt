package com.scorp.casestudy.furkanyurdakul.ui.home

import com.scorp.casestudy.furkanyurdakul.data.service.DataSource
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataSource: DataSource
): BaseViewModel()
{

}