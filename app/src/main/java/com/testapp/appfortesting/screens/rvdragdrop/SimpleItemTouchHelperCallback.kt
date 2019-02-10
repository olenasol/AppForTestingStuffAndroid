package com.testapp.appfortesting.screens.rvdragdrop

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleItemTouchHelperCallback(val helper: ItemTouchHelperAdapter): ItemTouchHelper.Callback(){

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean {
        helper.onItemMove(viewHolder.adapterPosition,target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        helper.onItemDismissed(viewHolder.adapterPosition)
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is ItemTouchHelperViewHolder)
            (viewHolder as ItemTouchHelperViewHolder).onItemClear()
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if (viewHolder is ItemTouchHelperViewHolder)
                (viewHolder as ItemTouchHelperViewHolder).onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

}