package com.jakesilver.photoclient.scintillate.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jakesilver.photoclient.api.PhotoDetails
import com.jakesilver.photoclient.scintillate.ui.theme.ScintillateTheme
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsUiState
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    onUpClicked: () -> Unit,
    photoDetailsViewModel: PhotoDetailsViewModel = koinViewModel(),
    modifier: Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onUpClicked() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
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
    val uiState by photoDetailsViewModel.photoDetailsUiState.collectAsState(
        initial = PhotoDetailsUiState.Loading
    )
    PhotoDetailContent(uiState = uiState, modifier = modifier)
}

@Composable
fun DateText(
    date: Date,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    Text(
        text = formattedDate,
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = style,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
fun PhotoDetailContent(
    uiState: PhotoDetailsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(
            rememberScrollState()
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            is PhotoDetailsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.padding(32.dp)
                )
            }

            is PhotoDetailsUiState.PhotoDetails -> {
                val photoDetails = uiState.details
                PhotoImage(
                    url = photoDetails.url,
                    contentDescription = photoDetails.description,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = photoDetails.title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                )
                if (photoDetails.description.isNotEmpty()) {
                    Text(
                        text = photoDetails.description,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Date Taken:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    DateText(
                        date = photoDetails.dateTaken,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Date Posted:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    DateText(
                        date = photoDetails.datePosted,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            is PhotoDetailsUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(
                        text = "Error",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                    Text(
                        text = uiState.message,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

// Preview Parameter Providers
class PhotoDetailsStateProvider : PreviewParameterProvider<PhotoDetailsUiState> {
    override val values = sequenceOf(
        PhotoDetailsUiState.Loading,
        PhotoDetailsUiState.Error("No photo found."),
        PhotoDetailsUiState.Error("Network connection failed. Please try again."),
        PhotoDetailsUiState.PhotoDetails(
            details = PhotoDetails(
                id = "1",
                url = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U",
                title = "Beautiful Dog",
                description = "A lovely black labrador enjoying the outdoors",
                dateTaken = Date(),
                datePosted = Date()
            )
        ),
        PhotoDetailsUiState.PhotoDetails(
            details = PhotoDetails(
                id = "2",
                url = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U",
                title = "A very long photo title that spans multiple lines to test our layout and text wrapping behavior",
                description = "An extremely detailed description that goes on for quite a while to test how our UI handles longer text content and whether it displays properly in both light and dark themes",
                dateTaken = Date(),
                datePosted = Date()
            )
        )
    )
}

// Preview Functions
@PreviewLightDark
@Composable
fun DateTextPreview() {
    ScintillateTheme {
        DateText(
            date = Date(),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@PreviewLightDark
@Composable
fun PhotoDetailContentPreview(
    @PreviewParameter(PhotoDetailsStateProvider::class) uiState: PhotoDetailsUiState
) {
    ScintillateTheme {
        PhotoDetailContent(
            uiState = uiState,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun DetailScreenPreview() {
    ScintillateTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Photo Details",
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                )
            },
            content = { paddingValues ->
                PhotoDetailContent(
                    uiState = PhotoDetailsUiState.PhotoDetails(
                        details = PhotoDetails(
                            id = "1",
                            url = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U",
                            title = "Beautiful Dog",
                            description = "A lovely black labrador enjoying the outdoors",
                            dateTaken = Date(),
                            datePosted = Date()
                        )
                    ),
                    modifier = Modifier.padding(paddingValues)
                )
            },
        )
    }
}
