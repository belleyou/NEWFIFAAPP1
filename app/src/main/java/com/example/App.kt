package com.example

import android.app.Application

/**
 * Application class initialized at process startup.
 * Sets graphics environment configuration to enforce software rendering and
 * suppress Mesa DRI hardware rendernode probes in virtualized / containerized environments.
 */
class App : Application() {
    companion object {
        init {
            configureGraphicsEnvironment()
        }

        private fun configureGraphicsEnvironment() {
            try {
                android.system.Os.setenv("LIBGL_ALWAYS_SOFTWARE", "1", true)
                android.system.Os.setenv("MESA_LOADER_DRIVER_OVERRIDE", "swrast", true)
                android.system.Os.setenv("GALLIUM_DRIVER", "softpipe", true)
                android.system.Os.setenv("LIBGL_DRI3_DISABLE", "1", true)
                android.system.Os.setenv("MESA_DEBUG", "0", true)
                android.system.Os.setenv("MESA_LOG_LEVEL", "silent", true)
            } catch (_: Throwable) {}
        }
    }

    override fun onCreate() {
        super.onCreate()
        configureGraphicsEnvironment()
    }
}
