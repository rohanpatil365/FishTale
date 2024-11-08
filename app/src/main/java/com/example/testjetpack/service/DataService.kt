package com.example.testjetpack.service

import com.example.testjetpack.model.CustomerModel
import com.example.testjetpack.model.OrderItemModel
import com.example.testjetpack.model.OrderModel
import com.example.testjetpack.model.OrderType
import com.example.testjetpack.model.PaymentMode
import com.example.testjetpack.model.PaymentModel
import com.example.testjetpack.model.ProductModel
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Locale

class DataService {
    fun getPurchase(): List<OrderModel> {
        var orderList = mutableListOf<OrderModel>(
            OrderModel(
                1,
                LocalDate.now(),
                OrderType.PURCHASE,
                CustomerModel(
                    1,
                    "Rohan",
                    "9011560602",
                    "B/402, Vishakha Apt, Viva Tarangan, Virar East",
                    true
                ),
                mutableListOf<OrderItemModel>(
                    OrderItemModel(1, 1, ProductModel(1, "Fish1"), 10, 1000),
                    OrderItemModel(2, 1, ProductModel(2, "Fish2"), 20, 1000),
                    OrderItemModel(3, 1, ProductModel(3, "Fish3"), 30, 2000),
                    OrderItemModel(4, 1, ProductModel(4, "Fish4"), 40, 4000),
                    OrderItemModel(5, 1, ProductModel(5, "Fish5"), 5, 800)
                ),
                emptyList(),
                false
            ),
            OrderModel(
                2,
                LocalDate.now().minusDays(1),
                OrderType.PURCHASE,
                CustomerModel(2, "Pratiksha", "9011560562", "A/405, Daji Palace, Virar East", true),
                mutableListOf<OrderItemModel>(
                    OrderItemModel(5, 2, ProductModel(3, "Fish3"), 30, 2000),
                    OrderItemModel(6, 2, ProductModel(4, "Fish4"), 40, 4000),
                    OrderItemModel(7, 2, ProductModel(5, "Fish5"), 5, 800)
                ),
                mutableListOf<PaymentModel>(
                    PaymentModel(1, 2, 2, LocalDate.now(), 3800, PaymentMode.CASH)
                ),
                true
            ),
            OrderModel(
                3,
                LocalDate.now().minusDays(3),
                OrderType.PURCHASE,
                CustomerModel(2, "Pratiksha", "9011560562", "A/405, Daji Palace, Virar East", true),
                mutableListOf<OrderItemModel>(
                    OrderItemModel(8, 3, ProductModel(3, "Fish3"), 30, 2000),
                    OrderItemModel(9, 3, ProductModel(4, "Fish4"), 40, 4000),
                    OrderItemModel(10, 3, ProductModel(5, "Fish5"), 5, 800)
                ),
                mutableListOf<PaymentModel>(
                    PaymentModel(2, 2, 3, LocalDate.now().minusDays(3), 3100, PaymentMode.CASH),
                    PaymentModel(3, 2, 3, LocalDate.now().minusDays(1), 3700, PaymentMode.ONLINE),
                ),
                false
            )

        )
        return orderList
    }

    fun getOrderDetail(orderId: Int): OrderModel {
        var orderList = getPurchase()
        for( order in orderList){
            if(order.id == orderId){
                return order
            }
        }
        return getEmptyOrderDetail()
    }
    fun getEmptyOrderDetail(): OrderModel {
        return OrderModel(-1, LocalDate.now(), OrderType.PURCHASE, getEmptyCustomer(), emptyList(), emptyList(), false )
    }

    fun getCustomers(): List<CustomerModel> {
        var customerList = mutableListOf<CustomerModel>(
            CustomerModel(1, "Rohan", "9011560602", "B/402, Vishakha Apt, Viva Tarangan, Virar East", true),
            CustomerModel(2, "Pratiksha", "9011560562", "A/405, Daji Palace, Virar East", true)
        )
        return customerList
    }

    fun getEmptyCustomer(): CustomerModel {
        return CustomerModel(-1, "Select Customer", "", "", true)
    }

    fun getFishItems(): List<ProductModel> {
        var fishList = mutableListOf<ProductModel>(
            ProductModel(1, "Salmon"),
            ProductModel(2, "Pronze"),
            ProductModel(3, "Pompret"),
            ProductModel(4, "Bombay Duck"),
            ProductModel(5, "Crab")
        )
        return fishList
    }

    fun getPayments(): List<PaymentModel> {
        var paymentList = mutableListOf<PaymentModel>(
            PaymentModel(2, 2, 3, LocalDate.now().minusDays(3), 3100, PaymentMode.CASH),
            PaymentModel(3, 2, 3, LocalDate.now().minusDays(1), 3700, PaymentMode.ONLINE),
        )
        return paymentList
    }

    fun getCustomerById(customerId: Int): CustomerModel? {
        var customerList = getCustomers()
        return customerList.find { it.id == customerId }
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

    fun convertMillisToDate(millis: Long?): LocalDate {
        if (millis == null) {
            return LocalDate.now()
        } else {
            return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }
    }
}