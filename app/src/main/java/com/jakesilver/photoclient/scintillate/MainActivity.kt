package com.jakesilver.photoclient.scintillate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import coil.Coil
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jakesilver.photoclient.scintillate.navigation.ScintillateApp
import com.jakesilver.photoclient.scintillate.ui.theme.ScintillateTheme
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val photoSearchViewModel: PhotoSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .crossfade(true)
                .build(),
        )

        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            LaunchedEffect(systemUiController, useDarkIcons) {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = useDarkIcons,
                )
            }
            ScintillateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ScintillateApp(
                        photoSearchViewModel = photoSearchViewModel,
                    )
                }
            }
        }
    }
}
