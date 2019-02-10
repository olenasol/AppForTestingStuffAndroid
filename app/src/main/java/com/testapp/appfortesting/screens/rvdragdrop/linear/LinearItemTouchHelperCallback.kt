package com.testapp.appfortesting.screens.rvdragdrop.linear

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.testapp.appfortesting.screens.rvdragdrop.ItemTouchHelperAdapter
import com.testapp.appfortesting.screens.rvdragdrop.SimpleItemTouchHelperCallback
import kotlin.math.abs

class LinearItemTouchHelperCallback(helper: ItemTouchHelperAdapter) : SimpleItemTouchHelperCallback(helper){

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            val width = viewHolder.itemView.width
            viewHolder.itemView.alpha = 1f - abs(dX) /width
            viewHolder.itemView.translationX = dX
        } else
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}