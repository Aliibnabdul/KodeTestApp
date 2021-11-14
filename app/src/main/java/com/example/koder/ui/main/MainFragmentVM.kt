package com.example.koder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koder.data.KoderRepository
import com.example.koder.domain.ApiResult
import com.example.koder.domain.ApplicationError
import com.example.koder.domain.EmployeeUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

const val ALL_KEY = "Все"
const val ALPHABET_SORT_KEY = 0
const val BIRTHDAY_SORT_KEY = 1

class MainFragmentVM(private val koderRepository: KoderRepository) : ViewModel() {

    private var departmentsMap: MutableMap<String, List<EmployeeUiModel>> = mutableMapOf()

    private val _keys: MutableSharedFlow<List<String>> = MutableSharedFlow()
    val keys = _keys.asSharedFlow()

    private val _filteredList: MutableStateFlow<List<EmployeeUiModel>> = MutableStateFlow(listOf())
    val filteredList = _filteredList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableSharedFlow<ApplicationError>()
    val error = _error.asSharedFlow()

    private val _sortValue = MutableStateFlow(ALPHABET_SORT_KEY)
    val sortValue = _sortValue.asStateFlow()

    private val selectedTab = MutableStateFlow("")

    private var searchQuery = MutableStateFlow("")

    init {
        fetchEmployees()
        viewModelScope.launch {
            combine(searchQuery, sortValue, selectedTab) { searchQuery, sortValue, selectedTab ->
                getFilteredAndSorted(searchQuery, sortValue, selectedTab)
            }.collectLatest { _filteredList.value = it }
        }
    }

    fun fetchEmployees() {
        viewModelScope.launch {
            _loading.value = true
            when (val apiResult = koderRepository.getEmployees()) {
                is ApiResult.Success -> {
                    departmentsMap = apiResult.result.groupBy { it.department }.toMutableMap()
                    val keys = listOf(ALL_KEY) + departmentsMap.keys
                    departmentsMap[ALL_KEY] = apiResult.result
                    _keys.emit(keys)
                    _loading.value = false
                }
                is ApiResult.Error   -> {
                    _error.emit(apiResult.error)
                }
            }
        }
    }

    fun setSelectedTab(key: String?) {
        key?.let { selectedTab.value = it }
    }

    fun setSortValue(checkedPosition: Int) {
        _sortValue.value = checkedPosition
    }

    fun onNewQuery(query: String) {
        searchQuery.value = query
    }

    private fun getFilteredAndSorted(
        query: String,
        sortValue: Int,
        selectedTab: String
    ): List<EmployeeUiModel> {
        var sortedList = departmentsMap[selectedTab] ?: listOf()
        if (sortedList.isNotEmpty()) {
            if (query.length > 2) {
                sortedList = sortedList.filter {
                    it.firstName.lowercase().contains(query.lowercase())
                            || it.lastName.lowercase().contains(query.lowercase())
                }
            }
            when (sortValue) {
                ALPHABET_SORT_KEY -> {
                    sortedList = sortedList.sortedWith(
                        compareBy({ it.firstName }, { it.lastName })
                    )
                }
                BIRTHDAY_SORT_KEY -> {
                    sortedList = sortedList.sortedBy { it.birthdayLocalDate }
                }
            }
        }
        return sortedList
    }
}