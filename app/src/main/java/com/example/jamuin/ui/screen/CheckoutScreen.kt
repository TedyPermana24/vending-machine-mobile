package com.example.jamuin.ui.screen

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jamuin.navigation.Screen
import com.example.jamuin.ui.viewmodel.CartViewModel
import com.example.jamuin.ui.viewmodel.TransactionUiState
import com.example.jamuin.ui.viewmodel.TransactionViewModel
import com.example.jamuin.util.CurrencyFormatter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartViewModel.getTotalPrice()
    val uiState by transactionViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    var customerName by remember { mutableStateOf("User Mobile") }
    var showPaymentWebView by remember { mutableStateOf(false) }
    var snapUrl by remember { mutableStateOf("") }
    var orderId by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is TransactionUiState.Success -> {
                snapUrl = state.response.snapUrl
                orderId = state.response.orderId
                showPaymentWebView = true
            }
            else -> {}
        }
    }

    if (showPaymentWebView && snapUrl.isNotEmpty()) {
        PaymentWebView(
            snapUrl = snapUrl,
            orderId = orderId,
            onPaymentComplete = { success ->
                // Use coroutine scope to ensure we're on main thread
                scope.launch {
                    showPaymentWebView = false

                    if (success) {
                        // Clear state first
                        cartViewModel.clearCart()
                        transactionViewModel.resetState()

                        // Navigate
                        try {
                            navController.navigate(Screen.TransactionStatus.createRoute(orderId)) {
                                popUpTo(Screen.Home.route) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("CheckoutScreen", "Navigation error", e)
                        }
                    } else {
                        transactionViewModel.resetState()
                    }
                }
            },
            onClose = {
                scope.launch {
                    showPaymentWebView = false
                    transactionViewModel.resetState()
                }
            }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Checkout") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Keranjang Kosong",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = { navController.navigate(Screen.Products.route) }) {
                            Text("Lihat Produk")
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Order Summary
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                "Ringkasan Pesanan",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            cartItems.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            item.product.nama,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "${item.quantity}x ${CurrencyFormatter.formatSimple(item.product.harga)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        CurrencyFormatter.formatSimple(item.totalPrice),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    CurrencyFormatter.formatSimple(totalPrice),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    // Payment Button
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        when (uiState) {
                            is TransactionUiState.Loading -> {
                                Button(
                                    onClick = { },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = false,
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Memproses...")
                                }
                            }
                            is TransactionUiState.Error -> {
                                val errorState = uiState as TransactionUiState.Error
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.Warning,
                                            null,
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            errorState.message,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        if (cartItems.isNotEmpty()) {
                                            val firstItem = cartItems.first()
                                            transactionViewModel.createTransaction(
                                                productId = firstItem.product.id,
                                                quantity = firstItem.quantity,
                                                customerName = customerName
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Coba Lagi", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                            else -> {
                                Button(
                                    onClick = {
                                        if (cartItems.isNotEmpty()) {
                                            val firstItem = cartItems.first()
                                            transactionViewModel.createTransaction(
                                                productId = firstItem.product.id,
                                                quantity = firstItem.quantity,
                                                customerName = customerName
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    Icon(Icons.Default.ShoppingCart, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Bayar Sekarang", style = MaterialTheme.typography.titleMedium)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentWebView(
    snapUrl: String,
    orderId: String,
    onPaymentComplete: (Boolean) -> Unit,
    onClose: () -> Unit
) {
    var hasNavigated by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            hasNavigated = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pembayaran") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)

                            // Prevent multiple navigation
                            if (hasNavigated) return

                            url?.let { currentUrl ->
                                android.util.Log.d("PaymentWebView", "Page finished: $currentUrl")

                                // Check for Midtrans finish URL patterns
                                val isSuccess = currentUrl.contains("status_code=200") ||
                                        currentUrl.contains("transaction_status=settlement") ||
                                        currentUrl.contains("transaction_status=capture") ||
                                        (currentUrl.contains("/finish") && !currentUrl.contains("unfinish"))

                                val isPending = currentUrl.contains("status_code=201") ||
                                        currentUrl.contains("transaction_status=pending") ||
                                        currentUrl.contains("/unfinish")

                                val isFailed = currentUrl.contains("status_code=202") ||
                                        currentUrl.contains("transaction_status=deny") ||
                                        currentUrl.contains("transaction_status=cancel") ||
                                        currentUrl.contains("transaction_status=expire") ||
                                        currentUrl.contains("transaction_status=failure")

                                when {
                                    isSuccess || isPending -> {
                                        if (!hasNavigated) {
                                            hasNavigated = true
                                            android.util.Log.d("PaymentWebView", "Payment success/pending detected")

                                            // Post to main thread handler
                                            view?.post {
                                                onPaymentComplete(true)
                                            }
                                        }
                                    }
                                    isFailed -> {
                                        if (!hasNavigated) {
                                            hasNavigated = true
                                            android.util.Log.d("PaymentWebView", "Payment failed detected")

                                            // Post to main thread handler
                                            view?.post {
                                                onPaymentComplete(false)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            android.util.Log.d("PaymentWebView", "Should override: $url")
                            return false
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            android.util.Log.d("PaymentWebView", "Loading URL: $url")
                        }
                    }

                    loadUrl(snapUrl)
                }
            }
        )
    }
}