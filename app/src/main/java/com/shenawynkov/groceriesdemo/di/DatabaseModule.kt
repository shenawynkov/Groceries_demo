package com.shenawynkov.groceriesdemo.di

import android.content.Context
import androidx.room.Room
import com.shenawynkov.groceriesdemo.data.local.GroceryDao
import com.shenawynkov.groceriesdemo.data.local.GroceryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GroceryDatabase {
        return Room.databaseBuilder(
            context,
            GroceryDatabase::class.java,
            "grocery_db"
        ).build()
    }

    @Provides
    fun provideDao(db: GroceryDatabase): GroceryDao = db.groceryDao()
}
