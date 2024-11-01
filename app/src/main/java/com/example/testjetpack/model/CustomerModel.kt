package com.example.testjetpack.model

data class CustomerModel(
    var id : Int,
    var name : String,
    var phone : String,
    var address : String,
    var active : Boolean ?= true
)
