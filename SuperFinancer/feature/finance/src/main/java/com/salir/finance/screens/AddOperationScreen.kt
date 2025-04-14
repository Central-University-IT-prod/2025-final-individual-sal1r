package com.salir.finance.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.Goal
import com.salir.resources.R

@Composable
fun AddOperationScreen(
    createOperation: (sum: Long, goalId: Long, comment: String?) -> Unit = { _, _, _ -> },
    close: () -> Unit = {},
    goals: List<Goal> = listOf() // DropDownMenu
) {
    AddOperationScreenContent(
        createOperation = createOperation,
        close = close,
        goals = goals
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOperationScreenContent(
    createOperation: (sum: Long, goalId: Long, comment: String?) -> Unit = { _, _, _ -> },
    close: () -> Unit = {},
    goals: List<Goal> = listOf()
) {
    var selectedGoal by remember { mutableStateOf<Goal?>(null) }
    var sum by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedGoal?.name ?: stringResource(R.string.select_goal),
                onValueChange = { },
                label = { Text(stringResource(R.string.goal)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    .clickable { expanded = !expanded },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                goals.forEach { goal ->
                    DropdownMenuItem(
                        text = { Text(goal.name) },
                        onClick = {
                            selectedGoal = goal
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = sum,
            onValueChange = { sum = it },
            label = { Text(stringResource(R.string.sum)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text(stringResource(R.string.comment)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                selectedGoal?.let {
                    createOperation(sum.toLongOrNull() ?: 0L, it.id, comment.ifBlank { null })
                }
                close()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedGoal != null && sum.isNotBlank() && sum.toLongOrNull() != null
        ) {
            Text(stringResource(R.string.add_operation))
        }
    }
}



@Composable
private fun AddOperationScreenPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        AddOperationScreenContent(
            goals = listOf(
                Goal(1, "Купить машину", 50000, 100000),
                Goal(2, "Путешествие", 30000, 60000)
            )
        )
    }
}

@Preview
@Composable
private fun AddOperationScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        AddOperationScreenPreview()
    }
}

@Preview
@Composable
private fun AddOperationScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        AddOperationScreenPreview()
    }
}