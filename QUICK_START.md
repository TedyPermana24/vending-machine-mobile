# 🚀 Quick Start Guide - Jamuin Mobile App

## ⚡ Ringkasan Super Cepat

### 1️⃣ Update MainActivity (WAJIB!)

Ganti konten `MainActivity.kt` dengan konten dari `MainActivityNew.kt`, atau hapus dan rename file tersebut.

### 2️⃣ Set Backend URL

Edit `app/src/main/java/com/example/jamuin/data/api/RetrofitInstance.kt`:

```kotlin
// Emulator
private const val BASE_URL = "http://10.0.2.2:3000/"

// Device fisik (ganti dengan IP komputer Anda)
// private const val BASE_URL = "http://192.168.1.XXX:3000/"
```

### 3️⃣ Run Backend

```bash
cd backend-folder
npm run dev
```

### 4️⃣ Sync & Run

1. Android Studio → Sync Project
2. Run App (Shift + F10)

---

## 📱 Fitur Aplikasi

### 🏠 Home Screen

- Menu utama aplikasi
- Navigasi ke AI Consultation dan Products
- Lihat jumlah item di cart (badge)

### 🤖 AI Consultation

1. Klik "Konsultasi AI"
2. Jawab pertanyaan tentang keluhan
3. Dapatkan rekomendasi produk
4. Langsung checkout produk yang direkomendasikan

### 🛍️ Products

1. Browse katalog produk
2. Klik produk untuk detail
3. Atur jumlah pembelian
4. Tambah ke keranjang

### 🛒 Shopping Cart

1. Lihat semua item di keranjang
2. Ubah quantity atau hapus item
3. Klik Checkout untuk lanjut pembayaran

### 💳 Checkout & Payment

1. Isi form pembeli (nama, email, phone)
2. Klik "Bayar Sekarang"
3. Pilih metode pembayaran di Midtrans
4. Selesaikan pembayaran
5. Lihat status transaksi

---

## 🔧 Konfigurasi Penting

### Network Configuration

File: `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
<application android:usesCleartextTraffic="true">
```

### API Endpoints

File: `data/api/ApiService.kt`

```kotlin
// Products
GET /products
GET /products/{id}

// Expert System
POST /expert-system/initialize
GET /expert-system/start
POST /expert-system/diagnose

// Transactions
POST /payments/create
GET /payments/status/{orderId}
```

### Dependencies

File: `gradle/libs.versions.toml` & `app/build.gradle.kts`

Sudah include:

- ✅ Retrofit (Network)
- ✅ OkHttp (Logging)
- ✅ Coil (Images)
- ✅ Navigation Compose
- ✅ Material 3
- ✅ ViewModel

---

## 🎯 Testing Checklist

### ✅ Test Connection

- [ ] Buka app
- [ ] Klik "Menu Produk"
- [ ] Produk muncul? → API OK ✓

### ✅ Test AI Consultation

- [ ] Klik "Konsultasi AI"
- [ ] Jawab pertanyaan
- [ ] Rekomendasi muncul? → Expert System OK ✓

### ✅ Test Add to Cart

- [ ] Pilih produk
- [ ] Klik "Tambah ke Keranjang"
- [ ] Badge cart bertambah? → Cart OK ✓

### ✅ Test Payment (Sandbox)

- [ ] Checkout dari cart
- [ ] Isi form customer
- [ ] WebView Midtrans muncul? → Payment Integration OK ✓
- [ ] Pilih payment method
- [ ] Status transaksi muncul? → Complete Flow OK ✓

---

## 🐛 Quick Troubleshooting

### ❌ "Failed to connect to API"

**Cek:**

```powershell
# Test backend dari browser/postman
http://localhost:3000/products

# Cek IP untuk device fisik
ipconfig
```

**Fix:**

- Backend running?
- BASE_URL benar?
- Firewall allow port 3000?

### ❌ Images tidak muncul

**Fix:**

- Check internet permission di Manifest
- Coil akan show placeholder icon

### ❌ Build error

**Fix:**

```
File → Invalidate Caches / Restart
Build → Clean Project
Build → Rebuild Project
```

### ❌ App crash

**Fix:**

- Check Logcat untuk error message
- Pastikan semua dependencies ter-sync
- Pastikan MainActivity sudah diupdate

---

## 📂 File Locations Quick Reference

```
Important files:

🔧 Configuration
- RetrofitInstance.kt       → BASE_URL
- AndroidManifest.xml       → Permissions
- build.gradle.kts          → Dependencies

📱 Screens
- HomeScreen.kt             → Landing page
- ProductListScreen.kt      → Product catalog
- ExpertSystemScreen.kt     → AI consultation
- CartScreen.kt             → Shopping cart
- CheckoutScreen.kt         → Payment
- TransactionStatusScreen.kt → Status

🎨 ViewModels
- ProductViewModel.kt       → Products state
- CartViewModel.kt          → Cart state
- ExpertSystemViewModel.kt  → AI state
- TransactionViewModel.kt   → Payment state

🔄 Navigation
- AppNavigation.kt          → Routes setup
- Screen.kt                 → Route definitions

📦 Data
- ApiService.kt             → API endpoints
- Product.kt                → Product model
- Transaction.kt            → Transaction models
- ExpertSystem.kt           → AI models
```

---

## 💡 Development Tips

### Hot Reload

Compose mendukung hot reload! Edit UI langsung terlihat.

### Preview

Gunakan `@Preview` untuk preview composable:

```kotlin
@Preview
@Composable
fun ScreenPreview() {
    JamuinTheme {
        MyScreen()
    }
}
```

### Logging

Check Logcat dengan filter:

- `OkHttp` → Network logs
- `System.out` → Custom logs

### State Debugging

Tambah log di ViewModel:

```kotlin
_uiState.value = NewState
Log.d("ViewModel", "State changed to $NewState")
```

---

## 🎓 Learning Resources

### Jetpack Compose

- [Compose Basics](https://developer.android.com/jetpack/compose/tutorial)
- [State Management](https://developer.android.com/jetpack/compose/state)
- [Navigation](https://developer.android.com/jetpack/compose/navigation)

### MVVM Architecture

- [Guide to app architecture](https://developer.android.com/topic/architecture)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [Repository pattern](https://developer.android.com/topic/architecture/data-layer)

### Networking

- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [Coil](https://coil-kt.github.io/coil/)

---

## 📞 Quick Commands

### Clean Build

```bash
./gradlew clean
./gradlew build
```

### Generate APK

```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Check Dependencies

```bash
./gradlew app:dependencies
```

### Lint Check

```bash
./gradlew lint
```

---

## 🎉 You're Ready!

App sudah lengkap dengan:

- ✅ AI Consultation dengan Expert System
- ✅ Product catalog dan detail
- ✅ Shopping cart functionality
- ✅ Midtrans payment integration
- ✅ Transaction status tracking
- ✅ Material Design 3 UI
- ✅ MVVM Architecture
- ✅ Complete navigation flow

**Selamat coding! 🚀**

---

**Need Help?**

- Baca: `README.md` untuk detail lengkap
- Baca: `SETUP_GUIDE.md` untuk setup detail
- Baca: `PROJECT_STRUCTURE.md` untuk architecture
- Check: Logcat untuk debugging

**Version**: 1.0.0  
**Last Updated**: November 5, 2025
