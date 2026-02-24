package com.shenawynkov.groceriesdemo.domain.repository

import com.shenawynkov.groceriesdemo.domain.model.GroceryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeGroceryRepository : GroceryRepository {

    private val items = MutableStateFlow<List<GroceryItem>>(emptyList())
    private var nextId = 1L

    override fun getAll(): Flow<Result<List<GroceryItem>>> = items.map { Result.success(it) }

    override suspend fun add(item: GroceryItem): Result<Unit> {
        val withId = item.copy(id = nextId++)
        items.update { it + withId }
        return Result.success(Unit)
    }

    override suspend fun update(item: GroceryItem): Result<Unit> {
        items.update { list ->
            list.map { if (it.id == item.id) item else it }
        }
        return Result.success(Unit)
    }

    override suspend fun delete(item: GroceryItem): Result<Unit> {
        items.update { list ->
            list.filter { it.id != item.id }
        }
        return Result.success(Unit)
    }
}
