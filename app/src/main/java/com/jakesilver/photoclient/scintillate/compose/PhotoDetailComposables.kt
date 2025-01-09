package com.jakesilver.photoclient.scintillate.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsUiState
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoDetailsViewModel
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
    val uiState by photoDetailsViewModel.photoDetailsUiState.collectAsState(
        initial = PhotoDetailsUiState.Loading
    )
    Column(
        modifier = modifier.verticalScroll(
            rememberScrollState()
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (uiState) {
            is PhotoDetailsUiState.Loading -> {
                CircularProgressIndicator()
            }

            is PhotoDetailsUiState.PhotoDetails -> {
                val photoDetails = (uiState as PhotoDetailsUiState.PhotoDetails).details
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
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = photoDetails.description,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodySmall,
                )
                DateText(
                    date = photoDetails.dateTaken,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                )
                DateText(
                    date = photoDetails.datePosted,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            is PhotoDetailsUiState.Error ->
                Text(
                    text = (uiState as PhotoDetailsUiState.Error).message,
                )
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
        textAlign = TextAlign.Center,
        modifier = modifier,
        style = style,
    )
}
