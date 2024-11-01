package com.example.testjetpack.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.testjetpack.MyTopAppBar2
import com.example.testjetpack.model.OrderModel
import com.example.testjetpack.service.DataService
import kotlinx.serialization.Serializable

@Serializable
object OrderDetail {
    var orderId: Int? = null
}

@Composable
fun OrderDetailScreen(navController: NavHostController, orderId: Int?) {
    Scaffold(
        topBar = {
            MyTopAppBar2(navController)
        }
    ) {
        it

        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            if (orderId != null) {
                val orderDetail = DataService().getOrderDetail(orderId)
                if (orderDetail != null) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "Order #${orderDetail.id}"
                        )
                    }
                } else {
                    Text(text = "Oops error occurred")
                }
            } else {
                Text(text = "New Order Flow")
            }
        }

    }
}



@Composable
@Preview(showBackground = true)
fun OrderDetailScreenPreview() {
    OrderDetailScreen(rememberNavController(), 1)
}