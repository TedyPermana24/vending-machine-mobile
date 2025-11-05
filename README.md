# Jamuin - Vending Machine Mobile App

Aplikasi mobile Android untuk sistem vending machine jamu tradisional Indonesia dengan fitur AI consultation, katalog produk, keranjang belanja, dan integrasi pembayaran Midtrans.

## 🌟 Fitur Utama

### 1. **AI Consultation (Expert System)**

- Sistem pakar berbasis Forward Chaining
- Rekomendasi produk personal berdasarkan keluhan kesehatan
- Interface Q&A yang user-friendly
- Ringkasan konsultasi

### 2. **Katalog Produk**

- Daftar produk jamu lengkap
- Detail produk dengan gambar, deskripsi, manfaat
- Informasi harga dan stok real-time
- Search dan filter produk

### 3. **Keranjang Belanja**

- Tambah/hapus produk ke keranjang
- Update quantity produk
- Kalkulasi total harga otomatis
- Persistent cart state

### 4. **Checkout & Pembayaran**

- Form data pembeli
- Integrasi Midtrans Payment Gateway
- Multiple payment methods (QRIS, Bank Transfer, E-Wallet, dll)
- WebView untuk payment page

### 5. **Status Transaksi**

- Real-time transaction status tracking
- Detail transaksi lengkap
- Informasi pembeli dan produk

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit2 + OkHttp3
- **Image Loading**: Coil
- **Navigation**: Jetpack Navigation Compose
- **State Management**: StateFlow & Compose State
- **Payment**: Midtrans SDK

## 📦 Dependencies

```kotlin
// Retrofit untuk REST API
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp untuk logging
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Coil untuk image loading
implementation("io.coil-kt:coil-compose:2.5.0")

// Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

// Gson
implementation("com.google.code.gson:gson:2.10.1")
```

## 🚀 Cara Setup

### 1. Clone Repository

```bash
git clone <repository-url>
cd vending-machine-mobile
```

### 2. Konfigurasi Backend URL

Edit file `RetrofitInstance.kt` dan sesuaikan BASE_URL:

```kotlin
// Untuk emulator Android
private const val BASE_URL = "http://10.0.2.2:3000/"

// Untuk device fisik, ganti dengan IP komputer Anda
// private const val BASE_URL = "http://192.168.1.XXX:3000/"
```

### 3. Setup Backend

Pastikan backend API sudah running di `http://localhost:3000`. Dokumentasi API dapat dilihat di API_DOCUMENTATION.md

### 4. Build & Run

Buka project di Android Studio dan:

1. Sync Gradle
2. Connect device atau start emulator
3. Run app (Shift + F10)

## 📱 Struktur Project

```
app/src/main/java/com/example/jamuin/
├── data/
│   ├── api/
│   │   ├── ApiService.kt          # API endpoints
│   │   └── RetrofitInstance.kt     # Retrofit configuration
│   ├── model/
│   │   ├── Product.kt              # Product data model
│   │   ├── Transaction.kt          # Transaction models
│   │   ├── ExpertSystem.kt         # Expert system models
│   │   ├── CartItem.kt             # Cart item model
│   │   └── ApiError.kt             # Error response model
│   └── repository/
│       ├── ProductRepository.kt
│       ├── TransactionRepository.kt
│       └── ExpertSystemRepository.kt
├── ui/
│   ├── screen/
│   │   ├── HomeScreen.kt
│   │   ├── ProductListScreen.kt
│   │   ├── ProductDetailScreen.kt
│   │   ├── ExpertSystemScreen.kt
│   │   ├── CartScreen.kt
│   │   ├── CheckoutScreen.kt
│   │   └── TransactionStatusScreen.kt
│   ├── viewmodel/
│   │   ├── ProductViewModel.kt
│   │   ├── CartViewModel.kt
│   │   ├── ExpertSystemViewModel.kt
│   │   └── TransactionViewModel.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── navigation/
│   ├── Screen.kt                   # Screen routes
│   └── AppNavigation.kt            # Navigation graph
├── util/
│   └── CurrencyFormatter.kt        # Utility functions
└── MainActivity.kt                 # Main entry point
```

## 🔧 Konfigurasi

### Android Manifest Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Minimum SDK Requirements

