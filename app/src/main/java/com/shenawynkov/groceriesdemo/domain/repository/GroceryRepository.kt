package com.shenawynkov.groceriesdemo.domain.repository

import com.shenawynkov.groceriesdemo.domain.model.GroceryItem
import kotlinx.coroutines.flow.Flow

interface GroceryRepository {
    fun getAll(): Flow<Result<List<GroceryItem>>>
    suspend fun add(item: GroceryItem): Result<Unit>
    suspend fun update(item: GroceryItem): Result<Unit>
    suspend fun delete(item: GroceryItem): Result<Unit>
}
