package com.scorp.casestudy.furkanyurdakul.data

import com.scorp.casestudy.furkanyurdakul.data.model.Person

data class FetchResponse(val people: List<Person>, val next: String?)
data class FetchError(val errorDescription: String)
typealias FetchCompletionHandler = (FetchResponse?, FetchError?) -> Unit