package com.shenawynkov.groceriesdemo.ui.grocerylist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.ui.components.AddItemSection
import com.shenawynkov.groceriesdemo.ui.components.CategoryFilterChips
import com.shenawynkov.groceriesdemo.ui.components.EditItemDialog
import com.shenawynkov.groceriesdemo.ui.components.GroceryItemRow
import com.shenawynkov.groceriesdemo.ui.theme.AppPrimary

@Composable
fun GroceryListScreen(
    modifier: Modifier = Modifier,
    viewModel: GroceryListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val formState = state.form
    val listState = state.list
    val editState = state.edit
    var showSortMenu by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(formState.addSectionExpanded) {
        if (!formState.addSectionExpanded) focusManager.clearFocus()
    }

    editState.editingItem?.let { item ->
        EditItemDialog(
            item = item,
            validationErrorResId = editState.validationErrorResId,
            onConfirm = { name, category ->
                viewModel.onIntent(GroceryListIntent.ConfirmEdit(name, category))
            },
            onDismiss = { viewModel.onIntent(GroceryListIntent.CancelEdit) }
        )
    }


    val snackbarDismissLabel = stringResource(R.string.snackbar_dismiss)
    LaunchedEffect(viewModel) {
        viewModel.genericErrorEvents.collect { resId ->
            snackbarHostState.showSnackbar(
                message = context.getString(resId),
                actionLabel = snackbarDismissLabel
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp, bottom = 8.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(AppPrimary)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.screen_title_grocery_list),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.screen_subtitle_grocery_list),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                item {
                    AddItemSection(
                        expanded = formState.addSectionExpanded,
                        onExpandToggle = { viewModel.onIntent(GroceryListIntent.ToggleAddSection) },
                        name = formState.inputName,
                        category = formState.inputCategory,
                        validationErrorResId = formState.validationErrorResId,
                        onNameChange = { viewModel.onIntent(GroceryListIntent.UpdateName(it)) },
                        onCategoryChange = { viewModel.onIntent(GroceryListIntent.UpdateCategory(it)) },
                        onAdd = { viewModel.onIntent(GroceryListIntent.AddItem) }
                    )
                }

                if (listState.totalItemCount > 0) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CategoryFilterChips(
                                activeFilter = listState.activeFilter,
                                onFilterSelected = {
                                    viewModel.onIntent(
                                        GroceryListIntent.SetFilter(
                                            it
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                            Box {
                                IconButton(onClick = { showSortMenu = true }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = stringResource(R.string.content_desc_sort)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showSortMenu,
                                    onDismissRequest = { showSortMenu = false }
                                ) {
                                    SortOption.entries.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    stringResource(sortOptionLabelResId(option)),
                                                    color = if (listState.sortOption == option)
                                                        MaterialTheme.colorScheme.primary
                                                    else
                                                        MaterialTheme.colorScheme.onSurface
                                                )
                                            },
                                            onClick = {
                                                viewModel.onIntent(GroceryListIntent.SetSort(option))
                                                showSortMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (listState.items.isEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = if (listState.totalItemCount > 0) {
                                    stringResource(R.string.empty_filtered_title)
                                } else {
                                    stringResource(R.string.empty_list_title)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (listState.totalItemCount > 0) {
                                    stringResource(R.string.empty_filtered_subtitle)
                                } else {
                                    stringResource(R.string.empty_list_subtitle)
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                items(listState.items, key = { it.id }) { item ->
                    GroceryItemRow(
                        item = item,
                        onToggle = { viewModel.onIntent(GroceryListIntent.TogglePurchased(item)) },
                        onEdit = { viewModel.onIntent(GroceryListIntent.StartEdit(item)) },
                        onDelete = { viewModel.onIntent(GroceryListIntent.DeleteItem(item)) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}
