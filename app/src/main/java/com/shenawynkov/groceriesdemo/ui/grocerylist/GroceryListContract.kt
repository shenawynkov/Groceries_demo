package com.shenawynkov.groceriesdemo.ui.grocerylist

import androidx.compose.runtime.Immutable
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.domain.model.GroceryItem

@Immutable
data class FormState(
    val inputName: String = "",
    val inputCategory: Category = Category.MILK,
    val validationErrorResId: Int? = null
)

@Immutable
data class ListState(
    val items: List<GroceryItem> = emptyList(),
    val totalItemCount: Int = 0,
    val activeFilter: Category? = null,
    val sortOption: SortOption = SortOption.NONE
)

@Immutable
data class EditState(
    val editingItem: GroceryItem? = null,
    val validationErrorResId: Int? = null
)

@Immutable
data class GroceryListState(
    val form: FormState = FormState(),
    val list: ListState = ListState(),
    val edit: EditState = EditState()
)

sealed interface GroceryListIntent {
    data class UpdateName(val name: String) : GroceryListIntent
    data class UpdateCategory(val category: Category) : GroceryListIntent
    data object AddItem : GroceryListIntent
    data class TogglePurchased(val item: GroceryItem) : GroceryListIntent
    data class DeleteItem(val item: GroceryItem) : GroceryListIntent
    data class StartEdit(val item: GroceryItem) : GroceryListIntent
    data class ConfirmEdit(val name: String, val category: Category) : GroceryListIntent
    data object CancelEdit : GroceryListIntent
    data class SetFilter(val category: Category?) : GroceryListIntent
    data class SetSort(val option: SortOption) : GroceryListIntent
    data object DismissError : GroceryListIntent
}

enum class SortOption {
    NONE,
    ALPHABETICAL,
    BY_CATEGORY,
    BY_STATUS
}
