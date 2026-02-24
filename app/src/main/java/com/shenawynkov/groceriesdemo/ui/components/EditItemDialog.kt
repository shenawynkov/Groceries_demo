package com.shenawynkov.groceriesdemo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.domain.model.GroceryItem
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryEmoji
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryLabelResId
import com.shenawynkov.groceriesdemo.ui.theme.AppPrimary
import com.shenawynkov.groceriesdemo.ui.theme.ChipUnselectedBackgroundDark
import com.shenawynkov.groceriesdemo.ui.theme.ChipUnselectedBackgroundLight

@Composable
fun EditItemDialog(
    item: GroceryItem,
    validationErrorResId: Int? = null,
    onConfirm: (String, Category) -> Unit,
    onDismiss: () -> Unit
) {
    var editName by remember { mutableStateOf(item.name) }
    var editCategory by remember { mutableStateOf(item.category) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_edit_item_title)) },
        text = {
            Column {
                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text(stringResource(R.string.dialog_edit_item_name_label)) },
                    isError = validationErrorResId != null,
                    supportingText = validationErrorResId?.let { id -> { Text(stringResource(id)) } },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text(stringResource(R.string.label_category), style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                val darkTheme = isSystemInDarkTheme()
                val chipUnselectedBg = if (darkTheme) ChipUnselectedBackgroundDark else ChipUnselectedBackgroundLight
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Category.entries.forEach { cat ->
                        val selected = cat == editCategory
                        val bg = if (selected) AppPrimary.copy(alpha = 0.15f) else chipUnselectedBg
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(bg)
                                .clickable { editCategory = cat }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(categoryEmoji(cat), fontSize = 18.sp)
                            Text(
                                stringResource(categoryLabelResId(cat)),
                                fontSize = 10.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) AppPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(editName, editCategory) }) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        }
    )
}
