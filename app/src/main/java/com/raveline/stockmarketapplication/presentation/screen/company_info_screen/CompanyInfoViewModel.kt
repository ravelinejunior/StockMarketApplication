package com.raveline.stockmarketapplication.presentation.screen.company_info_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raveline.stockmarketapplication.domain.repository.CompanyStockRepository
import com.raveline.stockmarketapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * This class is a ViewModel for the CompanyInfo screen. It uses Hilt for dependency injection.
 * It is responsible for fetching and managing the state of the company information and intra-day information.
 *
 * @property stateHandle SavedStateHandle for managing the state of the ViewModel.
 * @property repository CompanyStockRepository for fetching the company information and intra-day information.
 */
@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: CompanyStockRepository,
) : ViewModel() {
    /**
     * The state of the ViewModel. It is a mutable state of CompanyStateInfo.
     */
    var state by mutableStateOf(CompanyStateInfo())

    init {
        /**
         * Launch a coroutine in the ViewModelScope.
         */
        viewModelScope.launch {
            /**
             * Fetch the symbol from the stateHandle. If it is null, return from the launch.
             */
            val symbol = stateHandle.get<String>("symbol") ?: return@launch
            /**
             * Set the state to loading.
             */
            state = state.copy(isLoading = true)

            /**
             * Fetch the company information asynchronously.
             */
            val companyInfoResult = async {
                repository.getCompanyStockInfo(symbol = symbol)
            }

            /**
             * Fetch the intra-day information asynchronously.
             */
            val intraDayResult = async {
                repository.getIntraDayInfo(symbol = symbol)
            }

            /**
             * Await the result of the company information fetch and update the state accordingly.
             */
            when (val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        infoModel = result.data,
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }

                is Resource.Loading -> {
                    Unit
                }
            }

            /**
             * Await the result of the intra-day information fetch and update the state accordingly.
             */
            when (val result = intraDayResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        intraDaysInfo = result.data ?: emptyList(),
                        isLoading = false
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false
                    )
                }

                is Resource.Loading -> {
                    Unit
                }
            }
        }
    }
}