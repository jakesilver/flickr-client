@file:Suppress("UNCHECKED_CAST")

package com.jakesilver.takehome.scintillate

import androidx.activity.compose.ReportDrawn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jakesilver.takehome.api.PhotoSummary
import com.jakesilver.takehome.app.R
import kotlinx.coroutines.flow.Flow


@Composable
fun Home(
    photoViewModel: PhotoViewModel,
    onPhotoClick: (PhotoSummary) -> Unit,
    modifier: Modifier,
) {
    PhotoSummaryScreen(
        photoSummaries = photoViewModel.photoSummaries,
        onPhotoClick = onPhotoClick,
        modifier = modifier,
    )
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
            columns = GridCells.Fixed(1),
            modifier = Modifier.padding(12.dp),
            contentPadding = PaddingValues(all = 12.dp)
        ) {
            items(
                count = photoPagingItems.itemCount,
                key = { index ->
                    val photo = photoPagingItems[index]
                    photo?.id ?: ""
                }
            ) { index ->
                val photo = photoPagingItems[index] ?: return@items
                PhotoSummaryItem(photo = photo) {
                    onPhotoClick(photo)
                }
            }
        }
    }
}

@Composable
private fun EmptyPhotoSearch(modifier: Modifier) {
    ReportDrawn()

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.empty_state_label),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoSummaryItem(photo: PhotoSummary, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .padding(bottom = 4.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            PhotoSummaryImage(
                url = photo.url,
                contentDescription = photo.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Text(
                text = photo.url,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun PhotoSummaryImage(url: String, contentDescription: String, modifier: Modifier) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .memoryCacheKey(url)
            .crossfade(300)
            .allowHardware(false)
            .build(),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}