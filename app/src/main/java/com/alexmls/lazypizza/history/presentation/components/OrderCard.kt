package com.alexmls.lazypizza.history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexmls.lazypizza.core.designsystem.theme.color
import com.alexmls.lazypizza.core.designsystem.theme.success
import com.alexmls.lazypizza.core.designsystem.theme.text3
import com.alexmls.lazypizza.core.designsystem.theme.typography
import com.alexmls.lazypizza.core.designsystem.theme.warning
import com.alexmls.lazypizza.history.presentation.ui_model.OrderCardUiModel
import com.alexmls.lazypizza.history.presentation.ui_model.OrderStatusUi

@Composable
fun OrderCard(
    order: OrderCardUiModel,
    modifier: Modifier = Modifier
) {
    val cardShape = MaterialTheme.shapes.large
    val shadowColor = Color(0x1003131F)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = cardShape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
            .background(Color.White, cardShape)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Order ${order.orderNumber}",
                    style = typography().titleSmall,
                    color = color().onBackground
                )

                Text(
                    text = order.dateTimeText,
                    style = typography().bodySmall,
                    color = color().secondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                order.items.forEach { item ->
                    Text(
                        text = item,
                        style = typography().bodySmall,
                        color = color().text3
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                StatusChip(status = order.status)

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Bottom) {
                    Text(
                        text = "Total amount:",
                        style = typography().bodySmall,
                        color = color().secondary
                    )
                    Text(
                        text = order.totalAmountText,
                        style = typography().titleSmall,
                        color = color().onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusChip(
    status: OrderStatusUi,
    modifier: Modifier = Modifier
) {
    val (label, background, textColor) = when (status) {
        OrderStatusUi.Completed -> Triple(
            "Completed",
            color().success,
            Color.White
        )
        OrderStatusUi.InProgress -> Triple(
            "InProgress",
            color().warning,
            Color.White
        )
    }

    Box(
        modifier = modifier
            .background(
                color = background,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = typography().labelSmall,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderCardPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            val sampleOrder = OrderCardUiModel(
                orderNumber = "#12345",
                dateTimeText = "September 25, 12:15",
                items = listOf(
                    "1 x Margherita",
                    "2 x Pepsi",
                    "2 x Cookies Ice Cream"
                ),
                totalAmountText = "$25.45",
                status = OrderStatusUi.Completed
            )

            OrderCard(order = sampleOrder)
        }
    }
}