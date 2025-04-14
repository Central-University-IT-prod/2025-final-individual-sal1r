package com.salir.finance.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.Goal
import com.salir.domain.model.Operation
import com.salir.finance.viewmodels.FinanceViewModel
import com.salir.finance.widgets.GoalsWidget
import com.salir.finance.widgets.OperationsWidget
import com.salir.finance.widgets.SavingsWidget
import com.salir.resources.R
import java.time.LocalDate

@Composable
fun FinanceScreen(vm: FinanceViewModel) {
    val goals = vm.goals.collectAsState(emptyList()).value
    val operations = vm.operations.collectAsState(emptyList()).value

    FinanceScreenContent(
        goals = goals,
        operations = operations,
        onDeleteGoalClick = vm::deleteGoal,
        addGoalSheetContent = { close ->
            AddGoalScreen(
                onAddClicked = vm::createGoal,
                close = close
            )
        },
        addOperationSheetContent = { close ->
            AddOperationScreen(
                createOperation = vm::createOperation,
                close = close,
                goals = goals
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinanceScreenContent(
    goals: List<Goal>,
    operations: List<Operation>,
    onEditGoalClick: (Goal) -> Unit = {},
    onDeleteGoalClick: (Goal) -> Unit = {},
    addGoalSheetContent: @Composable (close: () -> Unit) -> Unit = {},
    addOperationSheetContent: @Composable (close: () -> Unit) -> Unit = {}
) {
    var addGoalSheetIsOpened by remember { mutableStateOf(false) }
    var addOperationSheetIsOpened by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn {
            item {
                Text(
                    text = stringResource(R.string.title_bottom_nav_bar_finance_button),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
            }

            item {
                val accumulated = remember(goals) { goals.sumOf { it.sum.coerceAtMost(it.goal) } }
                val goal = remember(goals) { goals.sumOf { it.goal } }

                SavingsWidget(
                    accumulated = accumulated,
                    goalsFraction = if (goal != 0L) (accumulated.toDouble() / goal).toFloat() else 0f
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                GoalsWidget(
                    goals = goals,
                    onAddGoalClick = { addGoalSheetIsOpened = true },
                    onDeleteGoalClick = onDeleteGoalClick,
                    onEditGoalClick = onEditGoalClick
                )
            }

            item {
                OperationsWidget(
                    operations = operations,
                    onAddOperationClick = { addOperationSheetIsOpened = true }
                )
            }
        }

        val addGoalSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        if (addGoalSheetIsOpened) {
            ModalBottomSheet(
                onDismissRequest = { addGoalSheetIsOpened = false },
                contentWindowInsets = { WindowInsets.systemBars },
                sheetState = addGoalSheetState
            ) {
                addGoalSheetContent { addGoalSheetIsOpened = false }
            }
        }

        val addOperationSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        if (addOperationSheetIsOpened) {
            ModalBottomSheet(
                onDismissRequest = { addOperationSheetIsOpened = false },
                contentWindowInsets = { WindowInsets.systemBars },
                sheetState = addOperationSheetState
            ) {
                addOperationSheetContent { addOperationSheetIsOpened = false }
            }
        }
    }
}


@Composable
private fun FinanceScreenPreview() {
    FinanceScreenContent(
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

@Preview
@Composable
private fun FinanceScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        FinanceScreenPreview()
    }
}

@Preview
@Composable
private fun FinanceScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        FinanceScreenPreview()
    }
}