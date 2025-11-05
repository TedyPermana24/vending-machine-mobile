package com.example.jamuin.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jamuin.ui.screen.*
import com.example.jamuin.ui.viewmodel.CartViewModel
import com.example.jamuin.ui.viewmodel.ProductViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    cartViewModel: CartViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartItemCount = cartViewModel.getTotalItems()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                cartItemCount = cartItemCount
            )
        }
        
        composable(Screen.Products.route) {
            ProductListScreen(
                navController = navController,
                cartItemCount = cartItemCount,
                productViewModel = productViewModel
            )
        }
        
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                navController = navController,
                productId = productId,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel
            )
        }
        
        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        
        composable(Screen.ExpertSystem.route) {
            ExpertSystemScreen(
                navController = navController
            )
        }
        
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
        
        composable(
            route = Screen.TransactionStatus.route,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            TransactionStatusScreen(
                navController = navController,
                orderId = orderId
            )
        }
    }
}
