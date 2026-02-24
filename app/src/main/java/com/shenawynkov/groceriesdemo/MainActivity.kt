package com.shenawynkov.groceriesdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shenawynkov.groceriesdemo.ui.grocerylist.GroceryListScreen
import com.shenawynkov.groceriesdemo.ui.theme.GroceriesDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GroceriesDemoTheme {
                GroceryListScreen()
            }
        }
    }
}
