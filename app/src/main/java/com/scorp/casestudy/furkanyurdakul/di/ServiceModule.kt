package com.scorp.casestudy.furkanyurdakul.di

import com.scorp.casestudy.furkanyurdakul.data.service.DataSource
import com.scorp.casestudy.furkanyurdakul.data.service.PersonPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ServiceModule
{
    @Provides
    @Singleton
    fun provideDataSource() = DataSource()

    @Provides
    @Singleton
    fun providePersonPagingSource(dataSource: DataSource) = PersonPagingSource(dataSource)
}