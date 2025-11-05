# 📁 Project Structure Documentation

## Struktur Folder Lengkap

```
vending-machine-mobile/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/jamuin/
│   │   │   │   ├── data/
│   │   │   │   │   ├── api/
│   │   │   │   │   │   ├── ApiService.kt
│   │   │   │   │   │   └── RetrofitInstance.kt
│   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── ApiError.kt
│   │   │   │   │   │   ├── CartItem.kt
│   │   │   │   │   │   ├── ExpertSystem.kt
│   │   │   │   │   │   ├── Product.kt
│   │   │   │   │   │   └── Transaction.kt
│   │   │   │   │   └── repository/
│   │   │   │   │       ├── ExpertSystemRepository.kt
│   │   │   │   │       ├── ProductRepository.kt
│   │   │   │   │       └── TransactionRepository.kt
│   │   │   │   ├── navigation/
│   │   │   │   │   ├── AppNavigation.kt
│   │   │   │   │   └── Screen.kt
│   │   │   │   ├── ui/
│   │   │   │   │   ├── screen/
│   │   │   │   │   │   ├── CartScreen.kt
│   │   │   │   │   │   ├── CheckoutScreen.kt
│   │   │   │   │   │   ├── ExpertSystemScreen.kt
│   │   │   │   │   │   ├── HomeScreen.kt
│   │   │   │   │   │   ├── ProductDetailScreen.kt
│   │   │   │   │   │   ├── ProductListScreen.kt
│   │   │   │   │   │   └── TransactionStatusScreen.kt
│   │   │   │   │   ├── theme/
│   │   │   │   │   │   ├── Color.kt
│   │   │   │   │   │   ├── Theme.kt
│   │   │   │   │   │   └── Type.kt
│   │   │   │   │   └── viewmodel/
│   │   │   │   │       ├── CartViewModel.kt
│   │   │   │   │       ├── ExpertSystemViewModel.kt
│   │   │   │   │       ├── ProductViewModel.kt
│   │   │   │   │       └── TransactionViewModel.kt
│   │   │   │   ├── util/
│   │   │   │   │   └── CurrencyFormatter.kt
│   │   │   │   ├── MainActivity.kt
│   │   │   │   └── MainActivityNew.kt (sementara, akan dihapus)
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── mipmap-*/
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── xml/
│   │   │   └── AndroidManifest.xml
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── settings.gradle.kts
├── README.md
└── SETUP_GUIDE.md
```

## 📂 Penjelasan Folder

### `/app/src/main/java/com/example/jamuin/`

Root package aplikasi.

### `/data/`

Layer data yang menangani sumber data.

#### `/data/api/`

- **ApiService.kt**: Interface Retrofit yang mendefinisikan semua endpoint API
- **RetrofitInstance.kt**: Singleton instance Retrofit dengan konfigurasi OkHttp

#### `/data/model/`

Data classes untuk representasi data:

- **ApiError.kt**: Model untuk error response dari API
- **CartItem.kt**: Model untuk item di keranjang
- **ExpertSystem.kt**: Models untuk AI consultation (Question, Answer, Recommendation, dll)
- **Product.kt**: Model produk jamu
- **Transaction.kt**: Models untuk transaksi dan pembayaran

#### `/data/repository/`

Repository pattern untuk abstraksi data source:

- **ExpertSystemRepository.kt**: Handle API calls untuk expert system
- **ProductRepository.kt**: Handle API calls untuk products
- **TransactionRepository.kt**: Handle API calls untuk transactions

### `/navigation/`

Komponen navigation app.

- **AppNavigation.kt**: Setup NavHost dan routing
- **Screen.kt**: Sealed class untuk route definitions

### `/ui/`

Layer UI dengan Jetpack Compose.

#### `/ui/screen/`

Composable screens:

- **HomeScreen.kt**: Landing page dengan menu utama
- **ProductListScreen.kt**: Daftar semua produk
- **ProductDetailScreen.kt**: Detail produk dengan quantity selector
- **ExpertSystemScreen.kt**: AI consultation flow (Q&A dan rekomendasi)
- **CartScreen.kt**: Keranjang belanja
- **CheckoutScreen.kt**: Form checkout dan WebView payment
- **TransactionStatusScreen.kt**: Status pembayaran

#### `/ui/theme/`

Material Design 3 theming:

- **Color.kt**: Color palette
- **Theme.kt**: App theme configuration
- **Type.kt**: Typography definitions

#### `/ui/viewmodel/`

ViewModels untuk state management:

- **ProductViewModel.kt**: Manage product list state
- **CartViewModel.kt**: Manage cart state
- **ExpertSystemViewModel.kt**: Manage consultation flow
- **TransactionViewModel.kt**: Manage transaction & payment

### `/util/`

Utility classes dan helper functions.

- **CurrencyFormatter.kt**: Format currency ke Rupiah

### Root Files

- **MainActivity.kt**: Entry point aplikasi
- **MainActivityNew.kt**: File temporary (akan dihapus setelah replace MainActivity.kt)

## 🏗 Architecture Pattern

### MVVM (Model-View-ViewModel)

```
View (Composable) ←→ ViewModel ←→ Repository ←→ API Service
                        ↓
                    StateFlow
```

