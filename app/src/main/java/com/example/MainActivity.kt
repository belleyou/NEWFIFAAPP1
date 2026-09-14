package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.GlobeScreen
import com.example.ui.theme.MyApplicationTheme
import java.io.File

class MainActivity : ComponentActivity() {
  companion object {
    init {
      try {
        android.system.Os.setenv("LIBGL_ALWAYS_SOFTWARE", "1", true)
        android.system.Os.setenv("MESA_LOADER_DRIVER_OVERRIDE", "swrast", true)
        android.system.Os.setenv("GALLIUM_DRIVER", "softpipe", true)
        android.system.Os.setenv("LIBGL_DRI3_DISABLE", "1", true)
        android.system.Os.setenv("MESA_DEBUG", "0", true)
        android.system.Os.setenv("MESA_LOG_LEVEL", "silent", true)
      } catch (e: Throwable) {
        // Safe fallback if OS environment modification is restricted
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    try {
      android.system.Os.setenv("LIBGL_ALWAYS_SOFTWARE", "1", true)
      android.system.Os.setenv("MESA_LOADER_DRIVER_OVERRIDE", "swrast", true)
      android.system.Os.setenv("GALLIUM_DRIVER", "softpipe", true)
      android.system.Os.setenv("LIBGL_DRI3_DISABLE", "1", true)
      android.system.Os.setenv("MESA_DEBUG", "0", true)
      android.system.Os.setenv("MESA_LOG_LEVEL", "silent", true)
    } catch (e: Throwable) {
      // Safe fallback
    }
    
    // Pre-create WebView WebAssembly cache directory to prevent internal Chromium warning log
    try {
      val wasmCacheDir = File(cacheDir, "WebView/Default/HTTP Cache/Code Cache/wasm")
      if (!wasmCacheDir.exists()) {
        wasmCacheDir.mkdirs()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          GlobeScreen()
        }
      }
    }
  }
}
