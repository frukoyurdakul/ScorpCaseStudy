package com.scorp.casestudy.furkanyurdakul.util

data class DataState(
    val isLoading: Boolean = false,
    val isEnded: Boolean = false,
    val isError: Boolean = false,
    val showMessage: Boolean = false,
    val message: String = ""
)

sealed class LoadState(val isLoading: Boolean, val isEnded: Boolean = false)
{
    object Loading: LoadState(isLoading = true)
    object NotLoading: LoadState(isLoading = false)
    object EndOfList: LoadState(isLoading = false, isEnded = true)
}