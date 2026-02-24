package com.shenawynkov.groceriesdemo.data.mappers

import com.shenawynkov.groceriesdemo.data.local.GroceryItemEntity
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.domain.model.GroceryItem

fun GroceryItemEntity.toDomain(): GroceryItem = GroceryItem(
    id = id,
    name = name,
    category = Category.entries.find { it.name == category } ?: Category.MILK,
    isPurchased = isPurchased
)

fun GroceryItem.toEntity(): GroceryItemEntity = GroceryItemEntity(
    id = id,
    name = name,
    category = category.name,
    isPurchased = isPurchased
)
