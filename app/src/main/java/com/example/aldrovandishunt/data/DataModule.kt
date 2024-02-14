package com.example.aldrovandishunt.data

import android.content.Context
import com.example.aldrovandishunt.HuntApplication
import com.example.aldrovandishunt.data.database.HuntRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideSettingsRepository(@ApplicationContext context: Context) = SettingsRepository(context)

    @Singleton
    @Provides
    fun providePlacesRepository(@ApplicationContext context: Context) =
        HuntRepository((context.applicationContext as HuntApplication).database.DatabaseDao())

}