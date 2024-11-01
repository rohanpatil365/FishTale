package com.example.testjetpack

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MyNavigationBar(items : List<BottomNavigationItem>, navController : NavController){
    NavigationBar {
        var selectedItemIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                alwaysShowLabel = false,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.route)
                },
                label = {
                    Text(
                        text = item.title,
                        fontSize = 9.sp
                    )
                },
                icon = {
                    if (selectedItemIndex == index) {
                        Icon(painter = item.selectedIcon, contentDescription = item.title)
                    }
                    else {
                        Icon(painter = item.unselectedIcon, contentDescription = item.title)
                    }
                }
            )
        }
    }
}