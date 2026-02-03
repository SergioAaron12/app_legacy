package com.example.legacyframeapp.ui.components

import androidx.compose.material.icons.Icons // Material icons
import androidx.compose.material.icons.filled.Home // Home icon
import androidx.compose.material.icons.filled.AccountCircle // Login icon
import androidx.compose.material.icons.filled.Person // Register icon
import androidx.compose.material.icons.automirrored.filled.ListAlt // Molduras icon
import androidx.compose.material.icons.automirrored.filled.Logout // Logout icon
import androidx.compose.material.icons.filled.Photo // Cuadros icon
import androidx.compose.material.icons.filled.ShoppingCart // Cart icon
import androidx.compose.material.icons.filled.AdminPanelSettings // Admin icon
import androidx.compose.material3.Icon // Drawer item icon
import androidx.compose.material3.NavigationDrawerItem // Selectable item
import androidx.compose.material3.NavigationDrawerItemDefaults // Style defaults
import androidx.compose.material3.Text // Text
import androidx.compose.material3.ModalDrawerSheet // Drawer content container
import androidx.compose.runtime.Composable // Composable marker
import androidx.compose.ui.Modifier // Modifier
import androidx.compose.ui.graphics.vector.ImageVector // Icon type

// Small data class to represent each drawer option.
data class DrawerItem( // Drawer menu item structure
    val label: String, // Text to display
    val icon: ImageVector, // Item icon
    val onClick: () -> Unit // Action when clicked
)

@Composable // Drawer component for ModalNavigationDrawer
fun AppDrawer(
    currentRoute: String?, // Current route (to mark selected if desired)
    items: List<DrawerItem>, // Items to display
    modifier: Modifier = Modifier // Optional modifier
) {
    ModalDrawerSheet( // Sheet containing the drawer content
        modifier = modifier // Chainable modifier
    ) {
        // Iterate through options and render items.
        items.forEach { item -> // For each item
            NavigationDrawerItem( // Material stateful item
                label = { Text(item.label) }, // Visible text
                selected = false,
                onClick = item.onClick, // Action on click
                icon = { Icon(item.icon, contentDescription = item.label) }, // Icon
                modifier = Modifier, // No extra modifiers
                colors = NavigationDrawerItemDefaults.colors() // Default style
            )
        }
    }
}


@Composable
fun loggedOutDrawerItems(
    onHome: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onCuadros: () -> Unit,
    onMolduras: () -> Unit,
    onCart: () -> Unit
): List<DrawerItem> = listOf(
    DrawerItem("Inicio", Icons.Filled.Home, onHome),
    DrawerItem("Molduras", Icons.AutoMirrored.Filled.ListAlt, onMolduras),
    DrawerItem("Cuadros", Icons.Filled.Photo, onCuadros),
    DrawerItem("Carrito", Icons.Filled.ShoppingCart, onCart),
    DrawerItem("Iniciar sesión", Icons.Filled.AccountCircle, onLogin),
    DrawerItem("Registro", Icons.Filled.Person, onRegister)
)

@Composable
fun loggedInDrawerItems(
    onHome: () -> Unit,
    onMolduras: () -> Unit,
    onCuadros: () -> Unit,
    onCart: () -> Unit,
    onAdmin: (() -> Unit)? = null, // Optional callback for the admin panel
    onLogout: () -> Unit
): List<DrawerItem> {
    // Create a mutable list to conditionally add items.
    val items = mutableListOf(
        DrawerItem("Inicio", Icons.Filled.Home, onHome),
        DrawerItem("Molduras", Icons.AutoMirrored.Filled.ListAlt, onMolduras),
        DrawerItem("Cuadros", Icons.Filled.Photo, onCuadros),
        DrawerItem("Carrito", Icons.Filled.ShoppingCart, onCart)
    )

    // If onAdmin is not null, add the Admin item to the list.
    onAdmin?.let { adminAction ->
        items.add(DrawerItem("Administrador", Icons.Filled.AdminPanelSettings, adminAction))
    }

    // Finally, add the logout item.
    items.add(DrawerItem("Cerrar Sesión", Icons.AutoMirrored.Filled.Logout, onLogout))

    // Return the full list.
    return items
}