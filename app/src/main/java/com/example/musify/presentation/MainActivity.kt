package com.example.musify.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.musify.R
import com.example.musify.data.service.PlaybackService
import com.example.musify.presentation.composables.MiniPlayer
import com.example.musify.presentation.ui.navigation.AppNavigation
import com.example.musify.presentation.ui.navigation.Destination
import com.example.musify.presentation.ui.song.MusicPlayer
import com.example.musify.presentation.ui.theme.MusifyTheme
import com.example.musify.presentation.viewmodels.SharedViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    val storagePermissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(this, "Permisison is required", Toast.LENGTH_SHORT)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusifyTheme {
                SetStatusBarColor(color = colorResource(id = R.color.top_color))
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val musicControllerUiState = sharedViewModel.musicControllerUiState
                    var showMusicPlayer by remember { mutableStateOf(false) }
                    val navController = rememberNavController()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        AppNavigation(navController = navController)
                        if (showMusicPlayer) {
                            MusicPlayer(
                                onEvent = sharedViewModel::onEvent,
                                state = musicControllerUiState,
                                showBottomSheet = { showMusicPlayer = it }
                            )
                        }
                    }
                    MiniPlayer(
                        state = musicControllerUiState,
                        onEvent = sharedViewModel::onEvent,
                        onClick = { showMusicPlayer = true },
                        onPlaylistClick = {
                            navController.navigate(Destination.Playlist.route) {
                                popUpTo(Destination.Home.route) {
                                    inclusive = false
                                }
                            }
                        }
                    )
                }
            }
        }
        checkStoragePermissions()
    }

    private fun checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                requestForStoragePermissions()
            }
        } else {
            val write = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            if (write != PackageManager.PERMISSION_GRANTED && read != PackageManager.PERMISSION_GRANTED) {
                requestForStoragePermissions()
            }
        }
    }

    private fun requestForStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent()
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            try {
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                storagePermissionResultLauncher.launch(intent)
            } catch (e: Exception) {
                storagePermissionResultLauncher.launch(intent)
                Log.e("Error", "$e")
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ),
                101
            )
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 101) {
//            if (grantResults.size > 0) {
//                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
//                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
//
//                if (read && write) {
//                    Toast.makeText(this, "Storage Permissions Granted", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.destroyMediaController()
        stopService(Intent(this, PlaybackService::class.java))
    }
}

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}