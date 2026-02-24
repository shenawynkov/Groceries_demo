package com.shenawynkov.groceriesdemo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {

    @Query("SELECT * FROM grocery_items")
    fun getAll(): Flow<List<GroceryItemEntity>>

    @Insert
    suspend fun insert(item: GroceryItemEntity): Long

    @Update
    suspend fun update(item: GroceryItemEntity)

    @Delete
    suspend fun delete(item: GroceryItemEntity)
}
