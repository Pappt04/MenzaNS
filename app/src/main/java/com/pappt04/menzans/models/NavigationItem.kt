package com.pappt04.menzans.models

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: Uitext,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val badgeCount: Int? = null,
)
