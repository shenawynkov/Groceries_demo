package com.shenawynkov.groceriesdemo.ui.grocerylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.domain.model.GroceryItem
import com.shenawynkov.groceriesdemo.domain.repository.GroceryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryListViewModel @Inject constructor(
    private val repository: GroceryRepository
) : ViewModel() {

    private val _genericErrorEvents = Channel<Int>(Channel.BUFFERED)
    val genericErrorEvents: Flow<Int> = _genericErrorEvents.receiveAsFlow()

    private val localState = MutableStateFlow(GroceryListState())

    private val getAllFlow = repository.getAll()
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    init {
        viewModelScope.launch {
            getAllFlow.collect { result ->
                if (result.isFailure) _genericErrorEvents.send(R.string.error_generic)
            }
        }
    }

    val state: StateFlow<GroceryListState> = combine(getAllFlow, localState) { result, local ->
        mergeRepoResult(result, local)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GroceryListState())

    fun onIntent(intent: GroceryListIntent) {
        when (intent) {
            is GroceryListIntent.UpdateName ->
                localState.update {
                    it.copy(
                        form = it.form.copy(
                            inputName = intent.name,
                            validationErrorResId = null
                        )
                    )
                }

            is GroceryListIntent.UpdateCategory ->
                localState.update { it.copy(form = it.form.copy(inputCategory = intent.category)) }

            is GroceryListIntent.StartEdit ->
                localState.update { it.copy(edit = it.edit.copy(editingItem = intent.item)) }

            is GroceryListIntent.CancelEdit ->
                localState.update { it.copy(edit = EditState()) }

            is GroceryListIntent.SetFilter ->
                localState.update { it.copy(list = it.list.copy(activeFilter = intent.category)) }

            is GroceryListIntent.SetSort ->
                localState.update { it.copy(list = it.list.copy(sortOption = intent.option)) }

            is GroceryListIntent.DismissError ->
                localState.update {
                    it.copy(
                        form = it.form.copy(validationErrorResId = null),
                        edit = it.edit.copy(validationErrorResId = null)
                    )
                }

            is GroceryListIntent.AddItem -> handleAddItem()
            is GroceryListIntent.TogglePurchased -> viewModelScope.launch {
                val success = repository.update(
                    intent.item.copy(isPurchased = !intent.item.isPurchased)
                ).isSuccess
                if (!success) _genericErrorEvents.send(R.string.error_generic)
            }

            is GroceryListIntent.DeleteItem -> viewModelScope.launch {
                val success = repository.delete(intent.item).isSuccess
                if (!success) _genericErrorEvents.send(R.string.error_generic)
            }

            is GroceryListIntent.ConfirmEdit -> handleConfirmEdit(intent.name, intent.category)
        }
    }

    private fun mergeRepoResult(
        result: Result<List<GroceryItem>>,
        local: GroceryListState
    ): GroceryListState = result.fold(
        onSuccess = { items ->
            val filtered = if (local.list.activeFilter != null) {
                items.filter { it.category == local.list.activeFilter }
            } else {
                items
            }
            val sorted = sortItems(filtered, local.list.sortOption)
            local.copy(
                list = local.list.copy(
                    items = sorted,
                    totalItemCount = items.size
                )
            )
        },
        onFailure = {
            local.copy(
                list = local.list.copy(
                    items = emptyList(),
                    totalItemCount = 0
                )
            )
        }
    )

    private fun sortItems(items: List<GroceryItem>, option: SortOption): List<GroceryItem> =
        when (option) {
            SortOption.NONE -> items
            SortOption.ALPHABETICAL -> items.sortedBy { it.name.lowercase() }
            SortOption.BY_CATEGORY -> items.sortedBy { it.category.ordinal }
            SortOption.BY_STATUS -> items.sortedBy { it.isPurchased }
        }

    private fun handleAddItem() {
        val form = localState.value.form
        val trimmed = form.inputName.trim()
        if (trimmed.isBlank()) {
            localState.update {
                it.copy(form = it.form.copy(validationErrorResId = R.string.error_item_name_empty))
            }
            return
        }
        viewModelScope.launch {
            val item = GroceryItem(name = trimmed, category = form.inputCategory)
            val success = repository.add(item).isSuccess
            if (success) {
                localState.update {
                    it.copy(form = it.form.copy(inputName = "", validationErrorResId = null))
                }
            } else {
                _genericErrorEvents.send(R.string.error_generic)
            }
        }
    }

    private fun handleConfirmEdit(name: String, category: Category) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            localState.update {
                it.copy(edit = it.edit.copy(validationErrorResId = R.string.error_item_name_empty))
            }
            return
        }
        val editingItem = localState.value.edit.editingItem ?: return
        viewModelScope.launch {
            val updated = editingItem.copy(name = trimmed, category = category)
            val success = repository.update(updated).isSuccess
            if (success) {
                localState.update { it.copy(edit = EditState()) }
            } else {
                _genericErrorEvents.send(R.string.error_generic)
            }
        }
    }
}
