package com.example.testjetpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.testjetpack.screens.CreateEditOrder
import com.example.testjetpack.screens.CreateEditOrderScreen
import com.example.testjetpack.screens.OrderDetail
import com.example.testjetpack.screens.OrderDetailScreen

@Composable
fun MyNavHost(modifier: Modifier) {
    val navController = rememberNavController()
    val navControllerBottomBar = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel()

    LaunchedEffect(Unit) {
        sharedViewModel.initialize(navController)
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = MyBottomNavigationView
    ){
        composable<MyBottomNavigationView>{
            MyBottomNavigationView(navController = navControllerBottomBar, parentNavController = navController)
        }
        composable<OrderDetail>{
            val args = it.toRoute<OrderDetail>()
            OrderDetailScreen(navController = navController, orderId = args.orderId)
        }
        composable<CreateEditOrder>{
            val args = it.toRoute<CreateEditOrder>()
            CreateEditOrderScreen(navController = navController, orderId = args.orderId)
        }
    }
}