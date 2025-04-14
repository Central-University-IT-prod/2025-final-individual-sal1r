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
import com.salir.domain.model.Goal
import com.salir.finance.components.GoalCard
import com.salir.resources.R
import java.time.LocalDate

@Composable
fun GoalsWidget(
    goals: List<Goal>,
    onAddGoalClick: () -> Unit = {},
    onEditGoalClick: (Goal) -> Unit = {},
    onDeleteGoalClick: (Goal) -> Unit = {}
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.goals),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onAddGoalClick
            ) {
                Icon(
                    painterResource(R.drawable.ic_add_30),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

        goals.forEach { goal ->
            GoalCard(
                goal = goal,
                onEditClick = onEditGoalClick,
                onDeleteClick = onDeleteGoalClick
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
private fun GoalsWidgetPreview() {
    Surface {
        GoalsWidget(
            goals = listOf(
                Goal(
                    id = 1L,
                    name = "Goal 1",
                    sum = 500,
                    goal = 2500,
                    date = LocalDate.of(2025, 2, 1)
                ),
                Goal(
                    id = 2L,
                    name = "Goal 2",
                    sum = 1250,
                    goal = 2500,
                    date = LocalDate.of(2025, 4, 1)
                ),
                Goal(
                    id = 3L,
                    name = "Goal 3",
                    sum = 2000,
                    goal = 2500
                )
            ),
            onAddGoalClick = {}
        )
    }
}

@Preview
@Composable
private fun GoalsWidgetDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        GoalsWidgetPreview()
    }
}

@Preview
@Composable
private fun GoalsWidgetLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        GoalsWidgetPreview()
    }
}