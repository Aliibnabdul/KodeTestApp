package com.example.koder.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koder.data.KoderRepository
import com.example.koder.domain.ApiResult
import com.example.koder.domain.ApplicationError
import com.example.koder.domain.EmployeeDomainModel
import com.example.koder.domain.UiModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

const val ALL_KEY = "Все"
const val ALPHABET_SORT_KEY = 0
const val BIRTHDAY_SORT_KEY = 1

class MainFragmentVM(private val koderRepository: KoderRepository) : ViewModel() {

    private var departmentsMap: MutableMap<String, List<EmployeeDomainModel>> = mutableMapOf()

    private val _keys: MutableSharedFlow<List<String>> = MutableSharedFlow(replay = 1)
    val keys = _keys.asSharedFlow()

    private val _filteredUiModel: MutableStateFlow<List<UiModel>> = MutableStateFlow(listOf())
    val filteredUiModel = _filteredUiModel.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _errorSnackBar = MutableSharedFlow<ApplicationError>()
    val errorSnackBar = _errorSnackBar.asSharedFlow()

    private val _error = MutableSharedFlow<ApplicationError>()
    val fatalError = _error.asSharedFlow()

    private val _sortValue = MutableStateFlow(ALPHABET_SORT_KEY)
    val sortValue = _sortValue.asStateFlow()

    private val _isSearchQueryNotEmpty = MutableStateFlow(false)
    val isSearchQueryNotEmpty = _isSearchQueryNotEmpty.asStateFlow()

    private val selectedTab = MutableStateFlow("")

    private var searchQuery = MutableStateFlow("")

    init {
        fetchEmployees()
        viewModelScope.launch {
            combine(searchQuery, sortValue, selectedTab) { searchQuery, sortValue, selectedTab ->
                getFilteredAndSorted(searchQuery, sortValue, selectedTab)
            }.collectLatest { list ->

                _filteredUiModel.value = if (sortValue.value == BIRTHDAY_SORT_KEY) {
                    mapToUiModelWithSeparator(list)
                } else {
                    list.map { UiModel.EmployeeItem(it.copy(birthday = "")) }
                }
            }
        }

        viewModelScope.launch {
            searchQuery.collect {
                _isSearchQueryNotEmpty.value = it.isNotEmpty()
            }
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
                is ApiResult.Error -> {
                    _loading.value = false
                    if (departmentsMap.isEmpty()) _error.emit(apiResult.error)
                    _errorSnackBar.emit(apiResult.error)
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
    ): List<EmployeeDomainModel> {
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
                    sortedList = sortedList.sortedBy { it.nextBirthdayLocalDate }
                }
            }
        }
        return sortedList
    }

    private fun mapToUiModelWithSeparator(list: List<EmployeeDomainModel>): List<UiModel> {
        val thisYearList = list.filter { it.nextBirthdayLocalDate.year == LocalDate.now().year }
        val nextYearList = list.filter { it.nextBirthdayLocalDate.year != LocalDate.now().year }
        return thisYearList.map { UiModel.EmployeeItem(it) } +
                UiModel.SeparatorItem((LocalDate.now().year + 1).toString()) +
                nextYearList.map { UiModel.EmployeeItem(it) }
    }
}