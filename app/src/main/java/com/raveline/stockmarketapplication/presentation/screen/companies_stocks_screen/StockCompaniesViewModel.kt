package com.raveline.stockmarketapplication.presentation.screen.companies_stocks_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raveline.stockmarketapplication.domain.model.CompaniesStockState
import com.raveline.stockmarketapplication.domain.repository.StockCompanyRepository
import com.raveline.stockmarketapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling the state and events related to company stocks.
 * This ViewModel is injected with a StockCompanyRepository for fetching data.
 * It uses the MVVM (Model-View-ViewModel) architectural pattern.
 *
 * @property repository The repository used to fetch data.
 * @property state The state of the ViewModel, represented by a CompaniesStockState object.
 * @property searchJob A nullable Job object for handling search operations.
 */
@HiltViewModel
class StockCompaniesViewModel @Inject constructor(
    private val repository: StockCompanyRepository
) : ViewModel() {
    var state by mutableStateOf(CompaniesStockState())
    private var searchJob: Job? = null

    /**
     * Handles the given event.
     * If the event is a Refresh event, it triggers a fetch operation.
     * If the event is a SearchCompaniesStockEvent, it updates the search query and triggers a search operation.
     *
     * @param event The event to handle.
     */
    fun onEvent(event: CompaniesStockEvent) {
        when (event) {
            is CompaniesStockEvent.Refresh -> {
                getStocks(searchQuery = state.searchQuery, fetchFromRemote = true)
            }

            is CompaniesStockEvent.SearchCompaniesStockEvent -> {
                state = state.copy(searchQuery = event.searchQuery)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getStocks()
                }
            }

        }
    }

    /**
     * Fetches company stocks based on the given search query and updates the state accordingly.
     * If fetchFromRemote is true, the data is fetched from a remote source.
     *
     * @param searchQuery The search query to use for fetching data. Defaults to the current search query in the state.
     * @param fetchFromRemote Whether to fetch data from a remote source. Defaults to false.
     */
    private fun getStocks(
        searchQuery: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository.getCompanyStocks(
                searchQuery = searchQuery,
                fetchFromRemote = fetchFromRemote
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { companiesStocks ->
                            state = state.copy(
                                companies = companiesStocks
                            )

                        }
                    }

                    is Resource.Error -> {
                        Unit
                    }

                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }
                }

            }
        }
    }
}












