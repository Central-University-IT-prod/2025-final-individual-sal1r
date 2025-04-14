package com.salir.finance.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.Operation
import com.salir.finance.components.OperationItem
import com.salir.resources.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun OperationsWidget(
    operations: List<Operation>,
    onAddOperationClick: () -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.operations),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onAddOperationClick
            ) {
                Icon(
                    painterResource(R.drawable.ic_add_30),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

        var currentDate: LocalDate? = null

        operations.sortedByDescending { it.id }.sortedByDescending { it.date }.forEach { operation ->
            if (operation.date != currentDate) {
                Text(
                    text = DateTimeFormatter
                        .ofPattern("d MMMM", Locale.getDefault())
                        .format(operation.date),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                currentDate = operation.date
            }

            OperationItem(operation)
        }
    }
}


@Composable
private fun OperationsWidgetPreview() {
    Surface {
        OperationsWidget(
            operations = listOf(
                Operation(
                    id = 1,
                    sum = 100,
                    goalName = "Машина",
                    comment = "комментарий к операции",
                    date = LocalDate.now()
                ),
                Operation(
                    id = 1,
                    sum = -300,
                    goalName = "Машина",
                    date = LocalDate.now().minusDays(1)
                ),
                Operation(
                    id = 1,
                    sum = 600,
                    goalName = "Машина",
                    comment = "длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции",
                    date = LocalDate.now().minusDays(1)
                )
            )
        )
    }
}

@Preview
@Composable
private fun OperationsWidgetDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        OperationsWidgetPreview()
    }
}

@Preview
@Composable
private fun OperationsWidgetLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        OperationsWidgetPreview()
    }
}