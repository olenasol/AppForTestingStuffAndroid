package com.testapp.appfortesting.screens.rvdragdrop

interface ItemTouchHelperAdapter{
    fun onItemMove(fromPosition:Int,toPosition:Int)

    fun onItemDismissed(position:Int)
}