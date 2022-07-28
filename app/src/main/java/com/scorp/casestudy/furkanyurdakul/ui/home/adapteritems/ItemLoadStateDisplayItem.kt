package com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems

import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseDisplayItem
import com.scorp.casestudy.furkanyurdakul.util.DataState

data class ItemLoadStateDisplayItem(val loadState: DataState)
    : BaseDisplayItem(R.layout.item_load_state_wrap)