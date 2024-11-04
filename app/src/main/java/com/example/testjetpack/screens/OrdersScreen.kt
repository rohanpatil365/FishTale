package com.example.testjetpack.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testjetpack.CustomerInitialsIcon
import com.example.testjetpack.service.DataService
import kotlinx.serialization.Serializable

@Serializable
object Orders{}

@Composable
fun OrdersScreen(navController: NavHostController, parentNavController: NavHostController) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = {
                    Text(text = "New Order")
                },
                onClick = {
                    val createOrderObject = CreateEditOrder
                    parentNavController.navigate(createOrderObject)
                }
            )
        },
        content = {
            paddingValues -> OrderList(paddingValues, parentNavController)
        }
    )
}

@Composable
fun OrderList(paddings: PaddingValues, parentNavController: NavHostController){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(DataService().getPurchase()) { item ->
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val orderDetailObject = OrderDetail
                    orderDetailObject.orderId = item.id
                    parentNavController.navigate(orderDetailObject)
                }
            ) {
                Row(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        CustomerInitialsIcon(customerName = item.customer.name)
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = item.customer.name,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = formatAsCurrency(item.getTotalAmount()),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = item.getDisplayDate(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = item.getStatus(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                icon = {
                                    if (item.isPaid()) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Done",
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                },
                                enabled = true,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}