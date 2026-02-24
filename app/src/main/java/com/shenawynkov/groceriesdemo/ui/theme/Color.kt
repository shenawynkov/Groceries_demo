package com.shenawynkov.groceriesdemo.ui.theme

import androidx.compose.ui.graphics.Color
import com.shenawynkov.groceriesdemo.domain.model.Category

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val AppPrimary = Color(0xFF6C63FF)
val GradientStart = Color(0xFF7B61FF)
val GradientEnd = Color(0xFF5B8DEF)
val AddButtonBackground = Color(0xFFB0B0B0)
val ChipUnselectedBackgroundLight = Color(0xFFF0F0F0)
val ChipUnselectedBackgroundDark = Color(0xFF3D3D3D)

val CategoryMilk = Color(0xFF1976D2)
val CategoryVegetables = Color(0xFF388E3C)
val CategoryFruits = Color(0xFFF57C00)
val CategoryBreads = Color(0xFF8D6E63)
val CategoryMeats = Color(0xFFC62828)

fun categoryColor(category: Category): Color = when (category) {
    Category.MILK -> CategoryMilk
    Category.VEGETABLES -> CategoryVegetables
    Category.FRUITS -> CategoryFruits
    Category.BREADS -> CategoryBreads
    Category.MEATS -> CategoryMeats
}