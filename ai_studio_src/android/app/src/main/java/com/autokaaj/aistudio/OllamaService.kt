package com.autokaaj.aistudio

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.io.File

/**
 * Runtime launcher scaffold. A compatible Android/arm64 Ollama executable is NOT included.
 * Add a tested Android-compatible executable at app/src/main/assets/ollama/ollama before
 * expecting this service to launch a server. The normal Linux Ollama binary is not a drop-in.
 */
class OllamaService : Service() {
    private var process: Process? = null
    private val channelId = "ollama_runtime"

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(1701, notification("Ollama runtime not bundled"))
        Thread { startBundledRuntimeIfPresent() }.start()
    }

    private fun startBundledRuntimeIfPresent() {
        try {
            val assetPath = "ollama/ollama"
            val files = assets.list("ollama") ?: emptyArray()
            if (!files.contains("ollama")) {
                updateNotification("Runtime missing: no Android Ollama binary packaged")
                return
            }
            val binDir = File(filesDir, "bin").apply { mkdirs() }
            val executable = File(binDir, "ollama")
            assets.open(assetPath).use { input -> executable.outputStream().use { output -> input.copyTo(output) } }
            executable.setExecutable(true, true)
            process = ProcessBuilder(executable.absolutePath, "serve")
                .directory(filesDir)
                .redirectErrorStream(true)
                .start()
            updateNotification("Ollama process started; verify /api/tags")
            Thread {
                try { process?.inputStream?.bufferedReader()?.useLines { lines -> lines.forEach { android.util.Log.i("OllamaService", it) } } }
                catch (e: Exception) { android.util.Log.e("OllamaService", "Log reader failed", e) }
            }.start()
        } catch (e: Exception) {
            android.util.Log.e("OllamaService", "Could not start runtime", e)
            updateNotification("Ollama failed to start: ${e.javaClass.simpleName}")
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel(channelId, "Ollama runtime", NotificationManager.IMPORTANCE_LOW))
        }
    }

    private fun notification(message: String): Notification = NotificationCompat.Builder(this, channelId)
        .setContentTitle("AI Studio Pro")
        .setContentText(message)
        .setSmallIcon(android.R.drawable.stat_sys_download_done)
        .setOngoing(true)
        .build()

    private fun updateNotification(message: String) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(1701, notification(message))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onDestroy() { process?.destroy(); super.onDestroy() }
    override fun onBind(intent: Intent?): IBinder? = null
}
