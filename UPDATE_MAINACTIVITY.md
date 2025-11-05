# ⚠️ IMPORTANT: Update MainActivity.kt

## 🚨 ACTION REQUIRED SEBELUM RUN APP

File `MainActivity.kt` yang lama masih berisi template default dan perlu diganti dengan versi baru.

---

## 📝 Cara Update MainActivity.kt

### Opsi 1: Copy-Paste Manual (RECOMMENDED)

1. **Buka file** `app/src/main/java/com/example/jamuin/MainActivity.kt`

2. **Hapus semua isinya** (Ctrl+A, Delete)

3. **Copy konten berikut dan paste ke MainActivity.kt:**

```kotlin
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
```

4. **Save file** (Ctrl+S)

5. **Hapus file** `MainActivityNew.kt` (tidak diperlukan lagi)

6. **Sync Project** (File → Sync Project with Gradle Files)

---

### Opsi 2: Via PowerShell/Terminal

Buka PowerShell di folder project dan jalankan:

```powershell
# Navigate to project root
cd "d:\TEDY\vending-machine\vending-machine-mobile"

# Copy MainActivityNew.kt ke MainActivity.kt
Copy-Item -Path "app\src\main\java\com\example\jamuin\MainActivityNew.kt" `
          -Destination "app\src\main\java\com\example\jamuin\MainActivity.kt" `
          -Force

# Delete MainActivityNew.kt
Remove-Item "app\src\main\java\com\example\jamuin\MainActivityNew.kt" -Force
```

Kemudian:

1. Refresh project di Android Studio
2. Sync Project with Gradle Files

---

### Opsi 3: Via Android Studio

1. **Buka kedua file:**

   - `MainActivity.kt`
   - `MainActivityNew.kt`

2. **Di MainActivityNew.kt:**

   - Pilih semua (Ctrl+A)
   - Copy (Ctrl+C)

3. **Di MainActivity.kt:**

   - Pilih semua (Ctrl+A)
   - Paste (Ctrl+V)
   - Save (Ctrl+S)

4. **Hapus MainActivityNew.kt:**

   - Right-click → Delete
   - Confirm

5. **Sync Project**

---

## ✅ Verification

Setelah update, pastikan MainActivity.kt berisi:

### Cek Import Statements:

```kotlin
import com.example.jamuin.navigation.AppNavigation
import com.example.jamuin.ui.viewmodel.CartViewModel
import com.example.jamuin.ui.viewmodel.ProductViewModel
```

### Cek onCreate:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    AppNavigation(
        navController = navController,
        cartViewModel = cartViewModel,
        productViewModel = productViewModel
    )
    // ...
}
```

### ❌ Tidak boleh ada:

```kotlin
// JANGAN ADA INI:
Greeting(name = "Android")
@Preview
fun GreetingPreview()
```

---

## 🐛 Troubleshooting

### Error: "Cannot resolve symbol 'AppNavigation'"

**Solusi:**

1. Pastikan file `AppNavigation.kt` ada
2. Sync Project with Gradle Files
3. File → Invalidate Caches / Restart

### Error: Build Failed

**Solusi:**

1. Build → Clean Project
2. Build → Rebuild Project
3. Sync Project

### Error: "Unresolved reference: viewModel"

**Solusi:**

- Check dependencies di `build.gradle.kts`:

```kotlin
implementation(libs.lifecycle.viewmodel.compose)
```

- Sync Gradle

---

## 📋 Post-Update Checklist

Setelah update MainActivity.kt:

- [ ] File `MainActivity.kt` updated
- [ ] File `MainActivityNew.kt` deleted
- [ ] Project synced
- [ ] No build errors
- [ ] Import statements correct
- [ ] AppNavigation called in onCreate

---

## ▶️ Ready to Run!

Setelah MainActivity.kt diupdate:

1. **Sync Project** ✅
2. **Build Project** ✅
3. **Run App** ✅

```
Click Run (▶️) atau tekan Shift + F10
```

---

## 🎯 Why This Update is Required?

**MainActivity.kt lama** berisi:

- Template default dari Android Studio
- Greeting composable (dummy)
- Preview functions (not needed)
- Tidak ada navigation setup

**MainActivity.kt baru** berisi:

- AppNavigation setup
- ViewModel initialization
- Navigation controller
- Proper theme application
- Ready untuk semua screens

---

## 📞 Still Having Issues?

Jika masih ada masalah setelah update:

1. **Check Logcat** untuk error messages
2. **Verify** semua files ada (AppNavigation.kt, ViewModels, dll)
3. **Clean & Rebuild** project
4. **Restart** Android Studio
5. **Check** SETUP_GUIDE.md untuk troubleshooting lengkap

---

**⚠️ PENTING**: Tanpa update MainActivity.kt, app akan crash atau menampilkan screen kosong!

**Status After Update**: ✅ Ready to Run
