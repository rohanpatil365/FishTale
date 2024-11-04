package com.example.testjetpack.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.testjetpack.MyTopAppBar2
import com.example.testjetpack.R
import com.example.testjetpack.model.OrderModel
import com.example.testjetpack.model.PaymentModel
import com.example.testjetpack.service.DataService
import kotlinx.serialization.Serializable
import java.text.NumberFormat
import java.util.Locale

@Serializable
object OrderDetail {
    var orderId: Int? = null
}

@Composable
fun OrderDetailScreen(navController: NavHostController, orderId: Int?) {
    Scaffold(
        topBar = {
            MyTopAppBar2(navController = navController, title = "Order Detail", showAction = true)
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
                    var chipBackgroundColor =
                        if (orderDetail.isPaid()) colorResource(R.color.done) else colorResource(
                            R.color.pending
                        )
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = orderDetail.customer.name,
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = orderDetail.getDisplayDate(),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Order #${orderDetail.id}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            InputChip(
                                selected = true,
                                onClick = {},
                                label = {
                                    Text(
                                        text = orderDetail.getStatus(),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    selectedContainerColor = chipBackgroundColor
                                )
                            )
                        }

                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Column{
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Items",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Badge(
                                            content = {
                                                Text(text = orderDetail.getItemCount().toString())
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.fillMaxWidth().padding(4.dp))
                                    ItemPriceHeader("Fish", "Price")
                                    ItemPriceListView(orderDetail)
                                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                                }
                                Column{
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Payments",
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Badge(
                                            content = {
                                                Text(text = orderDetail.getPaymentCount().toString())
                                            },
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.fillMaxWidth().padding(4.dp))
                                    if (orderDetail.payments != null) {
                                        PaymentHeader("Date","Mode","Amount")
                                        PaymentListView(orderDetail)
                                    } else {
                                        Text(
                                            text = "No payments found"
                                        )
                                    }
                                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                                }
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Subtotal",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Left
                                        )
                                        Text(
                                            text = formatAsCurrency(orderDetail.getTotalAmount()),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Right
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Discount",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Left
                                        )
                                        Text(
                                            text = formatAsNegativeCurrency(orderDetail.discountAmount),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Right
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Paid Amount",
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Left
                                        )
                                        Text(
                                            text = formatAsNegativeCurrency(orderDetail.getPaidAmount()),
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Right
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Due Amount",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Left
                                        )
                                        Text(
                                            text = formatAsCurrency(orderDetail.getDueAmount()),
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Right
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Text(text = "Oops error occurred")
                }
            }
        }

    }
}

@Composable
fun ItemPriceHeader(itemsLabel: String, priceLabel: String) {
    Row{
        Text(
            text = itemsLabel,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = priceLabel,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun ItemPriceListView(orderDetail: OrderModel) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(orderDetail.orderItems) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Left
                )
                Text(
                    text = formatAsPositiveCurrency(item.amount),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Composable
fun PaymentHeader(dateHeader: String, modeHeader: String, amountHeader: String) {
    Row{
        Text(
            text = dateHeader,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = modeHeader,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = amountHeader,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun PaymentListView(orderDetail: OrderModel) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(orderDetail.payments as List<PaymentModel>) { payment ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = payment.getDisplayPaymentDate(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Left
                )
                Text(
                    text = payment.paymentMode.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = formatAsNegativeCurrency(payment.amount),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Right
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
fun OrderDetailScreenPreview() {
    OrderDetailScreen(rememberNavController(), 2)
}

fun formatAsCurrency(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN")) // India locale
    format.maximumFractionDigits = 0 // No decimal places
    return format.format(amount)
}

fun formatAsPositiveCurrency(amount: Int) : String {
    return "+ " + formatAsCurrency(amount)
}

fun formatAsNegativeCurrency(amount: Int) : String {
    return "- " + formatAsCurrency(amount)
}