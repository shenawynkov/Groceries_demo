package com.shenawynkov.groceriesdemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GroceryItemEntity::class], version = 1)
abstract class GroceryDatabase : RoomDatabase() {
    abstract fun groceryDao(): GroceryDao
}
