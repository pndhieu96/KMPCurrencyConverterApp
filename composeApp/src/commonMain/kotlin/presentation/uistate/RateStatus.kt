package presentation.uistate

import androidx.compose.ui.graphics.Color
import freshColor
import staleColor

enum class RateStatus(
    val title: String,
    val color: Color
) {
    Idle(
        title = "Rates",
        color = Color.White
    ),
    Fresh(
        title = "Fresh",
        color = freshColor
    ),
    Stale(
        title = "Stale",
        color = staleColor
    )
}