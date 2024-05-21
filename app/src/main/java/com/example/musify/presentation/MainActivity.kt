package com.example.musify.presentation

import android.app.ActivityManager
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.musify.R
import com.example.musify.domain.service.PlaybackService
import com.example.musify.presentation.navigation.AppNavigation
import com.example.musify.presentation.screens.HomeScreen
import com.example.musify.presentation.screens.HomeScreenV2
import com.example.musify.presentation.theme.MusifyTheme
import com.example.musify.presentation.view_model.HomeScreenViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeScreenViewModel by viewModels()

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
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }

        checkStoragePermissions()
        startService()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101){
            if(grantResults.size > 0){
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (read && write) {
                    Toast.makeText(this, "Storage Permissions Granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Storage Permissions Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startService() {
        startService(Intent(this, PlaybackService::class.java))
    }

    private fun foregroundServiceRunning(): Boolean {
        val activityManager =
            applicationContext?.getSystemService(ComponentActivity.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (PlaybackService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.myAudioPlayer.exoPlayer.removeListener(viewModel.mediaPlayerStateHandler)
        viewModel.myAudioPlayer.exoPlayer.release()
        if (!foregroundServiceRunning()) {
            stopService(Intent(this, PlaybackService::class.java))
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        viewModel.myAudioPlayer.exoPlayer.removeListener(viewModel.mediaPlayerStateHandler)
//        viewModel.myAudioPlayer.exoPlayer.release()
//    }
}

@Composable
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(color)
    }
}