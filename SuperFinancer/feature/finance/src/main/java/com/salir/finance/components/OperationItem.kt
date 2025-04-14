package com.salir.finance.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.salir.domain.model.Operation
import com.salir.resources.R
import com.salir.util.formatRub
import java.time.LocalDate

@Composable
fun OperationItem(
    operation: Operation
) {
    val isRefill = operation.sum > 0

    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_rubble_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(6.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(if (isRefill) R.string.refill else R.string.debiting),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                )
                Text(
                    text = operation.goalName,
                    style = MaterialTheme.typography.titleSmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                )
            }

            Text(
                text = (if (isRefill) "+" else "") + formatRub(operation.sum),
                color = if (isRefill) Color.Green else Color.Red
            )
        }

        operation.comment?.let { comment ->
            val backgroundColor = CardDefaults.cardColors().containerColor
            val brush = remember {
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        backgroundColor,
                    )
                )
            }

            var expanded by remember { mutableStateOf(false) }
            var didOverflowHeight by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                onClick = { expanded = !expanded },
            ) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .animateContentSize()
                ) {
                    Text(
                        text = comment,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        onTextLayout = {
                            didOverflowHeight = it.didOverflowHeight
                        }
                    )

                    /*
                    градиент указвает на то, что комментарий можно
                    развернуть, в ином случае он мешает и путает,
                    поэтому он показывается только если комментарий
                    слишком длинный
                     */
                    if (didOverflowHeight) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(brush)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun OperationItemPreview() {
    Surface {
        Column {
            OperationItem(
                Operation(
                    id = 1,
                    sum = 100,
                    goalName = "Машина",
                    comment = "комментарий к операции",
                    date = LocalDate.now()
                )
            )
            OperationItem(
                Operation(
                    id = 1,
                    sum = -300,
                    goalName = "Машина",
                    date = LocalDate.now().minusDays(1)
                )
            )
            OperationItem(
                Operation(
                    id = 1,
                    sum = 600,
                    goalName = "Машина",
                    comment = "длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции длинный комментарий к операции",
                    date = LocalDate.now().minusDays(1)
                )
            )
        }
    }
}

@Preview
@Composable
private fun OperationItemDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        OperationItemPreview()
    }
}

@Preview
@Composable
private fun OperationItemLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        OperationItemPreview()
    }
}