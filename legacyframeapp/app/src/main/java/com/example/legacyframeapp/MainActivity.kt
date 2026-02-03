package com.example.legacyframeapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.legacyframeapp.navegation.AppNavGraph
import com.example.legacyframeapp.ui.theme.UINavegacionTheme
import com.example.legacyframeapp.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create the notification channels for purchases.
        createNotificationChannel(this)

        val appContainer = (application as LegacyFrameApplication).container

        setContent {
            AppRoot(appContainer.authViewModelFactory)
        }
    }
}

@Composable
fun AppRoot(authViewModelFactory: com.example.legacyframeapp.ui.viewmodel.AuthViewModelFactory) {
    // Create the ViewModel using the factory.
    val authViewModel: AuthViewModel = viewModel(factory = authViewModelFactory)

    // Configure navigation and theme.
    val navController = rememberNavController()

    // Observe preference state for dynamic theming.
    val darkMode by authViewModel.darkMode.collectAsStateWithLifecycle()
    val themeMode by authViewModel.themeMode.collectAsStateWithLifecycle()
    val accentHex by authViewModel.accentColor.collectAsStateWithLifecycle()
    val fontScale by authViewModel.fontScale.collectAsStateWithLifecycle()

    // Resolve whether to use the dark or light theme based on settings.
    val resolvedDark = when (themeMode) {
        "light" -> false
        "dark" -> true
        else -> darkMode // "system" uses the system setting (darkMode)
    }

    UINavegacionTheme(
        darkTheme = resolvedDark,
        accentHex = accentHex,
        fontScale = fontScale
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {

            // Previously removed the call that triggered an error.
            // It is no longer necessary to call prefetchProductImages.

            // Start the navigation graph.
            AppNavGraph(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}

// Helper function to create notification channels (Android 8.0+).
private fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        run {
            val channelId = "purchase_notifications"
            val name = "Notificaciones de Compra"
            val descriptionText = "Canal para notificar compras exitosas."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        run {
            val channelId = "support_notifications"
            val name = "Notificaciones de Soporte"
            val descriptionText = "Canal para notificar mensajes enviados a soporte."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
