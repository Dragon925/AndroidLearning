package com.github.dragon925.androidlearning.common.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleItemDecoration(
    context: Context,
    spaceInDp: Int = 10,
) : RecyclerView.ItemDecoration() {
    private val spaceInPx = context.dpToPx(spaceInDp)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = spaceInPx
        }
    }
}
