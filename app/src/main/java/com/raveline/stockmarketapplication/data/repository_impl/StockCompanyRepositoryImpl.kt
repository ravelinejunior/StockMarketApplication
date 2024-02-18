package com.raveline.stockmarketapplication.data.repository_impl

import com.raveline.stockmarketapplication.data.csv.CSVParser
import com.raveline.stockmarketapplication.data.local.StockCompanyDatabase
import com.raveline.stockmarketapplication.data.mapper.toCompanyStocks
import com.raveline.stockmarketapplication.data.mapper.toCompanyStocksEntity
import com.raveline.stockmarketapplication.data.remote.StockServiceApi
import com.raveline.stockmarketapplication.domain.model.CompanyStocks
import com.raveline.stockmarketapplication.domain.repository.StockCompanyRepository
import com.raveline.stockmarketapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


/**
 * Implementation of the StockCompanyRepository interface.
 *
 * @property apiService An instance of StockServiceApi for making network requests.
 * @property stockCompanyDatabase An instance of StockCompanyDatabase for local data storage.
 * @property csvParser An instance of CSVParser for parsing CSV data.
 */
class StockCompanyRepositoryImpl @Inject constructor(
    private val apiService: StockServiceApi,
    private val stockCompanyDatabase: StockCompanyDatabase,
    private val csvParser: CSVParser<CompanyStocks>
) : StockCompanyRepository {

    /**
     * Fetches a list of CompanyStocks either from remote or local storage based on the parameters.
     *
     * @param fetchFromRemote A boolean indicating whether to fetch data from remote.
     * @param searchQuery A string used to filter the data.
     * @return A Flow of Resource wrapping a list of CompanyStocks.
     */
    override suspend fun getCompanyStocks(
        fetchFromRemote: Boolean,
        searchQuery: String
    ): Flow<Resource<List<CompanyStocks>>> {
        return flow {
            // Emit loading state
            emit(Resource.Loading(isLoading = true))

            // Fetch local data
            val localCompanies = stockCompanyDatabase.companyStockDao()
                .searchCompanyStocks(searchQuery = searchQuery)

            // Emit local data
            emit(
                Resource.Success(
                    data = localCompanies.map {
                        it.toCompanyStocks()
                    }
                ),
            )

            // Determine whether to fetch from remote
            val isDBEmpty = localCompanies.isEmpty() && searchQuery.isBlank()
            val shouldJustLoadFromCache = !isDBEmpty && !fetchFromRemote

            // If not fetching from remote, emit loading state and return
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            // Fetch remote data
            val remoteCompaniesStocking = try {
                val response = apiService.getListStocks()
                csvParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message.toString()))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = e.message.toString()))
                null
            }

            // Store remote data in local database
            val dao = stockCompanyDatabase.companyStockDao()
            remoteCompaniesStocking?.let { companies ->
                dao.deleteAllCompanyStocks()
                dao.insertCompanyStocks(
                    companies.map { company ->
                        company.toCompanyStocksEntity()
                    }
                )

                // Emit updated local data
                emit(
                    Resource.Success(
                        data = dao.searchCompanyStocks(searchQuery = "")
                            .map { it.toCompanyStocks() }
                    )
                )

                // Emit loading state
                emit(Resource.Loading(isLoading = false))
            }
        }
    }
}