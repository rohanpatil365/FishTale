package com.example.testjetpack

import androidx.compose.ui.graphics.painter.Painter
import com.example.testjetpack.screens.Home

data class BottomNavigationItem(
    var title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val hasNews: Boolean,
    val route: Any,
    val badgeCount: Int? = null
)
