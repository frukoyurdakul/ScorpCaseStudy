package com.scorp.casestudy.furkanyurdakul.data.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.scorp.casestudy.furkanyurdakul.data.model.Person
import com.scorp.casestudy.furkanyurdakul.util.performResume
import kotlinx.coroutines.suspendCancellableCoroutine

class PersonPagingSource(private val dataSource: DataSource) : PagingSource<String, Person>()
{
    private companion object
    {
        const val PAGE_SIZE = 20
    }

    override fun getRefreshKey(state: PagingState<String, Person>): String?
    {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            return@let anchorPage?.prevKey?.toInt()?.plus(1)?.toString()
                ?:  anchorPage?.nextKey?.toInt()?.minus(1)?.toString()
        }
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Person>
    {
        val key = params.key
        return suspendCancellableCoroutine { cont ->
            dataSource.fetch(key) { fetchResponse , fetchError ->
                fetchResponse?.let { response ->
                    val prevKey = key?.let { it.toInt() - PAGE_SIZE }?.toString()
                    val nextKey = key?.let { it.toInt() + PAGE_SIZE }?.toString() ?: PAGE_SIZE.toString()
                    val page = LoadResult.Page(response.people, prevKey, nextKey)
                    cont.performResume(page)
                } ?: cont.performResume(LoadResult.Error(LoadException(fetchError!!.errorDescription)))
            }
        }
    }
}

class LoadException: RuntimeException
{
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}