#### Flow Data:

1. **View** (Screen) observe StateFlow dari ViewModel
2. **ViewModel** handle business logic dan state management
3. **Repository** abstraction layer untuk data source
4. **API Service** handle network calls

### State Management

Menggunakan **StateFlow** untuk reactive state:

```kotlin
// ViewModel
private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// Screen
val uiState by viewModel.uiState.collectAsState()
```

### Navigation

Single activity dengan Jetpack Navigation Compose:

```kotlin
NavHost(navController, startDestination) {
    composable(route) { Screen() }
}
```

## 📦 Dependencies Management

### libs.versions.toml

Centralized dependency management menggunakan version catalog:

```toml
[versions]
retrofit = "2.9.0"
coil = "2.5.0"

[libraries]
retrofit = { group = "...", name = "...", version.ref = "retrofit" }
```

### build.gradle.kts

```kotlin
dependencies {
    implementation(libs.retrofit)
    implementation(libs.coil.compose)
}
```

## 🎨 UI Components

### Reusable Components

#### Cards

```kotlin
Card(modifier, colors, onClick) { Content }
```

#### Buttons

- `Button` - Primary actions
- `OutlinedButton` - Secondary actions
- `FilledIconButton` - Icon actions
- `IconButton` - Toolbar actions

#### Lists

```kotlin
LazyColumn {
    items(list) { item -> ItemCard(item) }
}
```

#### Dialogs

```kotlin
AlertDialog(
    onDismissRequest,
    title,
    text,
    confirmButton,
    dismissButton
)
```

## 🔄 Data Flow Examples

### Fetch Products

```
ProductListScreen
    ↓ (collectAsState)
ProductViewModel.uiState
    ↓ (viewModelScope.launch)
ProductRepository.getAllProducts()
    ↓ (suspend function)
ApiService.getAllProducts()
    ↓ (Retrofit)
Backend API: GET /products
    ↓ (Response)
List<Product>
    ↓ (Success)
ProductUiState.Success(products)
    ↓ (StateFlow emit)
UI Update
```

### Add to Cart

```
ProductDetailScreen
    ↓ (Button click)
CartViewModel.addToCart(product, quantity)
    ↓ (MutableStateFlow update)
_cartItems.value = updated list
    ↓ (StateFlow emit)
CartScreen observes change
    ↓
UI Update (badge, list)
```

### Create Transaction

```
CheckoutScreen
    ↓ (Form submit)
TransactionViewModel.createTransaction(...)
    ↓
TransactionRepository.createTransaction(request)
    ↓
ApiService.createTransaction(request)
    ↓ (POST /payments/create)
Backend creates Midtrans transaction
    ↓ (Response)
CreateTransactionResponse(snapUrl, token, ...)
    ↓
TransactionUiState.Success
    ↓
Show PaymentWebView with snapUrl
```

## 🔐 Security Considerations

### Network Security

1. **HTTPS**: Production harus gunakan HTTPS
2. **Certificate Pinning**: Optional untuk extra security
3. **API Key**: Jangan hardcode di code, gunakan BuildConfig

### Data Security

1. **Sensitive Data**: Jangan simpan payment info di local
2. **Validation**: Validate semua user input
3. **ProGuard**: Obfuscate code untuk release build

### Permission Minimalis

Hanya request permission yang benar-benar dibutuhkan:

- INTERNET
- ACCESS_NETWORK_STATE

## 📊 Performance Optimization

### Image Loading

Coil dengan caching:

```kotlin
AsyncImage(
    model = imageUrl,
    contentDescription = null,
    error = { PlaceholderIcon }
)
```

### Lazy Loading

LazyColumn untuk efficient list rendering:

```kotlin
LazyColumn {
    items(products, key = { it.id }) { product ->
        ProductCard(product)
    }
}
```

### State Hoisting

Minimize recomposition dengan state hoisting:

```kotlin
@Composable
fun Screen(viewModel: VM = viewModel()) {
    val state by viewModel.state.collectAsState()
    ScreenContent(state, viewModel::onAction)
}
```

## 🧪 Testing Strategy

### Unit Tests

- ViewModel logic
- Repository functions
- Utility functions

### Integration Tests

- API calls dengan MockWebServer
- Navigation flow
- State transitions

### UI Tests

- Compose UI testing
- User interaction flows
- Screen navigation

## 📈 Monitoring & Analytics

### Logging

- Network logs via OkHttp Interceptor
- ViewModel state changes
- Error tracking

### Crash Reporting

Future: Firebase Crashlytics

### Analytics

Future: Firebase Analytics

## 🔄 CI/CD Pipeline

Future implementation:

1. GitHub Actions / GitLab CI
2. Automated builds
3. Unit test execution
4. APK generation
5. Play Store deployment

## 📝 Code Style Guide

### Kotlin Conventions

- Use camelCase for variables
- Use PascalCase for classes
- Use descriptive names
- Add documentation for public APIs

### Compose Best Practices

- Use remember for state
- Hoist state when needed
- Keep composables pure
- Use LaunchedEffect for side effects

### Package Structure

- Group by feature (not layer)
- Keep related code together
- Minimize dependencies

---

**Last Updated**: November 5, 2025
