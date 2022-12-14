package com.scorp.casestudy.furkanyurdakul.data

import com.scorp.casestudy.furkanyurdakul.data.model.Person

// Assuming these would be generic in a real project, moved to outside "model" package
data class FetchResponse(val people: List<Person>, val next: String?)
data class FetchError(val errorDescription: String)
typealias FetchCompletionHandler = (FetchResponse?, FetchError?) -> Unit