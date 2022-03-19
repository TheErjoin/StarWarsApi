package com.example.starwarsapi.domain.common.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwarsapi.data.common.resource.Resource
import com.example.starwarsapi.presentation.state.UIState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun <T> stateData(
        state: MutableLiveData<UIState<T>>,
        request: () -> Flow<Resource<T>>
    ) {
        viewModelScope.launch {
            request().collect {
                when (it) {
                    is Resource.Loading -> {
                        state.value = UIState.Loading()
                    }
                    is Resource.Error -> it.message?.let { error ->
                        state.value = UIState.Error(error)
                    }
                    is Resource.Success -> it.data?.let { data ->
                        state.value = UIState.Success(data)
                    }
                }
            }
        }
    }
}