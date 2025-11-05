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
import com.example.jamuin.ui.viewmodel.ExpertSystemUiState
import com.example.jamuin.ui.viewmodel.ExpertSystemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpertSystemScreen(
    navController: NavController,
    viewModel: ExpertSystemViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val answeredQuestions by viewModel.answeredQuestions.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Konsultasi AI") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
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
            when (val state = uiState) {
                is ExpertSystemUiState.Initial -> {
                    InitialScreen(
                        onStartClick = { viewModel.initializeSystem() }
                    )
                }
                is ExpertSystemUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is ExpertSystemUiState.Question -> {
                    QuestionScreen(
                        question = state.question,
                        sessionId = state.sessionId,
                        answeredCount = answeredQuestions.size,
                        onAnswerSelected = { questionId, optionId, questionText, answerText ->
                            viewModel.submitAnswer(
                                questionId,
                                optionId,
                                state.sessionId,
                                questionText,
                                answerText
                            )
                        }
                    )
                }
                is ExpertSystemUiState.Recommendation -> {
                    RecommendationScreen(
                        recommendation = state.recommendation,
                        answeredQuestions = answeredQuestions,
                        onViewProduct = { productId ->
                            if (productId > 0) {
                                navController.navigate(Screen.ProductDetail.createRoute(productId))
                            }
                        },
                        onStartOver = {
                            viewModel.reset()
                        }
                    )
                }
                is ExpertSystemUiState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.initializeSystem() },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun InitialScreen(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Favorite, // Changed from Psychology to LocalHospital
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Konsultasi AI Jamu",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Sistem AI akan menanyakan beberapa pertanyaan tentang kondisi kesehatan Anda untuk memberikan rekomendasi jamu yang tepat.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Rekomendasi Personal")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Berdasarkan Sistem Pakar")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Cepat dan Mudah")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text("Mulai Konsultasi", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun QuestionScreen(
    question: com.example.jamuin.data.model.Question,
    sessionId: String,
    answeredCount: Int,
    onAnswerSelected: (String, String, String, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress indicator
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, null) // Changed from Quiz to HelpOutline
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Pertanyaan ${answeredCount + 1}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Question
        Text(
            question.text,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Options
        question.options.forEach { option ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAnswerSelected(
                        question.id,
                        option.id,
                        question.text,
                        option.text
                    )
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        option.text,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.ArrowForward, null) // Changed from ChevronRight to ArrowForward
                }
            }
        }
    }
}

@Composable
fun RecommendationScreen(
    recommendation: com.example.jamuin.data.model.Recommendation,
    answeredQuestions: List<Pair<String, String>>,
    onViewProduct: (Int) -> Unit,
    onStartOver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (recommendation.productId > 0)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            ),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    if (recommendation.productId > 0) Icons.Default.CheckCircle else Icons.Default.Close, // Changed from Cancel to Close
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = if (recommendation.productId > 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    if (recommendation.productId > 0) "Rekomendasi Ditemukan!" else "Tidak Ada Rekomendasi",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Product Name
            if (recommendation.productId > 0) {
                Text(
                    recommendation.productName,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            // Recommendation Text
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        recommendation.alasan,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            // Summary of answers
            if (answeredQuestions.isNotEmpty()) {
                HorizontalDivider() // Changed from Divider to HorizontalDivider
                
                Text(
                    "Ringkasan Konsultasi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                answeredQuestions.forEachIndexed { index, (question, answer) ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                "${index + 1}. $question",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "➜ $answer",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Actions
            if (recommendation.productId > 0) {
                Button(
                    onClick = { onViewProduct(recommendation.productId) },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Icon(Icons.Default.Search, null) // Changed from Visibility to Search
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lihat Produk", style = MaterialTheme.typography.titleMedium)
                }
            }
            
            OutlinedButton(
                onClick = onStartOver,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                Icon(Icons.Default.Refresh, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Konsultasi Lagi", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Warning, // Changed from Error to Warning
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Terjadi Kesalahan",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Refresh, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Coba Lagi")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kembali")
        }
    }
}