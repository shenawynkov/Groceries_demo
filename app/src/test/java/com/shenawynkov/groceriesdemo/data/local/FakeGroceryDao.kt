package com.shenawynkov.groceriesdemo.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeGroceryDao : GroceryDao {

    private val items = MutableStateFlow<List<GroceryItemEntity>>(emptyList())
    private var nextId = 1L

    override fun getAll(): Flow<List<GroceryItemEntity>> = items

    override suspend fun insert(item: GroceryItemEntity): Long {
        val id = nextId++
        val withId = item.copy(id = id)
        items.update { it + withId }
        return id
    }

    override suspend fun update(item: GroceryItemEntity) {
        items.update { list ->
            list.map { if (it.id == item.id) item else it }
        }
    }

    override suspend fun delete(item: GroceryItemEntity) {
        items.update { list ->
            list.filter { it.id != item.id }
        }
    }
}
