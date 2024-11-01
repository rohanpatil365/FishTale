package com.example.testjetpack.model

import java.time.LocalDate

data class PaymentModel(
    var id : Int,
    var customerId : Int,
    var orderId : Int,
    var paymentDate : LocalDate,
    var amount : Int,
    var paymentMode : PaymentMode
)
