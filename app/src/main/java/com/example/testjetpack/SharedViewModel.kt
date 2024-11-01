package com.example.testjetpack

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController

class SharedViewModel : ViewModel() {
    lateinit var parentNavController: NavHostController

    fun initialize(navController: NavHostController) {
        parentNavController = navController
    }

//    fun getParentNavController(): NavHostController {
//        return parentNavController
//    }

}