package com.jakesilver.photoclient.scintillate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import coil.Coil
import coil.ImageLoader
import com.jakesilver.photoclient.scintillate.navigation.ScintillateApp
import com.jakesilver.photoclient.scintillate.ui.theme.ScintillateTheme
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val photoSearchViewModel: PhotoSearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .crossfade(true)
                .build(),
        )

        setContent {
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
