package com.pp.bookxpert.composescreens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.pp.bookxpert.R
import java.io.File

@Composable
fun CameraGalleryPickerScreen(navController: NavController,modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraImageUri.value
        }
    }

    fun launchCamera(context: android.content.Context, cameraUriState: MutableState<Uri?>, onCaptured: (Uri) -> Unit) {
        val file = File(context.cacheDir, "captured_image.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        cameraUriState.value = uri
        cameraLauncher.launch(uri)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera(context, cameraImageUri, onCaptured = {
                imageUri = it
            })
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { imageUri = it }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(context, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    val onCameraClick = {
        val permission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            launchCamera(context, cameraImageUri, onCaptured = {
                imageUri = it
            })
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }


    val onGalleryClick = {
        val permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                Manifest.permission.READ_MEDIA_IMAGES
            }
            else -> {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            galleryLauncher.launch("image/*")
        } else {
            galleryPermissionLauncher.launch(permission)
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onCameraClick) {
                    Icon(modifier = Modifier.size(50.dp), painter = painterResource(id = R.drawable.ic_camera_24), contentDescription = "Camera")
                }
                IconButton(onClick = onGalleryClick) {
                    Icon(modifier = Modifier.size(50.dp),painter = painterResource(id = R.drawable.ic_gallery_24), contentDescription = "Gallery")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                IconButton(
                    onClick = {
                        navController.navigate("product_list") {
                            popUpTo("image_picker") { inclusive = true }
                        }

                              },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

