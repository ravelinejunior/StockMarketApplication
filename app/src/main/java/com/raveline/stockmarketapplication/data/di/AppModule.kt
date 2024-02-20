package com.raveline.stockmarketapplication.data.di

import android.app.Application
import androidx.room.Room
import com.raveline.stockmarketapplication.data.local.StockCompanyDatabase
import com.raveline.stockmarketapplication.data.local.dao.CompanyStockDao
import com.raveline.stockmarketapplication.data.remote.StockServiceApi
import com.raveline.stockmarketapplication.util.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): StockServiceApi =
        Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
            .client(okHttpClient)
            .build()
            .create(StockServiceApi::class.java)


    @Provides
    @Singleton
    fun provideStockCompanyDatabase(application: Application): StockCompanyDatabase =
        Room.databaseBuilder(
            context = application,
            klass = StockCompanyDatabase::class.java,
            name = Constants.STOCK_DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    @Singleton
    fun provideCompanyStockDao(stockCompanyDatabase: StockCompanyDatabase): CompanyStockDao =
        stockCompanyDatabase.companyStockDao()
}