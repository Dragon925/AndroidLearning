package com.github.dragon925.androidlearning.news.ui.utils

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

class PlaceholderPainter(
    private val color: Color,
    private val width: Float,
    private val height: Float
) : Painter() {

    override val intrinsicSize: Size
        get() = Size(width, height)

    override fun DrawScope.onDraw() {
        drawRoundRect(color, cornerRadius = CornerRadius(4f.dp.value))
    }
}