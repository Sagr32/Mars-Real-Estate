/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marsrealestate.network.MarsApi
import com.example.marsrealestate.network.MarsApiFilter
import com.example.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */

enum class MarsApiStatus {
    LOADING, SUCCESS, FAILED
}

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()

    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    private val _navigateToDetails = MutableLiveData<MarsProperty>()

    val navigatoToDetails: LiveData<MarsProperty>
        get() = _navigateToDetails


    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filterQuery: MarsApiFilter) {
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING

            try {
                val response = MarsApi.retrofitService.getProperties(filterQuery.value)
                if (response.isSuccessful && response.body() != null) {
                    _status.value = MarsApiStatus.SUCCESS
                    _properties.value = response.body()
                } else {
                    _status.value = MarsApiStatus.FAILED
                }
            } catch (error: Exception) {
                _status.value = MarsApiStatus.FAILED
            }


        }

    }

    fun onNavigateComplete() {
        _navigateToDetails.value = null
    }

    fun setPropertyDetails(marsProperty: MarsProperty) {
        _navigateToDetails.value = marsProperty
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
