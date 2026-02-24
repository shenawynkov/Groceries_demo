package com.shenawynkov.groceriesdemo.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryEmoji
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryLabelResId

@Composable
fun CategoryFilterChips(
    activeFilter: Category?,
    onFilterSelected: (Category?) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf(null to stringResource(R.string.filter_all)) +
        Category.entries.map { it to "${categoryEmoji(it)} ${stringResource(categoryLabelResId(it))}" }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        items(options, key = { it.first?.name ?: "all" }) { (category, label) ->
            FilterChip(
                selected = activeFilter == category,
                onClick = { onFilterSelected(category) },
                label = { Text(label) }
            )
        }
    }
}
