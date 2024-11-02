package com.example.testjetpack.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class PaymentModel(
    var id: Int,
    var customerId: Int,
    var orderId: Int,
    var paymentDate: LocalDate,
    var amount: Int,
    var paymentMode: PaymentMode
) {
    fun getDisplayPaymentDate(): String {
        return paymentDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    }
}
