package com.example.testjetpack

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testjetpack.screens.Home
import com.example.testjetpack.screens.HomeScreen
import com.example.testjetpack.screens.OrderDetail
import com.example.testjetpack.screens.OrderDetailScreen
import com.example.testjetpack.screens.Orders
import com.example.testjetpack.screens.OrdersScreen
import com.example.testjetpack.screens.Payments
import com.example.testjetpack.screens.PaymentsScreen
import com.example.testjetpack.screens.Settings
import com.example.testjetpack.screens.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object MyBottomNavigationView {

}

@Composable
fun MyBottomNavigationView(navController: NavHostController, parentNavController : NavHostController) {
    val sharedViewModel: SharedViewModel = SharedViewModel()

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            route = Home,
            selectedIcon = rememberVectorPainter(Icons.Filled.Home),
            unselectedIcon = rememberVectorPainter(Icons.Outlined.Home),
            hasNews = false,
        ),
        BottomNavigationItem(
            title = "Orders",
            route = Orders,
            selectedIcon = painterResource(R.drawable.sell_fill),
            unselectedIcon = painterResource(R.drawable.sell_outline),
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Payments",
            route = Payments,
            selectedIcon = painterResource(R.drawable.payments_fill),
            unselectedIcon = painterResource(R.drawable.payments_outline),
            hasNews = false
        ),
        BottomNavigationItem(
            title = "Settings",
            route = Settings,
            selectedIcon = rememberVectorPainter(Icons.Filled.Settings),
            unselectedIcon = rememberVectorPainter(Icons.Outlined.Settings),
            hasNews = true
        ),
    )

    Scaffold(
        topBar = {
            MyTopAppBar()
        },
        bottomBar = {
            MyNavigationBar(items, navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<Home> {
                HomeScreen()
            }
            composable<Orders> {
                OrdersScreen(sharedViewModel, navController, parentNavController)
            }

            composable<Payments> {
                PaymentsScreen()
            }

            composable<Settings> {
                SettingsScreen()
            }

        }
    }
}