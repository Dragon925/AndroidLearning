package com.github.dragon925.androidlearning

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SimpleItemDecoration(
    context: Context,
    space: Int = 10,
) : RecyclerView.ItemDecoration() {
    private val spaceInDp = context.dpToPx(space)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        outRect.left = spaceInDp
        outRect.right = spaceInDp
        outRect.bottom = spaceInDp
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spaceInDp
        }
    }
}
