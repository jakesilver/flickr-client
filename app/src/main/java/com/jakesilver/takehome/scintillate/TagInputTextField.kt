package com.jakesilver.takehome.scintillate

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jakesilver.takehome.app.R
import com.jakesilver.takehome.scintillate.ui.theme.Purple80
import com.jakesilver.takehome.scintillate.ui.theme.ScintillateTheme

@Composable
fun TagInputTextField() {
    var inputText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        label = { Text(stringResource(id = R.string.tag_search_hint)) }
    )

    Divider(
        color = Purple80,
        thickness = 1.dp,
    )
}
