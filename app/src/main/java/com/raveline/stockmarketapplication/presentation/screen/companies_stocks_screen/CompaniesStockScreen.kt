package com.raveline.stockmarketapplication.presentation.screen.companies_stocks_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.raveline.stockmarketapplication.presentation.screen.companies_stocks_screen.components.CompanyItem
import com.raveline.stockmarketapplication.presentation.screen.destinations.CompanyInfoScreenDestination

@Composable
@Destination(start = true)
fun CompaniesStockScreen(
    navigator: DestinationsNavigator,
    viewModel: CompaniesStockViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = viewModel.state.isRefreshing)
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(
                    CompaniesStockEvent.SearchCompaniesStockEvent(it)
                )
            },
            placeholder = {
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.titleMedium,
                )
            },
            singleLine = true,
            maxLines = 1,
        )

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                viewModel.onEvent(
                    CompaniesStockEvent.Refresh
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(state.companies.size) { index ->
                    val companyItem = state.companies[index]
                    CompanyItem(
                        companyItem = companyItem,
                        onClick = {
                            navigator.navigate(
                                CompanyInfoScreenDestination(symbol = companyItem.symbol)
                            )
                        }
                    )
                    if (index < state.companies.size) {
                        Divider(
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }
        }


    }
}