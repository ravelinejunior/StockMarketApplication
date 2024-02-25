package com.raveline.stockmarketapplication.data.di


import com.raveline.stockmarketapplication.data.csv.CSVParser
import com.raveline.stockmarketapplication.data.csv.CSVParserCompanyStockImpl
import com.raveline.stockmarketapplication.data.csv.CSVParserIntraDayInfoImpl
import com.raveline.stockmarketapplication.data.repository_impl.CompanyStockRepositoryImpl
import com.raveline.stockmarketapplication.domain.model.CompanyStocks
import com.raveline.stockmarketapplication.domain.model.IntraDayInfoModel
import com.raveline.stockmarketapplication.domain.repository.CompanyStockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This is a Dagger Hilt module used for providing dependencies related to repositories.
 * It is installed in the SingletonComponent, meaning the provided dependencies will be available as long as the application is running.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * This function tells Dagger Hilt how to provide an instance of CompanyStockRepository.
     * It takes an instance of CompanyStockRepositoryImpl as a parameter and returns an instance of CompanyStockRepository.
     * This means that whenever an instance of CompanyStockRepository is requested, Dagger Hilt will provide an instance of CompanyStockRepositoryImpl.
     */
    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: CompanyStockRepositoryImpl
    ): CompanyStockRepository

    /**
     * This function tells Dagger Hilt how to provide an instance of CSVParser<CompanyStocks>.
     * It takes an instance of CSVParserImpl as a parameter and returns an instance of CSVParser<CompanyStocks>.
     * This means that whenever an instance of CSVParser<CompanyStocks> is requested, Dagger Hilt will provide an instance of CSVParserImpl.
     */
    @Binds
    @Singleton
    abstract fun bindCSVParser(
        csvParserImpl: CSVParserCompanyStockImpl
    ): CSVParser<CompanyStocks>

    /**
     * This function tells Dagger Hilt how to provide an instance of CSVParser<IntraDayInfoModel>.
     * It takes an instance of CSVParserImpl as a parameter and returns an instance of CSVParser<IntraDayInfoModel>.
     * This means that whenever an instance of CSVParser<IntraDayInfoModel> is requested, Dagger Hilt will provide an instance of CSVParserImpl.
     */
    @Binds
    @Singleton
    abstract fun bindCSVIntraDayParser(
        csvParserIntraDayInfoImpl: CSVParserIntraDayInfoImpl
    ): CSVParser<IntraDayInfoModel>

}