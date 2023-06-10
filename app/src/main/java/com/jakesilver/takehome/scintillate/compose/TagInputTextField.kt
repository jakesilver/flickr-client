package com.jakesilver.takehome.scintillate.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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

@Composable
fun TagInputTextField(onTagQuery: (String) -> Unit, modifier: Modifier) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text(stringResource(id = R.string.tag_search_label)) },
            modifier = Modifier.width(IntrinsicSize.Max),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { onTagQuery(inputText) },
            content = {
                Text(
                    text = stringResource(id = R.string.tag_search_hint),
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        )
    }
}
