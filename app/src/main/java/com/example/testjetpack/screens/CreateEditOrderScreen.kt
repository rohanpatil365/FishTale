package com.example.testjetpack.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.testjetpack.model.OrderModel
import com.example.testjetpack.model.PaymentMode
import com.example.testjetpack.model.ProductModel
import com.example.testjetpack.service.DataService
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
object CreateEditOrder {
    var orderId: Int? = null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditOrderScreen(navController: NavHostController, orderId: Int?) {
    val isNewOrder: Boolean = orderId == null
    val title = if (isNewOrder) "Create Order" else "Edit Order"

    val orderObject : OrderModel = if (orderId != null) {
        DataService().getOrderDetail(orderId)
    } else {
        DataService().getEmptyOrderDetail()
    }

    var currentOrder by remember { mutableStateOf(orderObject)}

    // Customer dropdown data here
    val customers = DataService().getCustomers()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        MyTopAppBar2(navController = navController, title = title, showAction = false)
    }) {
        val openAddFishItemDialog = remember { mutableStateOf(false) }
        val openAddPaymentItemDialog = remember { mutableStateOf(false) }
        val fishItemListVisible = remember { mutableStateOf(true) }
        val paymentsListVisible = remember { mutableStateOf(true) }
        it
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {

            item {
                CustomerDropdown(expanded,
                    customers,
                    currentOrder.customer,
                    onCustomerSelected = { customerModel ->
                        currentOrder.customer = customerModel
                    },
                    onExpandedChange = {
                        expanded = it
                    })
                Spacer(modifier = Modifier.height(8.dp))
                OrderDatePicker(currentOrder.orderDate)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = {
                            fishItemListVisible.value = !fishItemListVisible.value
                        }
                    )
                ) {
                    Text("Fish Items", style = MaterialTheme.typography.titleLarge)
                    Badge(
                        content = {
                            Text(text = currentOrder.orderItems.size.toString())
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(width = 24.dp, height = 24.dp)
                    )
                    FilledTonalIconButton(onClick = {
                        openAddFishItemDialog.value = true
                    }) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add Items")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (!fishItemListVisible.value) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Fish Items Arrow",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
            if (fishItemListVisible.value) {
                items(currentOrder.orderItems) { fishItem ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = fishItem.product.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        trailingContent = {
                            Text(
                                text = formatAsCurrency(fishItem.amount),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                    HorizontalDivider()
                }
            }

            item {
                if (openAddFishItemDialog.value == true) {
                    AddFishItemDialog(
                        onDismiss = { openAddFishItemDialog.value = false },
                        onOrderChanged = { currentOrder = it},
                        currentOrder
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = {
                            paymentsListVisible.value = !paymentsListVisible.value
                        }
                    )
                ) {
                    Text("Payments", style = MaterialTheme.typography.titleLarge)
                    Badge(
                        content = {
                            Text(text = currentOrder.payments.size.toString())
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .size(width = 24.dp, height = 24.dp)
                    )
                    FilledTonalIconButton(
                        onClick = { openAddPaymentItemDialog.value = true }
                    ) {
                        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add Payment")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = if (!paymentsListVisible.value) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = "Payment Items Arrow",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
            if (paymentsListVisible.value) {
                items(items = currentOrder.payments) { paymentItem ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = paymentItem.paymentDate.toString(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        trailingContent = {
                            Text(
                                text = DataService().formatAsCurrency(paymentItem.amount),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
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

            item {
                if (openAddPaymentItemDialog.value == true) {
                    AddPaymentItemDialog(onDismiss = { openAddPaymentItemDialog.value = false })
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                DiscountToggleContainer(onOrderChanged = {currentOrder = it}, currentOrder)
                Spacer(modifier = Modifier.height(8.dp))
                OrderSummaryContainer(currentOrder)
                Spacer(modifier = Modifier.height(16.dp))
                CancelSaveButtonContainer(orderModel = currentOrder)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun OrderSummaryContainer(orderModel: OrderModel) {
    Row {
        Text("Summary", style = MaterialTheme.typography.titleLarge)
    }
    Spacer(modifier = Modifier.padding(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Subtotal",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = DataService().formatAsCurrency(orderModel.getTotalAmount()),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Spacer(modifier = Modifier.padding(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Discount",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = DataService().formatAsCurrency(orderModel.getDiscountAmount()),
            style = MaterialTheme.typography.bodyLarge
        )
//        Text(
//            text = orderModel.isDiscounted.toString(),
//            style = MaterialTheme.typography.bodyLarge
//        )
    }
    Spacer(modifier = Modifier.padding(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Paid Amount",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = DataService().formatAsCurrency(orderModel.getPaidAmount()),
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Spacer(modifier = Modifier.padding(4.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Due Amount",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = DataService().formatAsCurrency(orderModel.getDueAmount()),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun DiscountToggleContainer(onOrderChanged : (OrderModel) -> Unit, orderModel: OrderModel) {
    var checked by remember { mutableStateOf(orderModel.isDiscounted) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Apply Discount",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        Switch(checked = checked, onCheckedChange = {
            checked = it
            onOrderChanged(orderModel.copy(isDiscounted = it))
        }, thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        })
    }
}

@Composable
fun CancelSaveButtonContainer(orderModel: OrderModel) {
    Row {
        OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
            Spacer(Modifier.width(8.dp))
            Text("Cancel")
        }
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Button(onClick = {}, modifier = Modifier.weight(1f)) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Cancel")
            Spacer(Modifier.width(8.dp))
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDropdown(
    expanded: Boolean,
    customers: List<CustomerModel>,
    selectedCustomer: CustomerModel,
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
            value = selectedCustomer.name,
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
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            customers.forEach { customerModel ->
                DropdownMenuItem(onClick = {
                    onCustomerSelected(customerModel)
                    onExpandedChange(false)
                }, text = {
                    Text(text = customerModel.name)
                }, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDatePicker(orderDate: LocalDate) {
    var selectedDate by remember { mutableStateOf<LocalDate>(orderDate) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(value = selectedDate.let { getDisplayDate(it) },
        onValueChange = { },
        readOnly = true,
        label = { Text("Order Date") },
        placeholder = { Text("yyyy/MM/dd") },
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
            })
    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false },
            selectedDate = selectedDate
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (LocalDate) -> Unit, onDismiss: () -> Unit, selectedDate: LocalDate?
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
            ?.toEpochMilli()
    )

    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateSelected(
                DataService().convertMillisToDate(
                    datePickerState.selectedDateMillis
                )
            )

            onDismiss()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}

fun getDisplayDate(orderDate: LocalDate): String {
    return orderDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFishItemDialog(
    onDismiss: () -> Unit,
    onOrderChanged: (OrderModel) -> Unit,
    currentOrder: OrderModel
) {
    val fishItems = DataService().getFishItems()
    var expanded by remember { mutableStateOf(false) }
    var selectedFishText by remember { mutableStateOf("Select fish") }
    var selectedFishProduct by remember { mutableStateOf<ProductModel?>(null) }
    var amount by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Add Fish Item",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                textAlign = TextAlign.Center
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
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
                    onDismissRequest = { expanded = false }) {
                    fishItems.forEach { fishModel ->
                        DropdownMenuItem(onClick = {
                            selectedFishText = fishModel.name
                            selectedFishProduct = fishModel
                            expanded = false
                        }, text = {
                            Text(text = fishModel.name)
                        }, modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    // Allow only numeric input and remove decimal values
                    val filteredValue = newValue.filter { it.isDigit() }
                    amount = filteredValue
                },
                label = { Text("Amount") },
                leadingIcon = {
                    Text("₹")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                TextButton(onClick = {
                    if(selectedFishProduct != null && amount != ""){
                        val newOrderItem : OrderItemModel =
                            OrderItemModel(-1, currentOrder.id, selectedFishProduct!!, null, amount.toInt())

                        onOrderChanged(
                            currentOrder.copy(orderItems = (currentOrder.orderItems + newOrderItem))
                        )
                    }else{
                        // show snackbar
                    }
                    onDismiss()
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentItemDialog(
    onDismiss: () -> Unit
) {
    val paymentModeList = mutableListOf<PaymentMode>(
        PaymentMode.CASH, PaymentMode.ONLINE
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedPaymentMode by remember { mutableStateOf("Select payment mode") }
    var amount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Add Payment",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                textAlign = TextAlign.Center
            )

            PaymentDialogDatePicker()

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedPaymentMode,
                    onValueChange = { },
                    label = { Text("Payment Mode") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.payments_outline),
                            contentDescription = "Fish Type"
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    paymentModeList.forEach { paymentMode ->
                        DropdownMenuItem(onClick = {
                            selectedPaymentMode = paymentMode.name
                            expanded = false
                        }, text = {
                            Text(text = paymentMode.name)
                        }, modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { newValue ->
                    // Allow only numeric input and remove decimal values
                    val filteredValue = newValue.filter { it.isDigit() }
                    amount = filteredValue
                },
                label = { Text("Amount") },
                leadingIcon = {
                    Text("₹")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(16.dp))
                TextButton(onClick = { onDismiss() }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDialogDatePicker(
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(value = selectedDate?.let { getDisplayDate(it) } ?: "",
        onValueChange = { },
        readOnly = true,
        label = { Text("Payment Date") },
        placeholder = { Text("yyyy/MM/dd") },
        leadingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            })
    if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false },
            selectedDate = selectedDate
        )
    }
}


@Composable
@Preview()
fun CreateEditOrderScreenPreview() {
    CreateEditOrderScreen(rememberNavController(), 1)
}