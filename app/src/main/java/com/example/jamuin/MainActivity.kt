package com.example.jamuin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.jamuin.navigation.AppNavigation
import com.example.jamuin.ui.theme.JamuinTheme
import com.example.jamuin.ui.viewmodel.CartViewModel
import com.example.jamuin.ui.viewmodel.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JamuinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val cartViewModel: CartViewModel = viewModel()
                    val productViewModel: ProductViewModel = viewModel()
                    
                    AppNavigation(
                        navController = navController,
                        cartViewModel = cartViewModel,
                        productViewModel = productViewModel
                    )
                }
            }
        }
    }
}
