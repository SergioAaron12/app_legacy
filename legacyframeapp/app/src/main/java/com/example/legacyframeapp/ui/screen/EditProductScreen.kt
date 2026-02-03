package com.example.legacyframeapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.legacyframeapp.domain.model.Product
import com.example.legacyframeapp.ui.components.AppLogoTitle
import com.example.legacyframeapp.ui.components.formatWithThousands
import com.example.legacyframeapp.ui.components.ThousandSeparatorTransformation
import com.example.legacyframeapp.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    vm: AuthViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        vm.refreshCatalog()
    }

    val products by vm.products.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val categories = listOf(
        2L to "Grecas",
        3L to "Rústicas",
        4L to "Nativas",
        5L to "Finger-Joint",
        6L to "Naturales"
    )

    var editing by remember { mutableStateOf<Product?>(null) }
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf(2L) }
    var expanded by remember { mutableStateOf(false) }

    val thousandTransformation = remember { ThousandSeparatorTransformation() }

    var menuProduct by remember { mutableStateOf<Product?>(null) }

    fun startEdit(p: Product) {
        editing = p
        name = p.name
        description = p.description
        // Store digits only to avoid parsing issues (12.990 / 12,990).
        price = p.price.toString().filter { it.isDigit() }
        imageUrl = p.imageUrl
        categoryId = categories.firstOrNull { it.second.equals(p.category, ignoreCase = true) }?.first ?: 2L
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Volver") } }
            )
        }
    ) { innerPadding ->
        if (products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No hay molduras para editar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    Card(
                        elevation = CardDefaults.cardElevation(2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "$ ${formatWithThousands(product.price.toString())}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Box {
                                IconButton(onClick = { menuProduct = product }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Opciones")
                                }

                                DropdownMenu(
                                    expanded = menuProduct?.id == product.id,
                                    onDismissRequest = { menuProduct = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Modificar") },
                                        onClick = {
                                            menuProduct = null
                                            startEdit(product)
                                        },
                                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Cancelar") },
                                        onClick = { menuProduct = null }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (editing != null) {
        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Editar Moldura") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = price,
                        onValueChange = { newValue ->
                            // Price as digits; the separator is applied visually only.
                            price = newValue.filter { it.isDigit() }
                        },
                        label = { Text("Precio") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = thousandTransformation
                    )

                    // Category
                    Box {
                        OutlinedTextField(
                            value = categories.firstOrNull { it.first == categoryId }?.second ?: "Seleccionar",
                            onValueChange = {},
                            label = { Text("Tipo de Moldura") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
                        )
                        Box(modifier = Modifier.matchParentSize().clickable { expanded = true })
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            categories.forEach { (id, name) ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        categoryId = id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = { Text("Imagen URL") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val current = editing ?: return@Button
                        vm.updateProduct(
                            ctx = context,
                            id = current.id,
                            name = name,
                            description = description,
                            price = price,
                            imageUrl = imageUrl,
                            categoryId = categoryId
                        ) { ok ->
                            if (ok) editing = null
                        }
                    }
                ) { Text("Guardar") }
            },
            dismissButton = {
                OutlinedButton(onClick = { editing = null }) { Text("Cancelar") }
            }
        )
    }
}
