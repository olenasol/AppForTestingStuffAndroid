package com.testapp.appfortesting.screens.rvdragdrop.grid

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.testapp.appfortesting.screens.rvdragdrop.ItemTouchHelperAdapter
import com.testapp.appfortesting.screens.rvdragdrop.SimpleItemTouchHelperCallback

class GridItemTouchHelperCallback(helper: ItemTouchHelperAdapter) : SimpleItemTouchHelperCallback(helper){

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }
}