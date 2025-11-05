package com.example.jamuin.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Products : Screen("products")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object ExpertSystem : Screen("expert_system")
    object Checkout : Screen("checkout")
    object Payment : Screen("payment/{orderId}") {
        fun createRoute(orderId: String) = "payment/$orderId"
    }
    object TransactionStatus : Screen("transaction_status/{orderId}") {
        fun createRoute(orderId: String) = "transaction_status/$orderId"
    }
}
