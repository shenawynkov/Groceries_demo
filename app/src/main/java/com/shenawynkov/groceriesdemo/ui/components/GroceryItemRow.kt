package com.shenawynkov.groceriesdemo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.domain.model.GroceryItem
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryEmoji
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryLabelResId
import com.shenawynkov.groceriesdemo.ui.theme.categoryColor

@Composable
fun GroceryItemRow(
    item: GroceryItem,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Checkbox(checked = item.isPurchased, onCheckedChange = { onToggle() })

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (item.isPurchased) TextDecoration.LineThrough else null
                    ),
                    color = if (item.isPurchased) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                SuggestionChip(
                    onClick = {},
                    label = { Text("${categoryEmoji(item.category)} ${stringResource(categoryLabelResId(item.category))}", style = MaterialTheme.typography.labelSmall) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = categoryColor(item.category).copy(alpha = 0.15f),
                        labelColor = categoryColor(item.category)
                    )
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.content_desc_edit))
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.content_desc_delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
