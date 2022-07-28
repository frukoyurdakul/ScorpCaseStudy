package com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems

import com.scorp.casestudy.furkanyurdakul.R
import com.scorp.casestudy.furkanyurdakul.data.model.Person
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseDisplayItem

data class PersonDisplayItem(val person: Person): BaseDisplayItem(R.layout.item_person)