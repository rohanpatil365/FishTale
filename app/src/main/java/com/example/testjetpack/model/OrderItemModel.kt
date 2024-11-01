package com.example.testjetpack.model

data class OrderItemModel(
    var id : Int,
    var orderId : Int,
    var product : ProductModel,
    var quantity : Int ? = null,
    var amount : Int
)
