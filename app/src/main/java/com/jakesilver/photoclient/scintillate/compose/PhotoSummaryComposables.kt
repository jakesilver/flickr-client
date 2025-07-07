package com.jakesilver.photoclient.scintillate.compose

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jakesilver.photoclient.api.PhotoSummary
import com.jakesilver.photoclient.app.R
import com.jakesilver.photoclient.scintillate.ui.theme.ScintillateTheme
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoSearchViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun Home(
    photoSearchViewModel: PhotoSearchViewModel = koinViewModel(),
    onPhotoClick: (PhotoSummary) -> Unit,
    modifier: Modifier,
) {
    val uiState by photoSearchViewModel.uiState.collectAsState()

    Column(
        modifier = modifier,
    ) {
        TagInputTextField(
            onTagQuery = { tag -> photoSearchViewModel.searchByTag(tag) },
            modifier = Modifier.padding(8.dp),
        )

        uiState.photos?.let { photosFlow ->
            PhotoSummaryScreen(
                photoSummaries = photosFlow,
                onPhotoClick = onPhotoClick,
                modifier = modifier,
            )
        } ?: EmptyPhotoSearch(modifier = modifier)
    }
}

@Composable
private fun PhotoSummaryScreen(
    photoSummaries: Flow<PagingData<PhotoSummary>>,
    onPhotoClick: (PhotoSummary) -> Unit = {},
    modifier: Modifier,
) {
    val photoPagingItems = photoSummaries.collectAsLazyPagingItems()
    if (photoPagingItems.itemCount == 0) {
        EmptyPhotoSearch(modifier = modifier)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.padding(12.dp),
            contentPadding = PaddingValues(all = 12.dp),
        ) {
            items(
                count = photoPagingItems.itemCount,
                key = { index ->
                    val photo = photoPagingItems[index]
                    // Combine index with photo ID to ensure uniqueness even with duplicate IDs from API
                    "${index}_${photo?.id ?: "unknown"}"
                },
            ) { index ->
                val photo = photoPagingItems[index] ?: return@items
                PhotoSummaryItem(photo = photo, onClick = {
                    onPhotoClick(photo)
                })
            }
        }
    }
}

@Composable
fun EmptyPhotoSearch(modifier: Modifier = Modifier) {
    ReportDrawn()

    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.empty_state_label),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Try searching for something else",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun PhotoSummaryItem(
    photo: PhotoSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 4.dp),
    ) {
        Column(Modifier.fillMaxWidth()) {
            PhotoImage(
                url = photo.url,
                contentDescription = photo.title.ifEmpty { "Photo" },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = photo.title.ifEmpty { "Untitled" },
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 12.dp),
            )
        }
    }
}

@Composable
fun PhotoImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .memoryCacheKey(url)
            .crossfade(300)
            .allowHardware(false)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
    )
}

// Preview Parameter Providers
class PhotoSummaryProvider : PreviewParameterProvider<PhotoSummary> {
    override val values = sequenceOf(
        PhotoSummary(
            id = "1",
            title = "Beautiful dog",
            url = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"
        ),
        PhotoSummary(
            id = "2",
            title = "A very long title that might wrap to multiple lines and test our text handling",
            url = "https://picsum.photos/200/300/"
        ),
        PhotoSummary(
            id = "3",
            title = "",
            url = "https://picsum.photos/seed/picsum/200/300"
        )
    )
}

// Preview Functions
@PreviewLightDark
@Composable
@Preview
fun EmptyPhotoSearchPreview() {
    ScintillateTheme {
        EmptyPhotoSearch(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        )
    }
}

@PreviewLightDark
@Composable
@Preview
fun PhotoSummaryItemPreview(
    @PreviewParameter(PhotoSummaryProvider::class) photo: PhotoSummary
) {
    ScintillateTheme {
        PhotoSummaryItem(
            photo = photo,
            onClick = {}
        )
    }
}

@PreviewLightDark
@Composable
@Preview
fun PhotoImagePreview() {
    ScintillateTheme {
        PhotoImage(
            url = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U",
            contentDescription = "Sample photo",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@PreviewLightDark
@Composable
@Preview
fun PhotoSummaryGridPreview() {
    ScintillateTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(12.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            contentPadding = PaddingValues(all = 12.dp),
        ) {
            items(4) { index ->
                PhotoSummaryItem(
                    photo = PhotoSummary(
                        id = "$index",
                        title = "Photo ${index + 1}",
                        url = "https://fastly.picsum.photos/id/237/200/300.jpg?hmac=TmmQSbShHz9CdQm0NkEjx1Dyh_Y984R9LpNrpvH2D_U"
                    ),
                    onClick = {}
                )
            }
        }
    }
}
