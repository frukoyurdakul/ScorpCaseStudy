package com.scorp.casestudy.furkanyurdakul.util

import androidx.paging.LoadState

data class DataLoadState(
    val isLoading: Boolean,
    val isEnded: Boolean,
    val isError: Boolean,
    val errorMessage: String
)

fun LoadState.toDataLoadState(): DataLoadState
{
    return when (this)
    {
        LoadState.Loading -> DataLoadState(
            isLoading = true,
            isEnded = false,
            isError = false,
            errorMessage = ""
        )
        is LoadState.NotLoading -> DataLoadState(
            isLoading = false,
            isEnded = endOfPaginationReached,
            isError = false,
            errorMessage = ""
        )
        is LoadState.Error -> DataLoadState(
            isLoading = false,
            isEnded = true,
            isError = true,
            errorMessage = error.message ?: "Unknown error message"
        )
    }
}