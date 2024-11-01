package com.example.testjetpack.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object Payments{

}

@Composable
fun PaymentsScreen(){
    Text(
        text = "Payments",
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = MaterialTheme.colorScheme.primary
    )
}