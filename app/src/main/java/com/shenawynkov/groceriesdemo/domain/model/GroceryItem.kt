package com.shenawynkov.groceriesdemo.domain.model

data class GroceryItem(
    val id: Long = 0L,
    val name: String,
    val category: Category,
    val isPurchased: Boolean = false
)
