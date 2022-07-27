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
import com.scorp.casestudy.furkanyurdakul.util.DataLoadState
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

    private val loadedList: MutableList<Person> = ArrayList(50)
    private val displayList: MutableList<BaseDisplayItem> = ArrayList(50)

    private var lastLoadJob: Job? = null

    private var lastKey: String? = null

    fun loadPeople()
    {
        viewModelScope.launch {
            lastLoadJob?.join()
            lastLoadJob = TaskDispatchers.IOScope.launch {

                var isFirst = true
                if (loadedList.isEmpty())
                    displayList.add(FirstLoadStateDisplayItem(DataLoadState(isLoading = true)))
                else
                {
                    displayList.add(ItemLoadStateDisplayItem(DataLoadState(isLoading = true)))
                    isFirst = false
                }

                suspendCancellableCoroutine<Unit?> { cont ->
                    dataSource.fetch(lastKey) { fetchResponse, fetchError ->
                        displayList.removeLast()
                        fetchResponse?.let { response ->
                            displayList.addAll(response.people.map { PersonDisplayItem(it) })
                        } ?: run {
                            if (isFirst)
                            {
                                displayList.add(FirstLoadStateDisplayItem(DataLoadState(
                                    isError = true,
                                    errorMessage = fetchError!!.errorDescription
                                )))
                            }
                            else
                            {
                                displayList.add(ItemLoadStateDisplayItem(DataLoadState(
                                    isError = true,
                                    errorMessage = fetchError!!.errorDescription
                                )))
                            }
                            cont.performResume(null)
                        }
                    }
                }

                withContext(Dispatchers.Main)
                {
                    _loadedLiveData.value = displayList
                }
            }
        }
    }

    fun refreshPeople()
    {
        viewModelScope.launch {
            lastLoadJob?.join()
            displayList.clear()
            loadPeople()
        }
    }
}