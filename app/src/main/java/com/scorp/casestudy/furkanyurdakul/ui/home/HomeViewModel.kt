package com.scorp.casestudy.furkanyurdakul.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.scorp.casestudy.furkanyurdakul.data.model.Person
import com.scorp.casestudy.furkanyurdakul.data.service.DataSource
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseDisplayItem
import com.scorp.casestudy.furkanyurdakul.ui.base.BaseViewModel
import com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems.FirstLoadStateDisplayItem
import com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems.ItemLoadStateDisplayItem
import com.scorp.casestudy.furkanyurdakul.ui.home.adapteritems.PersonDisplayItem
import com.scorp.casestudy.furkanyurdakul.util.DataState
import com.scorp.casestudy.furkanyurdakul.util.LoadState
import com.scorp.casestudy.furkanyurdakul.util.TaskDispatchers
import com.scorp.casestudy.furkanyurdakul.util.performResume
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataSource: DataSource
): BaseViewModel()
{
    private val _loadedLiveData: MutableLiveData<List<BaseDisplayItem>> = MutableLiveData()
    val liveData: LiveData<List<BaseDisplayItem>> = _loadedLiveData

    private val _loadState: MutableLiveData<LoadState> = MutableLiveData()
    val loadState: LiveData<LoadState> = _loadState

    private val _backendErrorState: MutableLiveData<String> = MutableLiveData()
    val backendErrorState: LiveData<String> = _backendErrorState

    private val loadedList: MutableSet<Person> = LinkedHashSet(50)
    private val displayList: MutableList<BaseDisplayItem> = ArrayList(50)

    private var lastLoadJob: Job? = null
    private var lastKey: String? = null

    private var endOfListReached = false
    private var blockNextRequest = false

    fun loadPeople(force: Boolean = false, retry: Boolean = false)
    {
        if (!force && !retry && blockNextRequest)
            return

        if (endOfListReached)
        {
            if (displayList.lastOrNull() !is PersonDisplayItem)
            {
                displayList.removeLast()
                viewModelScope.launch {
                    dispatchList()
                }
                return
            }
        }

        endOfListReached = false
        blockNextRequest = false
        viewModelScope.launch {
            lastLoadJob?.let {
                if (it.isActive)
                    return@launch
            }

            dispatchLoadState(LoadState.Loading)
            lastLoadJob = TaskDispatchers.IOScope.launch {
                if (displayList.isNotEmpty() && displayList.lastOrNull() !is PersonDisplayItem)
                    displayList.removeLast()

                if (loadedList.isEmpty())
                    displayList.add(FirstLoadStateDisplayItem(DataState(isLoading = true)))
                else
                    displayList.add(ItemLoadStateDisplayItem(DataState(isLoading = true)))

                dispatchList()
                suspendCancellableCoroutine<Unit?> { cont ->
                    dataSource.fetch(lastKey) { fetchResponse, fetchError ->
                        fetchResponse?.let { response ->
                            val previousSize = loadedList.size
                            val changed = loadedList.addAll(response.people)
                            val sizeDiff = loadedList.size - previousSize
                            val existingSize = response.people.size - sizeDiff

                            if (existingSize != 0)
                            {
                                // There was a backend bug. Show a toast with
                                // a UI callback.
                                val messageSuffix = if (existingSize == 1) "person" else "people"
                                _backendErrorState.postValue("There was " +
                                        "$existingSize $messageSuffix " +
                                        "that was already added in the list.")
                            }

                            if (changed)
                            {
                                displayList.clear()
                                displayList.addAll(loadedList.map { PersonDisplayItem(it) })

                                // Determine whether we have more people or not.
                                response.next?.let {
                                    // Assign the last key.
                                    lastKey = it

                                    // Add a last item so that when scrolled down, it won't bug
                                    // out the recycler view.
                                    displayList.add(ItemLoadStateDisplayItem(
                                        DataState(isLoading = true))
                                    )
                                } ?: run {

                                    // End of the list was reached. Edit the message
                                    // so that the user will know the end of list
                                    // was reached.
                                    endOfListReached = true
                                    displayList.removeLast()
                                    handleMessage(
                                        message = "All people have been listed.",
                                        isEndOfList = true
                                    )
                                }
                            }
                            else
                            {
                                // Remove the loading item.
                                displayList.removeLast()

                                // This could be handled with localization,
                                // but hard-coded since other errors are
                                // also hard-coded on the data source.
                                if (loadedList.isEmpty())
                                {
                                    handleMessage(message = "No people to display.",
                                        isError = true)
                                }
                                else if (response.people.isEmpty())
                                {
                                    handleMessage(message = "An empty people list was returned.",
                                        isError = true)
                                }
                                else
                                {
                                    handleMessage(message = "No other people to display.",
                                        isError = true)
                                }
                            }
                        } ?: run {
                            // Remove the loading item.
                            displayList.removeLast()
                            handleMessage(
                                message = fetchError!!.errorDescription,
                                isError = true
                            )
                        }

                        // Resume the suspending function
                        cont.performResume(null)
                    }
                }

                // Update the loading state and the button
                if (endOfListReached)
                    dispatchLoadState(LoadState.EndOfList)
                else
                    dispatchLoadState(LoadState.NotLoading)

                withContext(Dispatchers.Main)
                {
                    _loadedLiveData.value = ArrayList(displayList)
                }
            }
        }
    }

    fun refreshPeople()
    {
        viewModelScope.launch {
            lastLoadJob?.join()
            loadedList.clear()
            displayList.clear()
            lastKey = null
            endOfListReached = false
            loadPeople(force = true, retry = false)
        }
    }

    private fun handleMessage(message: String, isEndOfList: Boolean = false, isError: Boolean = false)
    {
        blockNextRequest = true
        if (displayList.isEmpty())
        {
            displayList.add(FirstLoadStateDisplayItem(DataState(
                showMessage = true,
                isError = isError,
                isEnded = isEndOfList,
                message = message
            )))
        }
        else
        {
            displayList.add(ItemLoadStateDisplayItem(DataState(
                showMessage = true,
                isError = isError,
                isEnded = isEndOfList,
                message = message
            )))
        }
    }

    private suspend fun dispatchList()
    {
        withContext(Dispatchers.Main)
        {
            // Create a new instance list that contains
            // the original elements for the DiffUtil to not
            // be confused in the future while comparing
            // elements.
            _loadedLiveData.value = ArrayList(displayList)
        }
    }

    private suspend fun dispatchLoadState(state: LoadState)
    {
        withContext(Dispatchers.Main)
        {
            _loadState.value = state
        }
    }
}