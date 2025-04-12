package com.pp.bookxpert.composescreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pp.bookxpert.R
import com.pp.bookxpert.models.ProductEntity
import com.pp.bookxpert.statehanler.UiState
import com.pp.bookxpert.viewmodels.ProductViewModel


@Composable
fun ProductScreen(navController: NavController,modifier: Modifier = Modifier,viewModel: ProductViewModel = hiltViewModel()) {
    val uiState by viewModel.productState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var showPdf by remember { mutableStateOf(false) }
    var images by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(modifier
        .fillMaxSize()) {

        ActionButtonsRow(
            onPdfClick = { showPdf = true },
            onCameraClick = { images=true },
        )


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            when (uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }

                is UiState.Error -> {
                    val error = (uiState as UiState.Error).message
                    Column {
                        Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                        Button(onClick = { viewModel.loadProducts() }) {
                            Text("Retry")
                        }
                    }
                }

                is UiState.Success -> {
                    val products = (uiState as UiState.Success).data
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onDelete = { viewModel.deleteProduct(product) },
                                onUpdate = {
                                    selectedProduct = product
                                    showDialog = true
                                }
                            )
                        }
                    }
                }

            }


            if (showDialog && selectedProduct != null) {
                EditProductDialog(
                    product = selectedProduct!!,
                    onDismiss = {
                        showDialog = false
                        selectedProduct = null
                    },
                    onUpdate = { updatedProduct ->
                        viewModel.updateProduct(updatedProduct)
                        showDialog = false
                        selectedProduct = null
                    }
                )
            }

            if (showPdf) {
                PdfViewerScreen(
                    pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf",
                    onCloseClick = { showPdf = false }
                )
            }

            if (images) {
               navController.navigate("image_picker")
            }


        }
    }
}


@Composable
fun ActionButtonsRow(
    onPdfClick: () -> Unit,
    onCameraClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onPdfClick) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_picture_as_pdf_24),
                contentDescription = "Open PDF",
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
        }

        IconButton(onClick = onCameraClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_camera_24),
                contentDescription = "Open Camera",
                tint = Color.Blue,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}





@Composable
fun EditProductDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onUpdate: (ProductEntity) -> Unit
) {
    val gson = remember { Gson() }
    val mapType = object : TypeToken<Map<String, Any?>>() {}.type
    val dataMap = remember { mutableStateOf(gson.fromJson<Map<String, Any?>>(product.dataJson, mapType)) }

    var name by remember { mutableStateOf(product.name) }
    var color by remember { mutableStateOf(dataMap.value["color"]?.toString() ?: "") }
    var capacity by remember { mutableStateOf(dataMap.value["capacity"]?.toString() ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Product") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Color") })
                OutlinedTextField(value = capacity, onValueChange = { capacity = it }, label = { Text("Capacity") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val updatedMap = dataMap.value.toMutableMap().apply {
                    put("color", color)
                    put("capacity", capacity)
                }
                val updatedJson = gson.toJson(updatedMap)
                product.name=name
                onUpdate(product.copy(dataJson = updatedJson))
                onDismiss()
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


@Composable
fun ProductCard(
    product: ProductEntity,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${product.name}")
            //  Text("Color: ${product.color}")
            //  Text("Capacity: ${product.capacity}")
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Button(onClick = onDelete) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onUpdate) {
                    Text("Update")
                }
            }
        }
    }
}