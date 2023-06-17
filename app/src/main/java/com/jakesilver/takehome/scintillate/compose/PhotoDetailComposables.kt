package com.jakesilver.takehome.scintillate.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakesilver.takehome.api.PhotoDetails
import com.jakesilver.takehome.app.R
import com.jakesilver.takehome.scintillate.PhotoDetailUiState
import com.jakesilver.takehome.scintillate.PhotoViewModel
import org.koin.androidx.compose.getViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    title: String,
    onUpClicked: () -> Unit,
    photoViewModel: PhotoViewModel = getViewModel(),
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
            PhotoDetail(photoViewModel, modifier.padding(it))
        },
    )
}

@Composable
fun PhotoDetail(
    photoViewModel: PhotoViewModel = getViewModel(),
    modifier: Modifier,
) {
    photoViewModel.onPhotoClicked(photoViewModel.photoId)
    val photoDetails by photoViewModel.photoDetails.collectAsState(initial = PhotoDetailUiState())
    Column(Modifier.fillMaxWidth()) {
        Box(Modifier.padding(all = 10.dp)) {
            when {
                photoDetails.isLoading -> {
                    CircularProgressIndicator()
                }

                photoDetails.photoDetails != null -> {
                    photoDetails.photoDetails?.let { photoDetails ->
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
                    }
                }

                photoDetails.errorMessage != null -> {
                    Text(
                        text = photoDetails.errorMessage
                            ?: stringResource(id = R.string.generic_error)
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
    style: androidx.compose.ui.text.TextStyle
) {
    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    Text(
        text = formattedDate,
        modifier = modifier,
        style = style,
    )
}