- Min SDK: 24 (Android 7.0)
- Target SDK: 36
- Compile SDK: 36

## 🎯 User Flow

### Flow 1: AI Consultation → Purchase

1. User membuka app
2. Pilih "Konsultasi AI"
3. Jawab pertanyaan tentang keluhan kesehatan
4. Sistem memberikan rekomendasi produk
5. User klik "Lihat Produk"
6. Tambahkan ke keranjang
7. Checkout dan bayar

### Flow 2: Direct Purchase

1. User membuka app
2. Pilih "Menu Produk"
3. Browse dan pilih produk
4. Lihat detail produk
5. Tambahkan ke keranjang
6. Checkout dan bayar

## 💳 Integrasi Midtrans

### Payment Flow

1. User mengisi form checkout
2. App membuat transaction via API
3. Menerima Snap Token dari backend
4. Load Snap URL di WebView
5. User memilih metode pembayaran
6. Selesaikan pembayaran
7. Redirect ke transaction status

### Supported Payment Methods

- QRIS
- Bank Transfer (BCA, BNI, Mandiri, dll)
- E-Wallet (GoPay, OVO, Dana, dll)
- Credit/Debit Card
- Convenience Store (Alfamart, Indomaret)

## 🔍 API Endpoints yang Digunakan

### Products

- `GET /products` - Get all products
- `GET /products/:id` - Get product by ID

### Expert System

- `POST /expert-system/initialize` - Initialize system
- `GET /expert-system/start` - Start diagnosis
- `POST /expert-system/diagnose` - Submit answer

### Transactions

- `POST /payments/create` - Create transaction
- `GET /payments/status/:orderId` - Get transaction status
- `GET /payments/transaction/:orderId` - Get transaction details

## 🎨 UI/UX Design

- **Design System**: Material Design 3
- **Color Scheme**: Dynamic color theming
- **Typography**: Roboto font family
- **Icons**: Material Icons
- **Layout**: Responsive design for various screen sizes

## 📝 State Management

### ViewModel States

#### ProductViewModel

```kotlin
sealed class ProductUiState {
    object Loading
    data class Success(val products: List<Product>)
    data class Error(val message: String)
}
```

#### CartViewModel

- Manages cart items
- Calculates totals
- Handles quantity updates

#### ExpertSystemViewModel

```kotlin
sealed class ExpertSystemUiState {
    object Initial
    object Loading
    data class Question(question, sessionId)
    data class Recommendation(recommendation, sessionId)
    data class Error(message)
}
```

#### TransactionViewModel

```kotlin
sealed class TransactionUiState {
    object Initial
    object Loading
    data class Success(response)
    data class Error(message)
}
```

## 🐛 Troubleshooting

### Connection Timeout

- Pastikan backend API running
- Check BASE_URL sudah benar
- Pastikan device/emulator bisa akses network

### Image Not Loading

- Check internet connection
- Verify image URL dari backend
- Coil akan show placeholder jika gagal load

### Payment WebView Issue

- Pastikan JavaScript enabled
- Check Midtrans credentials di backend
- Test dengan Sandbox mode dulu

## 📄 License

Project ini dibuat untuk keperluan edukasi dan pembelajaran.

## 👨‍💻 Developer Notes

### Important Files to Modify

1. **RetrofitInstance.kt** - Ganti BASE_URL sesuai environment
2. **ApiService.kt** - Tambah endpoint baru jika backend berubah
3. **Theme.kt** - Customize color scheme

### Tips Development

- Gunakan Logcat untuk debugging
- Enable network logging di OkHttp
- Test payment dengan Sandbox mode
- Implement proper error handling

## 🔄 Update MainActivity

Jangan lupa untuk mengganti konten `MainActivity.kt` dengan konten dari `MainActivityNew.kt`:

1. Hapus file `MainActivity.kt` yang lama
2. Rename `MainActivityNew.kt` menjadi `MainActivity.kt`
3. Atau copy konten dari `MainActivityNew.kt` ke `MainActivity.kt`

## 🚀 Next Steps

1. Implement authentication
2. Add product search functionality
3. Implement order history
4. Add push notifications
5. Offline mode dengan Room Database
6. Unit & Integration tests

## 📞 Support

Untuk bantuan atau pertanyaan, silakan hubungi tim development.

---

**Happy Coding! 🎉**
