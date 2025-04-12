package com.pp.bookxpert.statehanler

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object ProductList : Screen("product_list")
    object imagePicker : Screen("image_picker")
}
