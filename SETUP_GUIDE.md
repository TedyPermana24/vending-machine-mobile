# Panduan Konfigurasi & Setup

## 📝 Langkah-Langkah Setup

### 1. Update MainActivity.kt

File `MainActivity.kt` yang lama perlu diganti dengan versi baru. Silakan:

**Opsi A: Manual**

1. Buka file `app/src/main/java/com/example/jamuin/MainActivity.kt`
2. Hapus semua isinya
3. Copy semua konten dari `app/src/main/java/com/example/jamuin/MainActivityNew.kt`
4. Paste ke `MainActivity.kt`
5. Hapus file `MainActivityNew.kt`

**Opsi B: Via PowerShell**

```powershell
cd "d:\TEDY\vending-machine\vending-machine-mobile"
Copy-Item -Path "app\src\main\java\com\example\jamuin\MainActivityNew.kt" -Destination "app\src\main\java\com\example\jamuin\MainActivity.kt" -Force
Remove-Item "app\src\main\java\com\example\jamuin\MainActivityNew.kt" -Force
```

### 2. Konfigurasi Backend URL

Edit file `app/src/main/java/com/example/jamuin/data/api/RetrofitInstance.kt`:

**Untuk Android Emulator:**

```kotlin
private const val BASE_URL = "http://10.0.2.2:3000/"
```

**Untuk Device Fisik:**

1. Cari IP Address komputer Anda:
   ```powershell
   ipconfig
   ```
2. Gunakan IP dari "IPv4 Address":
   ```kotlin
   private const val BASE_URL = "http://192.168.1.XXX:3000/"
   ```

**Untuk Production:**

```kotlin
private const val BASE_URL = "https://your-api-domain.com/"
```

### 3. Pastikan Backend Running

Sebelum menjalankan app, pastikan backend API sudah running:

```bash
# Di folder backend
npm run start
# atau
npm run dev
```

Backend harus accessible di:

- `http://localhost:3000` (dari komputer)
- `http://10.0.2.2:3000` (dari emulator)
- `http://<IP_ADDRESS>:3000` (dari device fisik)

### 4. Sync Gradle Dependencies

1. Buka project di Android Studio
2. Klik **File** → **Sync Project with Gradle Files**
3. Tunggu hingga selesai download dependencies

### 5. Setup Emulator atau Device

**Emulator:**

1. Buka AVD Manager
2. Create Virtual Device (API 24+)
3. Start emulator

**Device Fisik:**

1. Enable Developer Options
2. Enable USB Debugging
3. Connect via USB
4. Pastikan device dan komputer di network yang sama (untuk API access)

### 6. Run Application

1. Klik **Run** → **Run 'app'** (Shift + F10)
2. Pilih device/emulator
3. Tunggu build selesai

## 🔧 Konfigurasi Tambahan

### Mengganti Base URL saat Runtime (Optional)

Untuk development yang lebih fleksibel, Anda bisa membuat build variants:

Edit `app/build.gradle.kts`:

```kotlin
android {
    // ... existing config

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:3000/\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://api.production.com/\"")
            // ... other release configs
        }
    }

    buildFeatures {
        buildConfig = true
    }
}
```

Kemudian di `RetrofitInstance.kt`:

```kotlin
private const val BASE_URL = BuildConfig.BASE_URL
```

### Network Security Configuration (Optional)

Untuk mengizinkan cleartext traffic ke localhost, file sudah dikonfigurasi dengan:

