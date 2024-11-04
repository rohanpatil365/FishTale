package com.example.testjetpack.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.testjetpack.MyTopAppBar2
import com.example.testjetpack.R
import com.example.testjetpack.model.CustomerModel
import com.example.testjetpack.model.OrderItemModel
import com.example.testjetpack.model.OrderType
import com.example.testjetpack.model.PaymentMode
import com.example.testjetpack.model.PaymentModel
import com.example.testjetpack.service.DataService
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Serializable
object CreateEditOrder {
    var orderId: Int? = null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditOrderScreen(navController: NavHostController, orderId: Int?) {
    val isNewOrder : Boolean = orderId == null
    val title = if (isNewOrder) "Create Order" else "Edit Order"

    // state variables
    var id by remember { mutableStateOf<Int?>(orderId) }
    var orderDate by remember { mutableStateOf<LocalDate?>(null) }
    var orderType = OrderType.SELL
    var customer by remember {mutableStateOf<CustomerModel?>(null)}
    var orderItems by remember {mutableStateOf<List<OrderItemModel>>(emptyList())}
    var payments by remember { mutableStateOf<List<PaymentModel>>(emptyList()) }
    var discountAmount by remember { mutableIntStateOf(0) }

    // Customer dropdown data here
    val customers = DataService().getCustomers()
    var expanded by remember { mutableStateOf(false) }
    var selectedCustomerText by remember { mutableStateOf("Select Option") }
    var selectedCustomerId: Int? = null

    Scaffold(
        topBar = {
            MyTopAppBar2(navController = navController, title = title, showAction = false)
        }
    ) {
        it
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                CustomerDropdown(
                    expanded, customers, selectedCustomerText,
                    onCustomerSelected = { customerModel ->
                        selectedCustomerText = customerModel.name
                        selectedCustomerId = customerModel.id
                    },
                    onExpandedChange = {
                        expanded = it
                    }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                OrderDatePicker()
                Spacer(modifier = Modifier.padding(4.dp))
                FishItemListContainer()
                Spacer(modifier = Modifier.padding(4.dp))
                PaymentItemListContainer()
                Spacer(modifier = Modifier.padding(4.dp))
                Row {
                    OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                        Spacer(Modifier.width(8.dp))
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 16.dp))
                    Button(onClick = {}, modifier = Modifier.weight(1f)){
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Cancel")
                        Spacer(Modifier.width(8.dp))
                        Text("Save")
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDropdown(
    expanded: Boolean,
    customers: List<CustomerModel>,
    selectedCustomerText: String,
    onCustomerSelected: (CustomerModel) -> Unit,
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedCustomerText,
            onValueChange = { },
            label = { Text("Customer") },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = "Customer")
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            customers.forEach { customerModel ->
                DropdownMenuItem(
                    onClick = {
                        onCustomerSelected(customerModel)
                        onExpandedChange(false)
                    },
                    text = {
                        Text(text = customerModel.name)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDatePicker(
) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text("Order Date") },
        placeholder = { Text("MM/DD/YYYY") },
        leadingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )
    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FishItemListContainer() {
    val openAddFishItemDialog = remember { mutableStateOf(false) }
    val fishDropDownExpanded = remember { mutableStateOf(false) }
    val fishItems = DataService().getFishItems()
    LazyColumn() {
        stickyHeader {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Fish Items", style = MaterialTheme.typography.titleLarge)
                Badge(
                    content = {
                        Text(text = fishItems.size.toString())
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        openAddFishItemDialog.value = true
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Items")
                }
            }
        }
        items(fishItems) { fishItem ->
            ListItem(
                headlineContent = { Text(fishItem.name) },
                trailingContent = { Text(fishItem.id.toString()) },
                leadingContent = {
                    Icon(
                        painter = when (fishItem.id) {
                            1 -> painterResource(R.drawable.paplet)
                            2 -> painterResource(R.drawable.prawns)
                            3 -> painterResource(R.drawable.paplet)
                            4 -> painterResource(R.drawable.bombil)
                            5 -> painterResource(R.drawable.khekda)
                            else ->
                                painterResource(R.drawable.paplet)
                        },
                        contentDescription = "Payment Mode",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            HorizontalDivider()
        }
    }

    if(openAddFishItemDialog.value == true){
        AddFishItemDialog(
            onDismiss = { openAddFishItemDialog.value = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFishItemDialog(
    onDismiss: () -> Unit
) {
    val fishItems = DataService().getFishItems()
    var expanded by remember { mutableStateOf(false) }
    var selectedFishText by remember { mutableStateOf("Select fish") }
    var amount by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {onDismiss()}
    ) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Add Fish Item", style = MaterialTheme.typography.titleLarge, modifier = Modifier.fillMaxWidth() ,textAlign = TextAlign.Center)

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedFishText,
                        onValueChange = { },
                        label = { Text("Fish Type") },
                        leadingIcon = {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Fish Type")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        fishItems.forEach { fishModel ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedFishText = fishModel.name
                                    expanded = false
                                },
                                text = {
                                    Text(text = fishModel.name)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        newValue ->
                        // Allow only numeric input and remove decimal values
                        val filteredValue = newValue.filter { it.isDigit() }
                        amount = filteredValue
                    },
                    label = { Text("Amount") },
                    leadingIcon = {
                        Text("₹")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismiss() }
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick = { onDismiss() }
                    ) {
                        Text("Add")
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentItemListContainer() {
    val payments = DataService().getPayments()
    LazyColumn() {
        stickyHeader {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Payments", style = MaterialTheme.typography.titleLarge)
                Badge(
                    content = {
                        Text(text = payments.size.toString())
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Items")
                }
            }
        }
        items(payments) { paymentItem ->
            ListItem(
                headlineContent = { Text(paymentItem.paymentDate.toString()) },
                trailingContent = { Text(paymentItem.paymentMode.toString()) },
                leadingContent = {
                    Icon(
                        painter = if (paymentItem.paymentMode.equals(PaymentMode.CASH)) painterResource(
                            R.drawable.outline_currency_rupee_circle
                        ) else painterResource(R.drawable.outline_upi_pay),
                        contentDescription = "Payment Mode"
                    )
                }
            )
            HorizontalDivider()
        }

    }
}

@Composable
@Preview
fun CreateEditOrderScreenPreview() {
    CreateEditOrderScreen(rememberNavController(), null)
}