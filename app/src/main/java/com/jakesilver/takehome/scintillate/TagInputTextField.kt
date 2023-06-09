package com.jakesilver.takehome.scintillate

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.jakesilver.takehome.app.R

@Composable
fun TagInputTextField() {
    var inputText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        label = { Text(stringResource(id = R.string.tag_search_hint)) }
    )
}
