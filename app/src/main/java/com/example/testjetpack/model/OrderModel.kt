package com.example.testjetpack.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class OrderModel(
    var id : Int,
    var orderDate : LocalDate,
    var orderType : OrderType,
    var customer : CustomerModel,
    var orderItems : List<OrderItemModel>,
    var payments : List<PaymentModel> ?= null,
    var discountAmount : Int
){

    fun getTotalAmount() : Int{
        return orderItems.map {it.amount}.sum()
    }

    fun getDisplayTotalAmount() : String {
        return "₹ " + getTotalAmount()
    }

    fun getDisplayDate() : String {
        val today = LocalDate.now()
        if(orderDate.isEqual(today)){
            return "Today"
        }else if(orderDate.isEqual(today.minusDays(1))){
            return "Yesterday"
        } else {
//            return orderDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMM"))
            return orderDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
        }
    }

    fun getItemCount() : Int{
        return orderItems.size
    }

    fun getPaidAmount() : Int{
        var paidAmount = payments?.map { it.amount }?.sum()
        return paidAmount ?: 0
    }

    fun isPaid() : Boolean{
        var orderAmount = getTotalAmount()
        var paidAmount = getPaidAmount()
        var pendingAmount = orderAmount - paidAmount - discountAmount;
        if(pendingAmount == 0){
            return true
        }else{
            return false
        }
    }

    fun getStatus() : String{
        if(isPaid()){
            return "Paid"
        }else{
            return "Unpaid"
        }
    }
}
