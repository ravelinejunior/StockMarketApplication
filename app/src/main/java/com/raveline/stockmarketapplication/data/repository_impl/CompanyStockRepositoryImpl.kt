package com.raveline.stockmarketapplication.data.repository_impl

import com.raveline.stockmarketapplication.data.csv.CSVParser
import com.raveline.stockmarketapplication.data.local.StockCompanyDatabase
import com.raveline.stockmarketapplication.data.mapper.toCompanyInfo
import com.raveline.stockmarketapplication.data.mapper.toCompanyStocks
import com.raveline.stockmarketapplication.data.mapper.toCompanyStocksEntity
import com.raveline.stockmarketapplication.data.remote.StockServiceApi
import com.raveline.stockmarketapplication.domain.model.CompanyStockInfoModel
import com.raveline.stockmarketapplication.domain.model.CompanyStocks
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel
import com.raveline.stockmarketapplication.domain.repository.CompanyStockRepository
import com.raveline.stockmarketapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyStockRepositoryImpl @Inject constructor(
    private val apiService: StockServiceApi,
    private val stockCompanyDatabase: StockCompanyDatabase,
    private val csvParserCompanyStock: CSVParser<CompanyStocks>,
    private val csvParserIntraDayInfo: CSVParser<IntraDayInfoModel>,
) : CompanyStockRepository {

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
                csvParserCompanyStock.parse(response.byteStream())
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

    /**
     * Fetches the intra-day information of a specific stock symbol.
     *
     * This function makes a network request to fetch the intra-day information of a specific stock symbol.
     * The data is then parsed from CSV format into a list of IntraDayInfoModel objects.
     * If the network request or the parsing fails, an error resource is returned.
     *
     * @param symbol The stock symbol to fetch the intra-day information for.
     * @return A Resource wrapping a list of IntraDayInfoModel objects.
     * @throws IOException If there is a network error.
     * @throws HttpException If there is an HTTP error.
     */
    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfoModel>> {
        return try {
            val response = apiService.getIntraDayInfo(symbol = symbol)
            val csvParsedData = csvParserIntraDayInfo.parse(stream = response.byteStream())
            Resource.Success(data = csvParsedData)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.message.toString())
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.message.toString())
        }
    }

    /**
     * Fetches the stock information of a specific company.
     *
     * This function makes a network request to fetch the stock information of a specific company.
     * The data is then parsed into a CompanyStockInfoModel object.
     * If the network request or the parsing fails, an error resource is returned.
     *
     * @param symbol The stock symbol to fetch the stock information for.
     * @return A Resource wrapping a CompanyStockInfoModel object.
     * @throws IOException If there is a network error.
     * @throws HttpException If there is an HTTP error.
     */
    override suspend fun getCompanyStockInfo(
        symbol: String
    ): Resource<CompanyStockInfoModel> {
        return try {
            val response = apiService.getCompanyStockInfo(symbol = symbol)
            Resource.Success(data = response.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.message.toString())
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.message.toString())
        }
    }
}