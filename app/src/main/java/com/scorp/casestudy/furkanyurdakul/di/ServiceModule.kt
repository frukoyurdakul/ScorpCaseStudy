package com.scorp.casestudy.furkanyurdakul.di

import com.scorp.casestudy.furkanyurdakul.data.service.DataSource
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
}