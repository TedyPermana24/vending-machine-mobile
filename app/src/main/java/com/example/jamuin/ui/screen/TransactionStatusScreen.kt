package com.example.jamuin.ui.screen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jamuin.navigation.Screen
import com.example.jamuin.ui.viewmodel.TransactionStatusUiState
import com.example.jamuin.ui.viewmodel.TransactionViewModel
import com.example.jamuin.util.CurrencyFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionStatusScreen(
    navController: NavController,
    orderId: String,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val statusUiState by transactionViewModel.statusUiState.collectAsState()
    
    LaunchedEffect(orderId) {
        transactionViewModel.getTransactionStatus(orderId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Status Transaksi") },
                navigationIcon = {
                    IconButton(onClick = { 
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Home, "Home")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = statusUiState) {
                is TransactionStatusUiState.Loading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Memuat status transaksi...")
                    }
                }
                is TransactionStatusUiState.Success -> {
                    val status = state.status
                    
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Status Icon and Message
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = when (status.status.lowercase()) {
                                    "success", "settlement" -> MaterialTheme.colorScheme.primaryContainer
                                    "pending" -> MaterialTheme.colorScheme.tertiaryContainer
                                    "failed", "expired", "cancelled" -> MaterialTheme.colorScheme.errorContainer
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = when (status.status.lowercase()) {
                                        "success", "settlement" -> Icons.Default.CheckCircle
                                        "pending" -> Icons.Default.DateRange
                                        "failed", "expired", "cancelled" -> Icons.Default.Close
                                        else -> Icons.Default.Info
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = when (status.status.lowercase()) {
                                        "success", "settlement" -> MaterialTheme.colorScheme.primary
                                        "pending" -> MaterialTheme.colorScheme.tertiary
                                        "failed", "expired", "cancelled" -> MaterialTheme.colorScheme.error
                                        else -> MaterialTheme.colorScheme.outline
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = when (status.status.lowercase()) {
                                        "success", "settlement" -> "Pembayaran Berhasil!"
                                        "pending" -> "Menunggu Pembayaran"
                                        "failed" -> "Pembayaran Gagal"
                                        "expired" -> "Pembayaran Kedaluwarsa"
                                        "cancelled" -> "Pembayaran Dibatalkan"
                                        else -> "Status: ${status.status}"
                                    },
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = when (status.status.lowercase()) {
                                        "success", "settlement" -> "Terima kasih! Pesanan Anda akan segera diproses."
                                        "pending" -> "Silakan selesaikan pembayaran Anda."
                                        "failed" -> "Pembayaran tidak dapat diproses. Silakan coba lagi."
                                        "expired" -> "Waktu pembayaran telah habis. Silakan buat pesanan baru."
                                        "cancelled" -> "Pembayaran telah dibatalkan."
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        
                        // Transaction Details
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "Detail Transaksi",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                Divider()
                                
                                DetailRow("Order ID", status.orderId)
                                if (status.transactionId != null) {
                                    DetailRow("Transaction ID", status.transactionId)
                                }
                                if (status.paymentType != null) {
                                    DetailRow("Metode Pembayaran", status.paymentType.uppercase())
                                }
                                DetailRow(
                                    "Total Pembayaran",
                                    CurrencyFormatter.formatSimple(status.grossAmount)
                                )
                            }
                        }
                        
                        // Product Information
                        status.product?.let { product ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Produk",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        product.nama,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        CurrencyFormatter.formatSimple(product.harga),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        
                        // Customer Information
                        status.customer?.let { customer ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Informasi Pembeli",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    DetailRow("Nama", customer.name)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Action Buttons
                        when (status.status.lowercase()) {
                            "success", "settlement" -> {
                                Button(
                                    onClick = { 
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Home.route) { inclusive = true }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    Icon(Icons.Default.Home, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Kembali ke Beranda")
                                }
                            }
                            "pending" -> {
                                Button(
                                    onClick = { transactionViewModel.getTransactionStatus(orderId) },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Refresh Status")
                                }
                            }
                            "failed", "expired", "cancelled" -> {
                                Button(
                                    onClick = { 
                                        navController.navigate(Screen.Products.route) {
                                            popUpTo(Screen.Home.route)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(vertical = 16.dp)
                                ) {
                                    Icon(Icons.Default.ShoppingCart, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Belanja Lagi")
                                }
                            }
                        }
                        
                        OutlinedButton(
                            onClick = { 
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            Text("Kembali ke Beranda")
                        }
                    }
                }
                is TransactionStatusUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Gagal Memuat Status",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Button(
                            onClick = { transactionViewModel.getTransactionStatus(orderId) }
                        ) {
                            Icon(Icons.Default.Refresh, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Coba Lagi")
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedButton(
                            onClick = { 
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        ) {
                            Text("Kembali ke Beranda")
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
