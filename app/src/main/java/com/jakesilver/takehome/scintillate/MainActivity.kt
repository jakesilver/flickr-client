package com.jakesilver.takehome.scintillate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import coil.Coil
import coil.ImageLoader
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jakesilver.takehome.api.di.apiModule
import com.jakesilver.takehome.scintillate.compose.Home
import com.jakesilver.takehome.scintillate.di.appModule
import com.jakesilver.takehome.scintillate.ui.theme.ScintillateTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {

    private val photoViewModel: PhotoViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(
                listOf(
                    apiModule,
                    appModule,
                ),
            )
        }
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
                    Home(
                        photoViewModel = photoViewModel,
                        onPhotoClick = { photoViewModel.onPhotoClicked(it.id) },
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                    )
                }
            }
        }
    }
}
