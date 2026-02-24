package com.shenawynkov.groceriesdemo.ui.grocerylist

import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.domain.model.Category

fun categoryEmoji(category: Category): String = when (category) {
    Category.MILK -> "\uD83E\uDD5B"
    Category.VEGETABLES -> "\uD83E\uDD55"
    Category.FRUITS -> "\uD83C\uDF4E"
    Category.BREADS -> "\uD83C\uDF5E"
    Category.MEATS -> "\uD83E\uDD69"
}

fun categoryLabelResId(category: Category): Int = when (category) {
    Category.MILK -> R.string.category_milk
    Category.VEGETABLES -> R.string.category_vegetables
    Category.FRUITS -> R.string.category_fruits
    Category.BREADS -> R.string.category_breads
    Category.MEATS -> R.string.category_meats
}

fun sortOptionLabelResId(option: SortOption): Int = when (option) {
    SortOption.NONE -> R.string.sort_default
    SortOption.ALPHABETICAL -> R.string.sort_az
    SortOption.BY_CATEGORY -> R.string.sort_category
    SortOption.BY_STATUS -> R.string.sort_status
}
