package com.example.legacyframeapp.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.legacyframeapp.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(vm: AuthViewModel, onBack: () -> Unit) {
    val session by vm.session.collectAsStateWithLifecycle()
    var name by remember(session.currentUser?.nombre) { mutableStateOf(session.currentUser?.nombre ?: "") }
    var email by remember(session.currentUser?.email) { mutableStateOf(session.currentUser?.email ?: "") }
    var message by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val charCount = remember(message) {
        message.count { !it.isWhitespace() }
    }
    val isCharCountValid = charCount in 10..300
    val charRuleText = "El mensaje debe tener entre 10 y 300 letras."

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Soporte") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Cuéntanos tu problema y te ayudaremos.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Mensaje") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 140.dp),
                minLines = 4
            )

            if (!message.isBlank() && !isCharCountValid) {
                Text(charRuleText, color = MaterialTheme.colorScheme.error)
            }

            if (errorMsg != null) {
                Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    if (!isCharCountValid) {
                        errorMsg = charRuleText
                        return@Button
                    }
                    isSending = true
                    errorMsg = null
                    vm.sendContactMessage(name.trim(), email.trim(), message.trim()) { result ->
                        isSending = false
                        if (result.isSuccess) {
                            Toast.makeText(context, "Mensaje enviado", Toast.LENGTH_SHORT).show()
                            message = ""
                        } else {
                            errorMsg = result.exceptionOrNull()?.message ?: "Error al enviar"
                        }
                    }
                },
                enabled = !isSending && name.isNotBlank() && email.isNotBlank() && message.isNotBlank() && isCharCountValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSending) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(Modifier.width(10.dp))
                }
                Text(if (isSending) "Enviando..." else "Enviar")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}