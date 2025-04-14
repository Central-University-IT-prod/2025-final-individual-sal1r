package com.salir.finance.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.Goal
import com.salir.resources.R
import com.salir.util.formatRub
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun GoalCard(
    goal: Goal,
    onEditClick: (Goal) -> Unit = {},
    onDeleteClick: (Goal) -> Unit = {}
) {
    val progress: Float = remember(goal) {
        (goal.sum.toDouble() / goal.goal).toFloat().coerceAtMost(1f)
    }

    ElevatedCard {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flag_36),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = goal.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee(Int.MAX_VALUE),
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = goal.date?.let {
                                DateTimeFormatter.ofPattern("dd.MM.yyyy").format(it)
                            } ?: stringResource(R.string.goal__no_date),
                            style = MaterialTheme.typography.titleSmall,
                            color = LocalContentColor.current.copy(alpha = 0.7f)
                        )
                    }

                    Box {
                        var dropDownExpanded by remember { mutableStateOf(false) }

                        IconButton(
                            onClick = {
                                dropDownExpanded = true
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_more_vert_24),
                                contentDescription = null
                            )
                        }

                        DropdownMenu(
                            expanded = dropDownExpanded,
                            onDismissRequest = { dropDownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_edit_20),
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(stringResource(R.string.edit))
                                    }
                                },
                                onClick = { onEditClick(goal) }
                            )

                            DropdownMenuItem(
                                text = {
                                    Row {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_delete_20),
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(stringResource(R.string.delete))
                                    }
                                },
                                onClick = { onDeleteClick(goal) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = stringResource(
                            R.string.goal__sum_of_goal, formatRub(goal.sum),  formatRub(goal.goal)
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${(progress * 100).roundToInt()}%",
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    val fullWidth = maxWidth

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .clip(CircleShape)
                    ) {
                        drawRect(
                            brush = Brush.horizontalGradient(
                                listOf(Color.Red, Color.Yellow, Color.Green),
                                startX = 0f,
                                endX = fullWidth.toPx()
                            ),
                            size = Size(
                                fullWidth.toPx() * progress,
                                constraints.maxHeight.toFloat()
                            )
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun GoalCardPreview() {
    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoalCard(
                Goal(
                    id = 1L,
                    name = "Goal 1",
                    sum = 500,
                    goal = 2500,
                    date = LocalDate.of(2025, 2, 1)
                )
            )
            GoalCard(
                Goal(
                    id = 1L,
                    name = "Goal 2",
                    sum = 1250,
                    goal = 2500,
                    date = LocalDate.of(2025, 4, 1)
                )
            )
            GoalCard(
                Goal(
                    id = 1L,
                    name = "Goal 3",
                    sum = 2000,
                    goal = 2500
                )
            )
        }
    }
}

@Preview
@Composable
private fun GoalCardDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        GoalCardPreview()
    }
}

@Preview
@Composable
private fun GoalCardLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        GoalCardPreview()
    }
}