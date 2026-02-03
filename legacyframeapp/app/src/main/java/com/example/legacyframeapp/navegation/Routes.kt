package com.example.legacyframeapp.navegation

// Centralized definition of navigation routes to avoid magic strings.
sealed class Route(val path: String) {
    data object Splash    : Route("splash")
    data object Home      : Route("home")
    data object Profile   : Route("profile")
    data object Login     : Route("login")
    data object Register  : Route("register")
    data object Molduras  : Route("molduras")
    data object Cuadros   : Route("cuadros")
    data object Contact   : Route("contact")
    data object Cart      : Route("cart")
    data object AddProduct: Route("add_product")
    data object AddCuadro : Route("add_cuadro")
    data object Admin     : Route("admin")
    data object ChangeProductImage : Route("change_product_image")
    data object EditProduct : Route("edit_product")
    data object DeleteProduct : Route("delete_product")
    data object DeleteCuadro : Route("delete_cuadro")
    data object Settings  : Route("settings")
    data object Purchases : Route("purchases")
    data object Terms     : Route("terms")
    data object ResetPassword : Route("reset_password")
}

// Note: using Route prevents renaming errors and simplifies maintenance.