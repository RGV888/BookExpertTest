package com.pp.bookxpert


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pp.bookxpert.composescreens.CameraGalleryPickerScreen
import com.pp.bookxpert.composescreens.GoogleSignInScreen
import com.pp.bookxpert.composescreens.ProductScreen
import com.pp.bookxpert.ui.theme.BookxpertTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookxpertTheme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val activity = context as? Activity
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "BookXpert") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->

        val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "sign_in") {
                    composable("sign_in") {
                        GoogleSignInScreen(navController,
                            onSignedIn = {
                                navController.navigate("product_list") {
                                    popUpTo("product_list") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("product_list") {
                        ProductScreen(navController,Modifier.padding(innerPadding))
                    }
                    composable("image_picker") {
                        CameraGalleryPickerScreen(navController,Modifier.padding(innerPadding))
                    }
                }
    }





}

class DatabaseEx {
    companion object{

        val instance : DatabaseEx by lazy {
            DatabaseEx()
        }
    }



    fun query() {
        println("Query executed")
    }


}



