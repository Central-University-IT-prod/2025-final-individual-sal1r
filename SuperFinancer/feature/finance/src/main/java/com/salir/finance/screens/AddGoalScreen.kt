package com.salir.finance.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.finance.components.MyDatePicker
import com.salir.resources.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddGoalScreen(
    onAddClicked: (name: String, goal: Long, date: LocalDate?) -> Unit = { _, _, _ -> },
    close: () -> Unit = {}
) {
    AddGoalScreenContent(
        onAddClicked = onAddClicked,
        close = close
    )
}

@Composable
private fun AddGoalScreenContent(
    onAddClicked: (name: String, goal: Long, date: LocalDate?) -> Unit = { _, _, _ -> },
    close: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var goal by remember { mutableLongStateOf(0L) }
    var date by remember { mutableStateOf<LocalDate?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.goal_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = goal.toString(),
            onValueChange = { goal = it.toLongOrNull() ?: 0L },
            label = { Text(stringResource(R.string.sum)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            Button(onClick = { showDatePicker = true }) {
                Text(text = date?.format(
                    DateTimeFormatter.ofPattern("dd.MM.yyyy")) ?:
                    stringResource(R.string.select_date
                ))
            }
            Spacer(modifier = Modifier.weight(1f))
            if (date != null) {
                OutlinedButton(
                    onClick = { date = null }
                ) {
                    Text(stringResource(R.string.clear_date))
                }
            }
        }

        if (showDatePicker) {
            MyDatePicker(
                onDateSelected = { selectedDate ->
                    date = selectedDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onAddClicked(name, goal, date)
                close()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && goal > 0
        ) {
            Text(stringResource(R.string.add_goal))
        }
    }
}


@Composable
private fun AddGoalScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        AddGoalScreenContent()
    }
}

@Preview
@Composable
private fun AddGoalScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        AddGoalScreenPreview()
    }
}

@Preview
@Composable
private fun AddGoalScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        AddGoalScreenPreview()
    }
}