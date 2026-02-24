package com.shenawynkov.groceriesdemo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shenawynkov.groceriesdemo.domain.model.Category
import com.shenawynkov.groceriesdemo.R
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryEmoji
import com.shenawynkov.groceriesdemo.ui.grocerylist.categoryLabelResId
import com.shenawynkov.groceriesdemo.ui.theme.AddButtonBackground
import com.shenawynkov.groceriesdemo.ui.theme.AppPrimary
import com.shenawynkov.groceriesdemo.ui.theme.ChipUnselectedBackgroundDark
import com.shenawynkov.groceriesdemo.ui.theme.ChipUnselectedBackgroundLight
import com.shenawynkov.groceriesdemo.ui.theme.GradientEnd
import com.shenawynkov.groceriesdemo.ui.theme.GradientStart

@Composable
fun AddItemSection(
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    name: String,
    category: Category,
    validationErrorResId: Int?,
    onNameChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(GradientStart, GradientEnd)))
                .clickable { onExpandToggle() }
                .padding(vertical = 14.dp)
        ) {
            Text(
                text = stringResource(if (expanded) R.string.add_section_collapse else R.string.add_section_expand),
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = stringResource(R.string.label_item_name),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = { Text(stringResource(R.string.placeholder_item_name)) },
                    isError = validationErrorResId != null,
                    supportingText = validationErrorResId?.let { id -> { Text(stringResource(id)) } },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = stringResource(R.string.label_category),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                )
                CategoryChipRow(
                    selected = category,
                    onSelect = onCategoryChange
                )

                Button(
                    onClick = onAdd,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AddButtonBackground
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(stringResource(R.string.button_add_item), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun CategoryChipRow(
    selected: Category,
    onSelect: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val darkTheme = isSystemInDarkTheme()
    val chipUnselectedBg = if (darkTheme) ChipUnselectedBackgroundDark else ChipUnselectedBackgroundLight
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Category.entries.forEach { cat ->
            val isSelected = cat == selected
            val bg = if (isSelected) AppPrimary.copy(alpha = 0.15f) else chipUnselectedBg

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bg)
                    .clickable { onSelect(cat) }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(categoryEmoji(cat), fontSize = 20.sp)
                Text(
                    text = stringResource(categoryLabelResId(cat)),
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) AppPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
        }
    }
}
