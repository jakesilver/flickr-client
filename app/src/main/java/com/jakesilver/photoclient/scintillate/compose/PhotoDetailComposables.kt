package com.jakesilver.photoclient.scintillate.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakesilver.photoclient.app.R
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsUiState
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    onUpClicked: () -> Unit,
    photoDetailsViewModel: PhotoDetailsViewModel = getViewModel(),
    modifier: Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.background,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onUpClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                        )
                    }
                },
            )
        },
        content = {
            PhotoDetail(
                photoDetailsViewModel,
                modifier.padding(it),
            )
        },
    )
}

@Composable
fun PhotoDetail(
    photoDetailsViewModel: PhotoDetailsViewModel,
    modifier: Modifier,
) {
    val uiState by photoDetailsViewModel.photoDetailsUiState.collectAsState(initial = PhotoDetailsUiState(isLoading = true))
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier.padding(all = 10.dp)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.photoDetails != null -> {
                    uiState.photoDetails?.let { photoDetails ->
                        PhotoImage(
                            url = photoDetails.url,
                            contentDescription = photoDetails.description,
                            modifier = modifier,
                        )
                        Text(
                            text = photoDetails.title,
                            Modifier.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Text(
                            text = photoDetails.description,
                            Modifier.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                        )
                        DateText(
                            date = photoDetails.dateTaken,
                            Modifier.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        DateText(
                            date = photoDetails.datePosted,
                            Modifier.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    } ?: run {
                        Text(
                            text = stringResource(id = R.string.generic_error)
                        )
                    }
                }

                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: stringResource(id = R.string.generic_error),
                    )
                }

            }
        }
    }
}

@Composable
private fun DateText(
    date: Date,
    modifier: Modifier,
    style: androidx.compose.ui.text.TextStyle,
) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    Text(
        text = formattedDate,
        modifier = modifier,
        style = style,
    )
}
