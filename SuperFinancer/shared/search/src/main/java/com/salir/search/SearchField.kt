package com.salir.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.salir.resources.R

@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    expanded: Boolean = false,
    onExpandedChanged: (Boolean) -> Unit = {},
    onSearch: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current

    Box {
        var textFieldHeight by remember { mutableStateOf(0.dp) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .zIndex(1f)
                .padding(horizontal = 16.dp)
                .onPlaced {
                    textFieldHeight = with(density) { it.size.height.toDp() }
                }
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        if (it.isFocused) {
                            onExpandedChanged(true)
                        }
                    },
                value = value,
                onValueChange = onValueChange,
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_20),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    VoiceInputButton {
                        if (it != null) {
                            onExpandedChanged(true)
                            onValueChange(value + it)
                        }
                    }
                },
                singleLine = true,
                shape = CircleShape,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        onSearch()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                )
            )

            AnimatedContent(
                targetState = expanded,
            ) {
                if (it) {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))

                        ElevatedButton(
                            onClick = {
                                focusManager.clearFocus()
                                onValueChange("")
                                onExpandedChanged(false)
                            },
                        ) {
                            Text(text = stringResource(R.string.cancel_input))
                        }
                    }
                }
            }
        }

        AnimatedContent(
            targetState = expanded,
        ) {
            if (it) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxHeight()
                ) {
                    Spacer(modifier = Modifier.height(textFieldHeight))
                    content()
                }
            }
        }
    }
}

@Composable
private fun SearchFieldPreview() {
    Surface {
        SearchField(
            "",
            {},
            true
        )
    }
}

@Preview
@Composable
private fun SearchFieldDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        SearchFieldPreview()
    }
}

@Preview
@Composable
private fun SearchFieldLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        SearchFieldPreview()
    }
}