```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

Untuk production, sebaiknya dihapus dan gunakan HTTPS.

## 🧪 Testing

### Test API Connection

1. Buka app
2. Navigasi ke "Menu Produk"
3. Jika data produk muncul, API connection berhasil
4. Check Logcat untuk network logs

### Test Expert System

1. Klik "Konsultasi AI"
2. Jawab beberapa pertanyaan
3. Pastikan rekomendasi muncul

### Test Checkout & Payment

⚠️ **PENTING**: Gunakan Midtrans Sandbox untuk testing!

1. Tambah produk ke cart
2. Checkout
3. Isi form customer
4. Klik "Bayar Sekarang"
5. Pilih payment method di Midtrans page
6. Gunakan test credentials dari [Midtrans Sandbox](https://docs.midtrans.com/docs/testing-payment-on-sandbox)

## 📊 Monitoring & Debugging

### Enable Network Logging

Network logging sudah enabled by default via OkHttp interceptor.

View logs di Logcat dengan filter:

```
OkHttp
```

### Check API Responses

Di Logcat, filter dengan:

```
System.out
```

### Debug ViewModel States

Tambahkan log di ViewModel:

```kotlin
viewModelScope.launch {
    _uiState.value = ProductUiState.Loading
    Log.d("ProductViewModel", "Loading products...")
    // ...
}
```

## 🚨 Troubleshooting

### Build Error: "Cannot resolve symbol"

**Solusi:**

1. File → Invalidate Caches / Restart
2. Clean Project (Build → Clean Project)
3. Rebuild Project (Build → Rebuild Project)

### Network Error: "Failed to connect"

**Cek:**

1. Backend running? → `http://localhost:3000/products`
2. BASE_URL benar?
3. Emulator bisa akses internet?
4. Firewall tidak block port 3000?

### Images Not Loading

**Cek:**

1. Internet permission ada di Manifest?
2. Image URL dari backend benar?
3. Coil dependency sudah di-sync?

### WebView Payment Tidak Muncul

**Cek:**

1. JavaScript enabled di WebView?
2. Midtrans credentials benar di backend?
3. Transaction dibuat dengan sukses?

### App Crash on Startup

**Cek Logcat untuk:**

1. Network exceptions
2. Serialization errors
3. Missing dependencies

## 📦 Build APK untuk Testing

### Debug APK

```bash
./gradlew assembleDebug
```

APK location:

```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (Optional)

1. Generate signing key
2. Configure signing in `build.gradle.kts`
3. Build:

```bash
./gradlew assembleRelease
```

## 🔒 Security Notes

### Production Checklist

- [ ] Ganti BASE_URL ke production URL
- [ ] Hapus `usesCleartextTraffic="true"`
- [ ] Gunakan HTTPS untuk semua API calls
- [ ] Implement ProGuard/R8 obfuscation
- [ ] Remove atau disable logging interceptor
- [ ] Implement proper error handling
- [ ] Add authentication/authorization
- [ ] Validate all user inputs
- [ ] Implement certificate pinning (optional)

## 📱 Testing on Different Devices

### Minimum Requirements

- Android 7.0 (API 24) or higher
- Internet connection
- ~50 MB storage space

### Recommended

- Android 10+ (API 29+)
- 4G/WiFi connection
- Screen size: 5" or larger

## 🎯 Next Development Steps

1. **Authentication**

   - Implement login/register
   - JWT token management
   - Secure storage

2. **Order History**

   - List past orders
   - Order details
   - Reorder functionality

3. **Offline Support**

   - Room Database
   - Sync mechanism
   - Cache product data

4. **Push Notifications**

   - FCM integration
   - Order status updates
   - Promotional notifications

5. **Analytics**
   - Firebase Analytics
   - Crashlytics
   - User behavior tracking

## 📚 Resources

- [Android Developers](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit](https://square.github.io/retrofit/)
- [Midtrans Documentation](https://docs.midtrans.com/)
- [Material Design 3](https://m3.material.io/)

## 💡 Tips

1. **Development**

   - Gunakan emulator dengan API 30+ untuk best experience
   - Enable hot reload untuk faster development
   - Use Android Studio preview untuk UI development

2. **Testing**

   - Test dengan berbagai screen sizes
   - Test dengan koneksi lambat
   - Test error scenarios

3. **Performance**
   - Monitor memory usage
   - Optimize image loading
   - Implement pagination untuk product list

---

Jika ada pertanyaan atau masalah, silakan cek dokumentasi atau hubungi tim development.

**Happy Coding! 🚀**
